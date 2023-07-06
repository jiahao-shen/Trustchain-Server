package com.trustchain.controller.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trustchain.configuration.RedisConfig;
import com.trustchain.enums.UserType;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.service.EmailService;
import com.trustchain.service.MinioService;
import com.trustchain.model.User;
import com.trustchain.mapper.UserMapper;
import com.trustchain.model.Organization;
import com.trustchain.enums.OrganizationType;
import com.trustchain.model.OrganizationInfo;
import com.trustchain.model.OrganizationRegister;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.service.FabricService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.X11.XSystemTrayPeer;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
public class OrganizationController {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrganizationRegisterMapper organizationRegisterMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private FabricService fabricService;

    @Autowired
    private RedisConfig redisConfig = new RedisConfig();



    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    RedisConnectionFactory redisConnectionFactory = redisConfig.initConnectionFactory();

    /**
     * 机构注册申请
     */
    @PostMapping("/organization/register/apply")
    public ResponseEntity<Object> organizationRegisterApply(@RequestPart("logo") MultipartFile logo,
                                                            @RequestPart("info") JSONObject request,
                                                            @RequestPart("file") MultipartFile file, HttpSession session) {

        // 新注册申请
        OrganizationRegister organizationRegister = new OrganizationRegister();
        organizationRegister.setName(request.getString("name"));
        organizationRegister.setType(OrganizationType.valueOf(request.getString("type")));
        organizationRegister.setTelephone(request.getString("telephone"));
        organizationRegister.setEmail(request.getString("email"));
        organizationRegister.setCity(request.getString("city"));
        organizationRegister.setAddress(request.getString("address"));
        organizationRegister.setIntroduction(request.getString("introduction"));
        organizationRegister.setSuperior(Long.parseLong(request.getString("superior")));
        organizationRegister.setProvideNode(request.getBoolean("provideNode"));
        organizationRegister.setNumNodes(request.getInteger("numNodes"));
        organizationRegister.setPassword((request.getString("password")));
        organizationRegister.setStatus(RegisterStatus.PROCESSED);
        organizationRegister.setApplyTime(new Date());
        String verifyCode = request.getString("verifyCode");
        String email = request.getString("email");

        //adjudge the verifycode
        RedisTemplate redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);
        String verifyCodeRedis = (String)redisTemplate.opsForValue().get(email);
        if (StringUtils.isNotEmpty(verifyCode) && verifyCodeRedis.equals(verifyCode)) {
            redisTemplate.delete(email);
            int count = organizationRegisterMapper.insert(organizationRegister);

            if (count != 0) {
                String serialNumber = organizationRegister.getSerialNumber().toString();
                String logoPath = String.format("organization_register/%s/logo.jpg", serialNumber);
                String filePath = String.format("organization_register/%s/file.zip", serialNumber);
                // 上传至云盘
                minioService.upload(logo, logoPath);
                minioService.upload(file, filePath);
                // 更新Logo和File路径
                organizationRegister.setLogo(logoPath);
                organizationRegister.setFile(filePath);
                organizationRegisterMapper.updateById(organizationRegister);


                return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
            }
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("verify code is error or the verify code is timeout.");
        }

    }

    /**
     * 机构注册申请列表
     */
    @GetMapping("/organization/register/approval/list")
    public ResponseEntity<Object> organizationRegisterApplyList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        LambdaQueryWrapper<OrganizationRegister> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationRegister::getSuperior, login.getOrganization()).orderByDesc(OrganizationRegister::getApplyTime);

        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
    }

    /**
     * 查询注册申请进度
     */
    @PostMapping("/organization/register/apply/list")
    public ResponseEntity<Object> organizationRegisterApplyProgress(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        ArrayList<Long> serialNumbers = request.getObject("serialNumbers", ArrayList.class);

        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectBatchIds(serialNumbers);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
    }

    /**
     * 回复注册申请
     */
    // add  organization_user
    @PostMapping("/organization/register/reply")
    public ResponseEntity<Object> organizationRegisterReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));

        OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));

        // 创建机构
        Organization organization = new Organization();
        organization.setName(organizationRegister.getName());
        organization.setType(organizationRegister.getType());
        organization.setTelephone(organizationRegister.getTelephone());
        organization.setEmail(organizationRegister.getEmail());
        organization.setCity(organizationRegister.getCity());
        organization.setAddress(organizationRegister.getAddress());
        organization.setIntroduction(organizationRegister.getIntroduction());
        organization.setSuperior(organizationRegister.getSuperior());
        organization.setProvideNode(organizationRegister.isProvideNode());
        organization.setNumNodes(organizationRegister.getNumNodes());
        organization.setCreatedTime(new Date());


        // create organzation user
        int count = organizationMapper.insert(organization);

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构创建失败");
        }
        // query org.id
//        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Organization::getName, organizationRegister.getName());
//        Organization org = organizationMapper.selectOne(queryWrapper);


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(organization.getName());
        user.setPassword(encoder.encode(organizationRegister.getPassword()));
        user.setOrganization(Long.parseLong(String.valueOf(organization.getId())));
        Long admin = (long)1668531762319499265l;
        if (organization.getSuperior().equals(admin)){
            user.setType(UserType.ADMIN);
        }else{
            user.setType(UserType.NORMAL);
        }
        user.setCreatedTime(new Date());
        count = userMapper.insert(user);
        if (count == 0) {
            // TODO: need to delete organization
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user创建失败");
        }

        Long orgID = organization.getId();
        organizationRegister.setId(orgID);   // 机构注册绑定机构ID
        organizationRegister.setStatus(reply);  // 更新注册状态
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            organizationRegister.setReplyMessage(reason);   // 更新回复理由
        }
        organizationRegister.setReplyTime(new Date());  // 更新回复时间
        organizationRegisterMapper.updateById(organizationRegister);

        String newLogoPath = String.format("organization/%s/logo.jpg", orgID);
        String newFilePath = String.format("organization/%s/file.zip", orgID);
        // 云端复制文件
        minioService.copy(organizationRegister.getLogo(), newLogoPath);
        minioService.copy(organizationRegister.getFile(), newFilePath);
        // 存入数据库
        organization.setLogo(newLogoPath);
        organization.setFile(newFilePath);
        organizationMapper.updateById(organization);

//        // 存储上链
//        fabricService.saveOrganization(organization);

        // send email

        Boolean IsOk = emailService.send(organization.getEmail(), "Your Name is:"+organization.getName() + "\n"+ "Your Password is:"+organizationRegister.getPassword(),
                "Welcome to Data Share Platform");
        if (IsOk) {
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("email false");
        }
    }


    /**
     * 判断机构是否存在
     */
    @PostMapping("/organization/exist")
    public ResponseEntity<Object> organizationExist(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getName, request.getString("name"));

        Organization organization = organizationMapper.selectOne(queryWrapper);
        if (organization != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    /**
     * 获得全部机构用于选择
     */
    @GetMapping("/organization/selectList")
    public ResponseEntity<Object> organizationSelectList() {
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Organization::getId, Organization::getName);

        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }

    /**
     * 获取指定机构的n信息
     */
    @PostMapping("/organization/information")
    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong(request.getString("id")));

        System.out.println(organizationInfo);

        return ResponseEntity.status(HttpStatus.OK).body(organizationInfo);
    }

    @GetMapping("/organization/subordinate/list")
    public ResponseEntity<Object> organizationSubordinateList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getSuperior, login.getOrganization());

        List<Organization> subordinateList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(subordinateList);
    }

    /*
     * modify organization information
     */




    @PostMapping("/organization/modifyinformation")
    public ResponseEntity<Object> organizationModifyInformation(@RequestPart("logo") MultipartFile logo,
                                                                @RequestPart("info") JSONObject request,
                                                                HttpSession session) {
//        logger.info(request);
//        System.out.println("in");

        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong(request.getString("id")));
        organizationInfo.setName(request.getString("name"));
        organizationInfo.setType(OrganizationType.valueOf(request.getString("type")));
        organizationInfo.setTelephone(request.getString("telephone"));
        organizationInfo.setEmail(request.getString("email"));
        organizationInfo.setCity(request.getString("city"));
        organizationInfo.setAddress(request.getString("address"));
        organizationInfo.setIntroduction(request.getString("introduction"));
        //    update logo

        Long orgID = organizationInfo.getId();

        String logoPath = String.format("organization/%s/logo.jpg", orgID);
        // 上传至云盘
        minioService.upload(logo, logoPath);
        // 云端复制文件 to origin path
        //String newLogoPath = String.format("organization/%s/logo.jpg", orgID);
        //minioService.copy(logoPath,organizationInfo.getLogo());
        //minioService.copy(logoPath,newLogoPath);

        organizationInfo.setLogo(logoPath);

        int count = organizationMapper.updateById(organizationInfo);

        if(count != 0){
            return ResponseEntity.status(HttpStatus.OK).body(organizationInfo);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
        }
    }


    /**
     *
     */
    @PostMapping("/organization/register/information")
    public ResponseEntity<Object> getorganizationRegisterInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);
        System.out.println("test");
        System.out.println(request.getString("serialNumber"));
//        OrganizationRegister organizationRegisterInfo = organizationRegisterMapper.getOrganizationRegisterInformation(Long.parseLong(request.getString("serialNumber")));
        LambdaQueryWrapper<OrganizationRegister> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationRegister::getSerialNumber, request.getString("serialNumber"));

        OrganizationRegister organizationRegisterInfo = organizationRegisterMapper.selectOne(queryWrapper);


        System.out.println(organizationRegisterInfo);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterInfo);
    }

    @PostMapping("/organization/register/sendverifycode")
    public ResponseEntity<Object> sendVerifyCode(@RequestBody JSONObject request){

        String verifyCode = emailService.verifycode();
        String emailaddr = request.getString("email");
        String message = "Welcome to data platform. Your verify code value is: " + verifyCode + ". Valid for 5 minutes.";
        String subject = "Your data platform verify code";

        //save to the redis

        RedisTemplate redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);
        redisTemplate.opsForValue().set(emailaddr, verifyCode, 1, TimeUnit.MINUTES);

        Boolean success = emailService.send(emailaddr, message, subject);

        if (success) {
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
        }
    }



    // forgotpasswd, get verifycode
    @PostMapping("/organization/forgotpasswd/sendverifycode")
    public ResponseEntity<Object> sendForgotPasswdVerifyCode(@RequestBody JSONObject request){
        String verifyCode = emailService.verifycode();
        System.out.println(verifyCode);
        String emailaddr = request.getString("email");
        String message = "Reset Passwd. Your verify code value is: " + verifyCode + ". Valid for 5 minutes.";
        String subject = "Forgot Passwd";
        //save to the redis`
        RedisTemplate redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);
        redisTemplate.opsForValue().set(emailaddr, verifyCode, 1, TimeUnit.MINUTES);
        Boolean success = emailService.send(emailaddr, message, subject);
        if (success) {

            return ResponseEntity.status(HttpStatus.OK).body("success"); // body yao me chuan shi ti, yao me chuan duixiang
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
        }
    }

    // forgotpasswd, confirm verifycode
    @PostMapping("/organization/forgotpasswd/confirmverifycode")
    public ResponseEntity<Object> VerifyForgotPasswdCode(@RequestBody JSONObject request){
        String verifyCode = request.getString("verifyCode");
        String email = request.getString("email");
        //adjudge the verifycode
        RedisTemplate redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);
        String verifyCodeRedis = (String)redisTemplate.opsForValue().get(email);
        System.out.println(verifyCodeRedis);
        if (StringUtils.isNotEmpty(verifyCode) && verifyCodeRedis.equals(verifyCode)) {
            redisTemplate.delete(email);
            LambdaQueryWrapper<Organization> orgWrapper = new LambdaQueryWrapper<>();
            orgWrapper.eq(Organization::getEmail, request.getString("email"));
            Organization org = organizationMapper.selectOne(orgWrapper);
            System.out.println(org);
            // from org to userdmin
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getOrganization, org.getId());
            userWrapper.eq(User::getType, UserType.ADMIN);
            User user = userMapper.selectOne(userWrapper);
            System.out.println(user);
            return ResponseEntity.status(HttpStatus.OK).body(user); // body yao me chuan shi ti, yao me chuan duixiang
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
        }
    }


    // forgotpasswd, reset passwd
    @PostMapping("/organization/resetpasswd")
    public ResponseEntity<Object> ResetPasswdCode(@RequestBody JSONObject request,HttpSession session){
        System.out.println(request);
        //TODO: some question, how to limit user registering here directly?
        User login = (User) session.getAttribute("login");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // user has logined
        if (login != null) { // user has logined
            String username = login.getUsername();
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getUsername, username);
            User user = userMapper.selectOne(userWrapper);
            user.setPassword(encoder.encode(request.getString("newpassword")));
            int count = userMapper.updateById(user);
            if(count != 0) {
                return ResponseEntity.status(HttpStatus.OK).body("success"); // body yao me chuan shi ti, yao me chuan duixiang
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
            }
        }

        // if no user
        String username= request.getString("username");
        String password = request.getString("password"); // take passwd as token
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userWrapper);
        if (user.getPassword().equals(password)){
            user.setPassword(encoder.encode(request.getString("newpassword")));
            int count = userMapper.updateById(user);
            if(count != 0) {
                return ResponseEntity.status(HttpStatus.OK).body("success"); // body yao me chuan shi ti, yao me chuan duixiang
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
            }

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fault");
        }


    }


}