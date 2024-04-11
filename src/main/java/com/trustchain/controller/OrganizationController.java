package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.mybatisflex.core.paginate.Page;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.OrganizationRegisterVO;
import com.trustchain.model.vo.OrganizationVO;
import com.trustchain.service.CaptchaService;
import com.trustchain.service.OrganizationService;
import com.trustchain.util.AuthUtil;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService orgService;

    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    @GetMapping("/selectList")
    @ResponseBody
    public BaseResponse<List<OrganizationVO>> selectList() {
        List<Organization> orgs = orgService.selectList();

        return new BaseResponse(StatusCode.SUCCESS, "",
                OrganizationConvert.INSTANCE.toOrganizationVOList(orgs));
    }

    @PostMapping("/exist")
    @ResponseBody
    public BaseResponse<Boolean> exist(@RequestBody JSONObject request) {
        String orgName = request.getString("orgName");
        String orgId = request.getString("orgId");

        boolean result = orgService.exist(orgName, orgId);

        return new BaseResponse(StatusCode.SUCCESS, "", result);
    }

    @PostMapping("/register/apply")
    @ResponseBody
    public BaseResponse<String> registerApply(@RequestBody JSONObject request) {
        OrganizationRegister orgReg = new OrganizationRegister();
        orgReg.setLogo(request.getString("logo"));
        orgReg.setName(request.getString("name"));
        orgReg.setType(OrganizationType.valueOf(request.getString("type")));
        orgReg.setTelephone(request.getString("telephone"));
        orgReg.setEmail(request.getString("email"));
        orgReg.setCity(request.getString("city"));
        orgReg.setAddress(request.getString("address"));
        orgReg.setIntroduction(request.getString("introduction"));
        orgReg.setSuperiorId(request.getString("superiorId"));
        orgReg.setCreationTime(request.getDate("creationTime"));
        orgReg.setFile(request.getString("file"));

        captchaService.verify(orgReg.getEmail(), request.getString("code"));

        String applyId = orgService.registerApply(orgReg);

        return new BaseResponse(StatusCode.SUCCESS, "", applyId);
    }

    @PostMapping("/register/apply/list")
    @ResponseBody
    public BaseResponse<Page<OrganizationRegisterVO>> registerApplyList(@RequestBody JSONObject request) {
        logger.info(request);
        List<String> applyIds = request.getList("applyIds", String.class);
        Integer pageNumebr = request.getInteger("pageNumber");
        Integer pageSize = request.getInteger("pageSize");
        Map<String, List<String>> filter = request.getObject("filter", new TypeReference<Map<String, List<String>>>() {
        });
        Map<String, String> sort = request.getObject("sort", new TypeReference<Map<String, String>>() {
        });

        Page<OrganizationRegister> orgRegs = orgService.registerApplyList(applyIds, pageNumebr, pageSize, filter, sort);

        return new BaseResponse(StatusCode.SUCCESS, "",
                new Page(OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs.getRecords()),
                        orgRegs.getPageNumber(), orgRegs.getPageSize(), orgRegs.getTotalRow()));
    }

    @PostMapping("/register/apply/detail")
    @ResponseBody
    public BaseResponse<OrganizationRegisterVO> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        OrganizationRegister orgReg = orgService.registerApplyDetail(applyId);

        return new BaseResponse<>(StatusCode.SUCCESS, "",
                OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg));
    }

    @PostMapping("/register/approval/list")
    @ResponseBody
    public BaseResponse<Page<OrganizationRegisterVO>> registerList(@RequestBody JSONObject request) {
        Integer pageNumebr = request.getInteger("pageNumber");
        Integer pageSize = request.getInteger("pageSize");
        Map<String, List<String>> filter = request.getObject("filter", new TypeReference<Map<String, List<String>>>() {
        });
        Map<String, String> sort = request.getObject("sort", new TypeReference<Map<String, String>>() {
        });

        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法获取机构注册审批列表");
        }

        Page<OrganizationRegister> orgRegs = orgService.registerApprovalList(user.getOrganizationId(), pageNumebr, pageSize, filter, sort);

        return new BaseResponse(StatusCode.SUCCESS, "",
                new Page(OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs.getRecords()),
                        orgRegs.getPageNumber(), orgRegs.getPageSize(), orgRegs.getTotalRow()));
    }

    @PostMapping("/register/approval/detail")
    @ResponseBody
    public BaseResponse<OrganizationRegisterVO> registerDetail(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法获取机构审批列表");
        }

        String applyId = request.getString("applyId");

        OrganizationRegister orgReg = orgService.registerApprovalDetail(applyId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg));
    }

    @PostMapping("/register/reply")
    @ResponseBody
    public BaseResponse<Boolean> registerReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法进行机构审批");
        }

        try {
            orgService.registerReply(applyId, reply, reason);
            return new BaseResponse(StatusCode.SUCCESS, "", true);
        } catch (RuntimeException e) {
            return new BaseResponse(StatusCode.SUCCESS, e.getMessage(), false);
        }
    }

    @PostMapping("/information/detail")
    @ResponseBody
    public BaseResponse<OrganizationVO> informationDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String version = request.getString("version");

        Organization org = orgService.informationDetail(orgId, version);
        OrganizationVO orgInfo = OrganizationConvert.INSTANCE.toOrganizationVO(org);
        orgInfo.setLatest(true);

        return new BaseResponse(StatusCode.SUCCESS, "", orgInfo);
    }

    @PutMapping("/information/update")
    @ResponseBody
    public BaseResponse<OrganizationVO> informationUpdate(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法修改机构信息");
        }

        Organization org = new Organization();

        org.setId(request.getString("orgId"));
        org.setLogo(request.getString("logo"));
        org.setName(request.getString("name"));
        org.setType(OrganizationType.valueOf(request.getString("type")));
        org.setCreationTime(request.getDate("creationTime"));
        org.setTelephone(request.getString("telephone"));
        org.setEmail(request.getString("email"));
        org.setCity(request.getString("city"));
        org.setAddress(request.getString("address"));
        org.setIntroduction(request.getString("introduction"));
        org.setFile(request.getString("file"));

        // 判断验证码是否正确
        captchaService.verify(org.getEmail(), request.getString("code"));

        Organization updateOrg = orgService.informationUpdate(org);

        return new BaseResponse(StatusCode.SUCCESS, "", OrganizationConvert.INSTANCE.toOrganizationVO(updateOrg));
    }

    @PostMapping("/information/history")
    @ResponseBody
    public BaseResponse<List<OrganizationVO>> informationHistory(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法查看机构信息历史记录");
        }

        String orgId = request.getString("orgId");

        List<Organization> orgs = orgService.informationHistory(orgId);

        return new BaseResponse(StatusCode.SUCCESS, "", OrganizationConvert.INSTANCE.toOrganizationVOList(orgs));
    }

    @PostMapping("/information/rollback")
    @ResponseBody
    public BaseResponse<Boolean> informationRollback(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法回滚机构信息");
        }

        String orgId = request.getString("orgId");
        String version = request.getString("version");

        boolean success = orgService.informationRollback(orgId, version);

        return new BaseResponse(StatusCode.SUCCESS, "", success);
    }

    @PostMapping("/subordinate/list")
    @ResponseBody
    public BaseResponse<Page<OrganizationVO>> subordinateList(@RequestBody JSONObject request) {
        Integer pageNumebr = request.getInteger("pageNumber");
        Integer pageSize = request.getInteger("pageSize");
        Map<String, List<String>> filter = request.getObject("filter", new TypeReference<Map<String, List<String>>>() {
        });
        Map<String, String> sort = request.getObject("sort", new TypeReference<Map<String, String>>() {
        });

        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法查看下级机构列表");
        }

        Page<Organization> subOrgs = orgService.subordinateList(user.getOrganizationId(), pageNumebr, pageSize, filter, sort);

        return new BaseResponse(StatusCode.SUCCESS, "",
                new Page(OrganizationConvert.INSTANCE.toOrganizationVOList(subOrgs.getRecords()),
                        subOrgs.getPageNumber(), subOrgs.getPageSize(), subOrgs.getTotalRow()));
    }

    @PostMapping("/subordinate/detail")
    @ResponseBody
    public BaseResponse<OrganizationVO> subordinateDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        Organization org = orgService.subordinateDetail(orgId);

        return new BaseResponse(StatusCode.SUCCESS, "",
                OrganizationConvert.INSTANCE.toOrganizationVO(org));
    }

}
