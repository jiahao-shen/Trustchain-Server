package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.OrganizationVO;
import com.trustchain.service.CaptchaService;
import com.trustchain.service.OrganizationService;
import com.trustchain.util.AuthUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService orgService;

    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    @GetMapping("/selectList")
    public ResponseEntity<Object> selectList() {
        List<Organization> orgs = orgService.selectList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationSelectVOList(orgs)));
    }

    @PostMapping("/exist")
    public ResponseEntity<Object> exist(@RequestBody JSONObject request) {
        String orgName = request.getString("orgName");
        String orgId = request.getString("orgId");

        boolean result = orgService.exist(orgName, orgId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @PostMapping("/register/apply")
    public ResponseEntity<Object> registerApply(@RequestBody JSONObject request) {
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", applyId));
    }

    @PostMapping("/register/apply/list")
    public ResponseEntity<Object> registerApplyList(@RequestBody JSONObject request) {
        List<String> applyIds = request.getList("applyIds", String.class);

        List<OrganizationRegister> orgRegs = orgService.registerApplyList(applyIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs)));
    }
    @PostMapping("/register/apply/detail")
    public ResponseEntity<Object> registerApplyDetail(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");

        OrganizationRegister orgReg = orgService.registerApplyDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg)));
    }

    @GetMapping("/register/approval/list")
    public ResponseEntity<Object> registerList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法获取机构注册审批列表");
        }

        List<OrganizationRegister> orgRegs = orgService.registerApprovalList(user.getOrganizationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs)));
    }

    @PostMapping("/register/approval/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法获取机构审批列表");
        }

        String applyId = request.getString("applyId");

        OrganizationRegister orgReg = orgService.registerApprovalDetail(applyId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        String applyId = request.getString("applyId");
        ApplyStatus reply = ApplyStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        User user = AuthUtil.getUser();
        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法进行机构审批");
        }

        boolean success = orgService.registerReply(applyId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @PostMapping("/information/detail")
    public ResponseEntity<Object> informationDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String version = request.getString("version");

        Organization org = orgService.informationDetail(orgId, version);
        OrganizationVO orgInfo = OrganizationConvert.INSTANCE.toOrganizationVO(org);
        orgInfo.setLatest(true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", orgInfo));
    }

    @PutMapping("/information/update")
    public ResponseEntity<Object> informationUpdate(@RequestBody JSONObject request) {
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", OrganizationConvert.INSTANCE.toOrganizationVO(updateOrg)));
    }

    @PostMapping("/information/history")
    public ResponseEntity<Object> informationHistory(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法查看机构信息历史记录");
        }

        String orgId = request.getString("orgId");

        List<Organization> orgs = orgService.informationHistory(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", orgs));
    }

    @PostMapping("/information/rollback")
    public ResponseEntity<Object> informationRollback(@RequestBody JSONObject request) {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法回滚机构信息");
        }

        String orgId = request.getString("orgId");
        String version = request.getString("version");

        boolean success = orgService.informationRollback(orgId, version);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @GetMapping("/subordinate/list")
    public ResponseEntity<Object> subordinateList() {
        User user = AuthUtil.getUser();

        if (!user.isAdmin()) {
            throw new NoPermissionException("非管理员无法查看下级机构列表");
        }

        List<Organization> subOrgs = orgService.subordinateList(user.getOrganizationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationVOList(subOrgs)));
    }

    @PostMapping("/subordinate/detail")
    public ResponseEntity<Object> subordinateDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        Organization org = orgService.subordinateDetail(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationVO(org)));
    }

}
