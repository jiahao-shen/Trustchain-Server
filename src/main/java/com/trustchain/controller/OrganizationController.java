package com.trustchain.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.trustchain.convert.OrganizationConvert;
import com.trustchain.enums.OrganizationType;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.enums.StatusCode;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.OrganizationInformation;
import com.trustchain.model.vo.OrganizationRegisterInformation;
import com.trustchain.model.vo.OrganizationSelectItem;
import com.trustchain.service.OrganizationService;
import com.trustchain.util.CaptchaUtil;
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
    private CaptchaUtil captchaUtil;


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
        String orgName = request.getString("name");
        String orgID = request.getString("id");

        Boolean result = orgService.exist(orgName, orgID);

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
        orgReg.setSuperiorID(request.getString("superior"));
        orgReg.setCreationTime(request.getDate("creationTime"));
        orgReg.setFile(request.getString("file"));
        orgReg.setRegStatus(RegisterStatus.PENDING);

        String regID = orgService.registerApply(orgReg);

        if (regID != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse<>(StatusCode.SUCCESS, "注册申请成功", regID));
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
        List<String> regIDs = request.getJSONArray("regIDs");

        List<OrganizationRegister> orgRegs = orgService.registerApplySearch(regIDs);

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
        String id = request.getString("id");

        List<OrganizationRegister> orgRegs = orgService.registerList(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterInformationList(orgRegs)));
    }

    @PostMapping("/register/detail")
    public ResponseEntity<Object> registerDetail(@RequestBody JSONObject request) {
        String regID = request.getString("regID");

        OrganizationRegister orgReg = orgService.registerDetail(regID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "",
                        OrganizationConvert.INSTANCE.toOrganizationRegisterInformation(orgReg)));
    }

    @PostMapping("/register/reply")
    public ResponseEntity<Object> registerReply(@RequestBody JSONObject request) {
        logger.info(request);
        String regID = request.getString("regID");
        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
        String reason = request.getString("reason");

        orgService.registerReply(regID, reply, reason);

        return null;
    }

    /**
     * 机构信息详情
     *
     * @param request
     * @return
     */
    @PostMapping("/information/detail")
    public ResponseEntity<Object> informationDetail(@RequestBody JSONObject request) {
        String id = request.getString("id");
        String version = request.getString("version");

        Organization org = orgService.informationDetail(id, version);
        OrganizationInformation orgInfo = OrganizationConvert.INSTANCE.toOrganizationInformation(org);
        orgInfo.setLatest(true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse<>(StatusCode.SUCCESS, "", orgInfo));
    }

    @PostMapping("/captcha/send")
    public ResponseEntity<Object> sendCaptcha(@RequestBody JSONObject request) {
        String email = request.getString("email");

        Boolean result = captchaUtil.send(email);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

    @PostMapping("/captcha/verify")
    public ResponseEntity<Object> verifyCaptcha(@RequestBody JSONObject request) {
        String email = request.getString("email");
        String code = request.getString("code");

        Boolean result = captchaUtil.verify(email, code);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(StatusCode.SUCCESS, "", result));
    }

//    @PostMapping("/organization/register/apply")
//    public ResponseEntity<Object> organizationRegisterApply(@RequestPart("logo") MultipartFile logo,
//                                                            @RequestPart("info") JSONObject request,
//                                                            @RequestPart("file") MultipartFile file, HttpSession session) {
//
//        // 新注册申请
//        OrganizationRegister organizationRegister = new OrganizationRegister();
//        organizationRegister.setName(request.getString("name"));
//        organizationRegister.setType(OrganizationType.valueOf(request.getString("type")));
//        organizationRegister.setTelephone(request.getString("telephone"));
//        organizationRegister.setEmail(request.getString("email"));
//        organizationRegister.setCity(request.getString("city"));
//        organizationRegister.setAddress(request.getString("address"));
//        organizationRegister.setIntroduction(request.getString("introduction"));
//        organizationRegister.setSuperior(Long.parseLong(request.getString("superior")));
//        organizationRegister.setProvideNode(request.getBoolean("provideNode"));
//        organizationRegister.setNumNodes(request.getInteger("numNodes"));
//        organizationRegister.setStatus(RegisterStatus.PROCESSED);
//        organizationRegister.setApplyTime(new Date());
//
//        int count = organizationRegisterMapper.insert(organizationRegister);
//
//        if (count != 0) {
//            String serialNumber = organizationRegister.getSerialNumber().toString();
//            String logoPath = String.format("organization_register/%s/logo.jpg", serialNumber);
//            String filePath = String.format("organization_register/%s/file.zip", serialNumber);
//            // 上传至云盘
//            minioService.upload(logo, logoPath);
//            minioService.upload(file, filePath);
//            // 更新Logo和File路径
//            organizationRegister.setLogo(logoPath);
//            organizationRegister.setFile(filePath);
//            organizationRegisterMapper.updateById(organizationRegister);
//
//            return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
//        } else {
//            System.out.println("fuck");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
//        }
//    }
//
//    /**
//     * 机构注册申请列表
//     */
//    @GetMapping("/organization/register/approval/list")
//    public ResponseEntity<Object> organizationRegisterApplyList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
//        }
//
//        LambdaQueryWrapper<OrganizationRegister> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrganizationRegister::getSuperior, login.getOrganization()).orderByDesc(OrganizationRegister::getApplyTime);
//
//        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
//    }
//
//    /**
//     * 查询注册申请进度
//     */
//    @PostMapping("/organization/register/apply/list")
//    public ResponseEntity<Object> organizationRegisterApplyProgress(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        ArrayList<Long> serialNumbers = request.getObject("serialNumbers", ArrayList.class);
//
//        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectBatchIds(serialNumbers);
//
//        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
//    }
//
//    /**
//     * 回复注册申请
//     */
//    @PostMapping("/organization/register/reply")
//    public ResponseEntity<Object> organizationRegisterReply(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
//
//        OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));
//
//        // 创建机构
//        Organization organization = new Organization();
//        organization.setName(organizationRegister.getName());
//        organization.setType(organizationRegister.getType());
//        organization.setTelephone(organizationRegister.getTelephone());
//        organization.setEmail(organizationRegister.getEmail());
//        organization.setCity(organizationRegister.getCity());
//        organization.setAddress(organizationRegister.getAddress());
//        organization.setIntroduction(organizationRegister.getIntroduction());
//        organization.setSuperior(organizationRegister.getSuperior());
//        organization.setProvideNode(organizationRegister.isProvideNode());
//        organization.setNumNodes(organizationRegister.getNumNodes());
//        organization.setCreatedTime(new Date());
//
//        int count = organizationMapper.insert(organization);
//
//        if (count == 0) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构创建失败");
//        }
//
//        Long orgID = organization.getId();
//        organizationRegister.setId(orgID);   // 机构注册绑定机构ID
//        organizationRegister.setStatus(reply);  // 更新注册状态
//        if (reply == RegisterStatus.REJECT) {
//            String reason = request.getString("reason");
//            organizationRegister.setReplyMessage(reason);   // 更新回复理由
//        }
//        organizationRegister.setReplyTime(new Date());  // 更新回复时间
//        organizationRegisterMapper.updateById(organizationRegister);
//
//        String newLogoPath = String.format("organization/%s/logo.jpg", orgID);
//        String newFilePath = String.format("organization/%s/file.zip", orgID);
//        // 云端复制文件
//        minioService.copy(organizationRegister.getLogo(), newLogoPath);
//        minioService.copy(organizationRegister.getFile(), newFilePath);
//        // 存入数据库
//        organization.setLogo(newLogoPath);
//        organization.setFile(newFilePath);
//        organizationMapper.updateById(organization);
//
//        // 存储上链
//        fabricService.saveOrganization(organization);
//
//        return ResponseEntity.status(HttpStatus.OK).body(true);
//    }
//
//
//    /**
//     * 获取指定机构的信息
//     */
//    @PostMapping("/organization/information")
//    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
//        logger.info(request);
//
//        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong(request.getString("id")));
//
//        System.out.println(organizationInfo);
//
//        return ResponseEntity.status(HttpStatus.OK).body(organizationInfo);
//    }
//
//    @GetMapping("/organization/subordinate/list")
//    public ResponseEntity<Object> organizationSubordinateList(HttpSession session) {
//        User login = (User) session.getAttribute("login");
//
//        if (login == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
//        }
//
//        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Organization::getSuperior, login.getOrganization());
//
//        List<Organization> subordinateList = organizationMapper.selectList(queryWrapper);
//
//        return ResponseEntity.status(HttpStatus.OK).body(subordinateList);
//    }
}
