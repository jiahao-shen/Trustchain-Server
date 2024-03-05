package com.trustchain;

import com.trustchain.enums.UserRole;
import com.trustchain.enums.OrganizationType;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.User;
import com.trustchain.model.vo.UserInformationVO;
import com.trustchain.service.OrganizationService;
import com.trustchain.service.UserService;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
class SpringbootApplicationTests {
    @Autowired
    private OrganizationService orgService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(SpringbootApplicationTests.class);

    @Test
    void testOrganizationRegister() throws ParseException {
        Organization org = new Organization();

        org.setName("数据资源可信共享平台");
        org.setType(OrganizationType.PUBLIC);
        org.setTelephone("13915558435");
        org.setEmail("1843781563@qq.com");
        org.setCity("11,1101,110108");
        org.setAddress("学院路37号");
        org.setIntroduction("我们致力于建立一个安全、高效的平台，帮助用户实现数据的可信共享。我们的平台提供了一个可靠的环境，" +
                "使用户能够安全地共享和访问敏感数据，同时保护数据的隐私和安全。通过我们的平台，您可以与合作伙伴、" +
                "研究机构和其他利益相关者共享数据，促进创新和合作。我们采用先进的技术和严格的安全措施来保护数据的完整性和机密性，" +
                "并确保数据在传输和存储过程中得到适当的加密和权限控制。我们的平台还提供灵活的数据管理工具和分析功能，" +
                "帮助用户探索和利用数据的潜力。无论您是企业、研究机构还是政府部门，我们的数据资源可信共享平台将为您提供一个可靠的合作平台，" +
                "帮助您实现数据驱动的决策和创新。加入我们的平台，共享可信数据，推动您的业务和项目取得更大的成功！");
        org.setSuperiorID(null);

        org.setCreationTime(new SimpleDateFormat("yyyy-MM-dd").parse("2024-03-05"));
//        org.setRegistrationTime(new Date());
//        org.setLastModified(new Date());
        org.setVersion(null);

        orgService.register(org);
//        logger.info(org);
    }

    @Test
    void testUserRegister() {
        User user = new User();

        user.setUsername("admin");
        user.setPassword(PasswordUtil.encrypt("258667"));
        user.setOrganizationID("feb15885025d4b8590928d55d9083140");
        user.setTelephone("13915558435");
        user.setEmail("1843781563@qq.com");
        user.setRole(UserRole.ADMIN);

        userService.register(user);
    }

    @Test
    void testOrganizationSelectList() {
        orgService.selectList();
    }

//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private OrganizationRegisterMapper registerOrganizationMapper;
//
//    @Test
//    void testAddUser() {
////        userMapper.insert(new User("test", DigestUtils.sha256Hex("test"), "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png", "e993ed9a-9119-45e6-8561-005e218e2005"));
////        userMapper.insert(new User("admin", DigestUtils.sha256Hex("admin"), "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png", "e7dc5bfe-14f6-49e5-8023-bb5000c26bda"));
//    }
//
//    @Test
//    void testQueryUser() {
////        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
////        queryWrapper.select("username").eq("username", "plus").eq("password", "792dd7a5054293e4e7efcb50b896bbce8f58426285608bd8fcbdaf430f413d30");
////        User user = userMapper.selectOne(queryWrapper);
//        long startTime = System.nanoTime();
//
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", "plus");
//
//        for (int i = 0; i < 1000; i++) {
////            User user = userMapper.selectById("plus"); // 1137355770
//            // 1945233024
//
//            User user = userMapper.selectOne(queryWrapper);
//        }
//
//        long endTime = System.nanoTime();
//
////        logger.info(user);
//        logger.error(endTime - startTime);
//    }
//
//    @Test
//    void testFabric() {
//
//        FabricGateway fg = new FabricGateway();
//
//        System.out.println(fg.query("queryOrganizationList"));
////        System.out.println(fg.query("queryAPIList"));
////        fg.invoke("createOrganization", "test", "test", "test", "test", "", "now");
////        UUID apiID = UUID.randomUUID();
////        System.out.println(apiID);
////        System.out.println(new Date().getTime());
////        System.out.println(LocalDateTime.now());
//    }
//
//    @Test
//    void testCreateOrganization() {
//        FabricGateway fg = new FabricGateway();
//
////        fg.invoke("createOrganization", "1583391160430190593", "数据资源可信共享运营平台", "EDUCATION", "", "", "2022-10-21 17:34:05");
////        fg.invoke("createOrganization", "e7dc5bfe-14f6-49e5-8023-bb5000c26bda", "admin", "管理", "", "", LocalDateTime.now().toString());
//        System.out.println(fg.query("queryOrganizationList"));
//    }
//
//    @Test
//    void testCreateAPI() {
//        FabricGateway fg = new FabricGateway();
//
////        fg.invoke("createAPI", "", "", "", "", "", "", "", "", "", "", "", "", "");
//        System.out.println(fg.query("queryAPIList"));
//    }
//
//
//    @Test
//    void testUUID() {
//        System.out.println(UUID.randomUUID());
//    }
//
//    @Autowired
//    private OrganizationMapper organizationMapper;
//
//    @Test
//    void testRegisterOrganization() {
//        Organization org = new Organization();
//        org.setName("数据资源可信共享运营平台");
//        org.setType(OrganizationType.EDUCATION);
//        org.setTelephone("13915558435");
//        org.setEmail("1843781563@qq.com");
//        org.setCity("北京市,市辖区,海淀区");
//        org.setAddress("学院路37号");
//        org.setProvideNode(true);
//        org.setNumNodes(4);
//        org.setCreatedTime(new Date());
//
//        organizationMapper.insert(org);
//    }
//
//    @Test
//    void testBcrypto() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String pwd = encoder.encode("258667");
//        System.out.println(pwd);
//        System.out.println(encoder.matches("258667", pwd));
//    }
//
//    @Test
//    void testFastJson() {
////        Fuck you = new Fuck(1, Gender.BOY);
////        String jsonString = JSON.toJSONString(you);
////        System.out.println(jsonString);    // {"gender":"BOY","id":1}
//
//        Organization fuck = new Organization();
//
////        fuck.setCreatedTime(new Date());
////        fuck.setSuperior(null);
//        Long id = fuck.getSuperior();
//
//        JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(fuck));
//        obj.put("good", id);
//
//        System.out.println(obj);
//
////        fuck.setType(OrganizationType.EDUCATION);
////        System.out.println(fuck);
////        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingToString));
////        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingName));
////        System.out.println(JSON.toJSON(fuck).toString());
////        System.out.println(JSONObject.toJSONString(OrganizationType.EDUCATION));
////        Organization fuck = new Organization();
////
////        fuck.setType(OrganizationType.EDUCATION);
////        System.out.println(fuck);
////        System.out.println(OrganizationType.EDUCATION.name());
////        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingToString));
////        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingName));
////        System.out.println(JSON.toJSON(fuck).toString());
////        System.out.println(JSONObject.toJSONString(OrganizationType.EDUCATION));
//    }
//
//    @Test
//    void testSaveFile() throws Exception {
//        Resource res = new ClassPathResource("static");
//
//        File dir = new File(res.getFile().getPath() + "/123456");
//        if (dir.mkdir()) {
//            System.out.println("Godd");
//        }
//
//        Long fuck = Long.parseLong("1234353452525");
//        System.out.println("8432098/" + fuck + "/389283012");
//    }
//
//    @Test
//    void testAddAmin() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        User admin = new User();
//        admin.setUsername("admin");
//        admin.setPassword(encoder.encode("258667"));
//        admin.setOrganization(Long.parseLong("1583391160430190593"));
//        admin.setCreatedTime(new Date());
//
//        userMapper.insert(admin);
//    }
//
//    @Test
//    void testAPI() {
//        APIRegister apiRegister = new APIRegister();
//        apiRegister.setName("随便");
//        apiRegister.setUrl("https://www.baidu.com");
//        apiRegister.setMethod(HttpMethod.GET);
//        apiRegister.setVersion("1.0.0");
//        apiRegister.setStatus(RegisterStatus.PROCESSED);
//        apiRegister.setApplyTime(new Date());
//
//        System.out.println(JSONObject.toJSONString(apiRegister));
//    }
//
//    @Test
//    void testJoin() {
//        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong("1583391160430190593"));
//
////        JSONObject organizationInfo = organizationMapper.getOrganizationInformation("1585171695289765890");
//
//        System.out.println(organizationInfo);
//        System.out.println(JSONObject.toJSONString(organizationInfo, SerializerFeature.WriteNullStringAsEmpty));
//
////        Organization org = organizationMapper.fuck("1583391160430190593");
////        System.out.println(org);
//    }
//
//    @Autowired
//    private APIInvokeMapper apiInvokeMapper;
//
//    @Test
//    void testOrderByTime() {
//        Long id = Long.parseLong("1585170615678930946");
//
//        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(APIInvoke::getApplyTime);
//
//        List<APIInvokeApplyInfo> apiInvokeApplyList = apiInvokeMapper.getAPIInvokeApplyList(id, queryWrapper);
//
//        System.out.println(apiInvokeApplyList);
//    }
//
//    @Autowired
//    private MinioService minioService;
//
//    @Test
//    void testMinio() {
//        try {
////            minioSerive.listBuckets();
////            minioService.copy("organization_register/1593230328973996033/logo.jpg",
////                    "organization/1593230328973996033/logo.jpg");
//            minioService.createBucket("fuck");
//            System.out.println("success");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    void testCustomSerializer() {
//
//        OrganizationRegister or = registerOrganizationMapper.selectById(Long.parseLong("1593230328973996033"));
//
//        System.out.println(JSONObject.toJSONString(or));
//    }
}

