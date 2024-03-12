package com.trustchain.controller;

import com.alibaba.fastjson.JSONObject;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.enums.OrganizationType;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.StatusCode;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.OrganizationInformation;
import com.trustchain.service.OrganizationService;
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


    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    /**
     * 机构选择列表
     *
     * @return
     */
    @GetMapping("/selectList")
    public ResponseEntity<Object> selectList() {
        List<Organization> orgs = orgService.selectList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationSelectItemList(orgs)));
    }

    /**
     * 判断机构是否存在
     *
     * @param request
     * @return
     */
    @PostMapping("/exist")
    public ResponseEntity<Object> exist(@RequestBody JSONObject request) {
        String orgName = request.getString("orgName");
        String orgId = request.getString("orgId");

        boolean result = orgService.exist(orgName, orgId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    /**
     * 机构注册申请
     *
     * @param request
     * @return
     */
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
        orgReg.setRegStatus(RegisterStatus.PENDING);

        String regId = orgService.registerApply(orgReg);

        if (regId != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", regId));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "注册申请失败", null));
        }
    }

    /**
     * 机构注册查询
     *
     * @param request
     * @return
     */
    @PostMapping("/register/apply/search")
    public ResponseEntity<Object> registerApplySearch(@RequestBody JSONObject request) {
        List<String> regIds = request.getJSONArray("regIds");

        List<OrganizationRegister> orgRegs = orgService.registerApplySearch(regIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterInformationList(orgRegs)));
    }

    /**
     * 机构注册列表
     *
     * @param request
     * @return
     */
    @PostMapping("/register/list")
    public ResponseEntity<Object> registerList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<OrganizationRegister> orgRegs = orgService.registerList(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterInformationList(orgRegs)));
    }

    @PostMapping("/register/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        String regId = request.getString("regId");

        OrganizationRegister orgReg = orgService.registerDetail(regId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterInformation(orgReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        String regId = request.getString("regId");
        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean flag = orgService.registerReply(regId, reply, reason);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", flag));
    }

    /**
     * 机构信息详情
     *
     * @param request
     * @return
     */
    @PostMapping("/information/detail")
    public ResponseEntity<Object> informationDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String version = request.getString("version");

        Organization org = orgService.informationDetail(orgId, version);
        OrganizationInformation orgInfo = OrganizationConvert.INSTANCE.toOrganizationInformation(org);
        orgInfo.setLatest(true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", orgInfo));
    }

    @PostMapping("/subordinate/list")
    public ResponseEntity<Object> subordinateList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<Organization> subOrgs = orgService.subordinateList(orgId);

        logger.info(subOrgs);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationInformationList(subOrgs)));
    }

    @PostMapping("/subordinate/detail")
    public ResponseEntity<Object> subordinateDetail(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        Organization org = orgService.subordinateDetail(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationInformation(org)));
    }

}
