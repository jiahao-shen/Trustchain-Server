package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.convert.OrganizationConvert;
import com.trustchain.model.enums.OrganizationType;
import com.trustchain.model.enums.RegisterStatus;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.OrganizationVO;
import com.trustchain.service.CaptchaService;
import com.trustchain.service.OrganizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        orgReg.setRegStatus(RegisterStatus.PENDING);

        String code = request.getString("code");
        // 判断验证码是否正确
        if (!captchaService.verify(orgReg.getEmail(), code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        String regId = orgService.registerApply(orgReg);

        if (regId != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", regId));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.REGISTER_FAILED, "未知错误", null));
        }
    }

    @PostMapping("/register/apply/search")
    public ResponseEntity<Object> registerApplySearch(@RequestBody JSONObject request) {
        List<String> regIds = request.getList("regIds", String.class);

        List<OrganizationRegister> orgRegs = orgService.registerApplySearch(regIds);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs)));
    }

    @PostMapping("/register/list")
    public ResponseEntity<Object> registerList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<OrganizationRegister> orgRegs = orgService.registerList(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVOList(orgRegs)));
    }

    @PostMapping("/register/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        String regId = request.getString("regId");

        OrganizationRegister orgReg = orgService.registerDetail(regId);

        logger.info(OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterVO(orgReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        String regId = request.getString("regId");
        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        boolean success = orgService.registerReply(regId, reply, reason);

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

        String code = request.getString("code");
        // 判断验证码是否正确
        if (!captchaService.verify(org.getEmail(), code)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.CAPTCHA_ERROR, "验证码不正确或已失效", null));
        }

        Organization updateOrg = orgService.informationUpdate(org);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", OrganizationConvert.INSTANCE.toOrganizationVO(updateOrg)));
    }

    @PostMapping("/information/history")
    public ResponseEntity<Object> informationHistory(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<Organization> orgs = orgService.informationHistory(orgId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", orgs));
    }

    @PostMapping("/information/rollback")
    public ResponseEntity<Object> informationRollback(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");
        String version = request.getString("version");

        boolean success = orgService.informationRollback(orgId, version);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", success));
    }

    @PostMapping("/subordinate/list")
    public ResponseEntity<Object> subordinateList(@RequestBody JSONObject request) {
        String orgId = request.getString("orgId");

        List<Organization> subOrgs = orgService.subordinateList(orgId);

        logger.info(subOrgs);

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
