package com.trustchain;

import com.trustchain.mapper.*;
import com.trustchain.model.entity.ApiRequestBody;
import com.trustchain.model.entity.ApiRawBody;
import com.trustchain.model.entity.ApiResponseBody;
import com.trustchain.model.entity.*;
import com.trustchain.model.enums.*;
import com.trustchain.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class MockDataTest {
    @Autowired
    OrganizationRegisterMapper orgRegMapper;
    @Autowired
    OrganizationMapper orgMapper;
    @Autowired
    UserRegisterMapper userRegMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ApiRegisterMapper apiRegMapper;
    @Autowired
    ApiMapper apiMapper;
    @Autowired
    ApiInvokeApplyMapper apiInvokeApplyMapper;

    private static final Logger logger = LogManager.getLogger(MockDataTest.class);

    @Test
    void addOrganizationRegister() {
        for (int i = 0; i < 100; i++) {
            OrganizationRegister orgReg = new OrganizationRegister();
            orgReg.setLogo("tmp/5e87c537c4b2403bb2247d8cac035bc9.jpg");
            orgReg.setName("Test-Organization-" + (i + 1));
            orgReg.setType(OrganizationType.values()[new Random().nextInt(OrganizationType.values().length)]);
            orgReg.setTelephone("13915558435");
            orgReg.setEmail("1843781563@qq.com");
            orgReg.setCity("11,1101,110108");
            orgReg.setAddress("你在我在哪里?");
            orgReg.setIntroduction("随便写写的啦");
            orgReg.setSuperiorId("e675a62fa8f24e9ebc0cff4e1a1634c5");
            orgReg.setCreationTime(new Date());
            orgReg.setApplyTime(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));
            orgReg.setFile("tmp/333352dd75a641ec8b99c0fb333e74a9.zip");
            orgReg.setApplyStatus(ApplyStatus.values()[new Random().nextInt(ApplyStatus.values().length)]);

            orgRegMapper.insert(orgReg);
        }
    }

    @Test
    void addOrganization() {
        for (int i = 0; i < 100; i++) {
            Organization org = new Organization();
            org.setLogo("tmp/5e87c537c4b2403bb2247d8cac035bc9.jpg");
            org.setName("Test-Organization-" + (i + 1));
            org.setType(OrganizationType.values()[new Random().nextInt(OrganizationType.values().length)]);
            org.setTelephone("13915558435");
            org.setEmail("1843781563@qq.com");
            org.setCity("11,1101,110108");
            org.setAddress("你在我在哪里?");
            org.setIntroduction("随便写写的啦");
            org.setSuperiorId("e675a62fa8f24e9ebc0cff4e1a1634c5");
            org.setCreationTime(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(i)));
            org.setRegistrationTime(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));
            org.setFile("tmp/333352dd75a641ec8b99c0fb333e74a9.zip");

            orgMapper.insert(org);
        }
    }

    @Test
    void addUserRegister() {
        for (int i = 0; i < 100; i++) {
            UserRegister userReg = new UserRegister();
            userReg.setLogo("tmp/5e87c537c4b2403bb2247d8cac035bc9.jpg");
            userReg.setUsername("Test-User-1" + (i + 1));
            userReg.setPassword(PasswordUtil.encrypt("258667"));
            userReg.setTelephone("13915558435");
            userReg.setEmail("1843781563@qq.com");
            userReg.setRole(UserRole.values()[new Random().nextInt(UserRole.values().length)]);
            userReg.setOrganizationId("e675a62fa8f24e9ebc0cff4e1a1634c5");
            userReg.setApplyStatus(ApplyStatus.values()[new Random().nextInt(ApplyStatus.values().length)]);
            userReg.setReplyTime(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));

            userRegMapper.insert(userReg);
        }
    }

    @Test
    void addUser() {
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setLogo("tmp/5e87c537c4b2403bb2247d8cac035bc9.jpg");
            user.setUsername("Test-User-1" + (i + 1));
            user.setPassword(PasswordUtil.encrypt("258667"));
            user.setTelephone("13915558435");
            user.setEmail("1843781563@qq.com");
            user.setRole(UserRole.values()[new Random().nextInt(UserRole.values().length)]);
            user.setOrganizationId("e675a62fa8f24e9ebc0cff4e1a1634c5");
            user.setLastModified(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));

            userMapper.insert(user);
        }
    }

    @Test
    void addApiRegister() {
        for (int i = 0; i < 100; i++) {
            ApiRegister apiReg = new ApiRegister();
            apiReg.setApplyStatus(ApplyStatus.values()[new Random().nextInt(ApplyStatus.values().length)]);
            apiReg.setReplyTime(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));
            apiReg.setUrl("127.0.0.1");
            apiReg.setUserId("3759462c2a7449068052e77c3c014f6a");
            apiReg.setName("Test-Api-" + (i + 1));
            apiReg.setPrice(new Random().nextDouble());
            apiReg.setProtocol(InternetProtocol.HTTP);
            apiReg.setUrl("www.baidu.com");
            apiReg.setMethod(HttpMethod.values()[new Random().nextInt(HttpMethod.values().length)]);
            apiReg.setIntroduction("随便写写");
            apiReg.setVisible(ApiVisible.values()[new Random().nextInt(ApiVisible.values().length)]);
            apiReg.setQuery(new ArrayList());
            apiReg.setParam(new ArrayList());
            apiReg.setRequestHeader(new ArrayList());
            apiReg.setRequestBody(new ApiRequestBody(HttpRequestBodyType.NONE, new ArrayList(), new ArrayList(), new ApiRawBody(HttpBodyRawType.JSON, ""), "", ""));
            apiReg.setResponseHeader(new ArrayList());
            apiReg.setResponseBody(new ApiResponseBody(HttpResponseBodyType.NONE, new ApiRawBody(HttpBodyRawType.JSON, ""), "", ""));

            apiRegMapper.insert(apiReg);
        }
    }

    @Test
    void addApi() {
        for (int i = 0; i < 1; i++) {
            Api api = new Api();
            api.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            logger.info(api.getId());
            api.setUrl("127.0.0.1");
//            api.setUserId("3759462c2a7449068052e77c3c014f6a");
            api.setUserId("852cceb530414f4685685066093defe5");
            api.setName("Test-Api-" + (i + 1));
            api.setPrice(new Random().nextDouble());
            api.setProtocol(InternetProtocol.HTTP);
            api.setUrl("www.baidu.com");
            api.setMethod(HttpMethod.values()[new Random().nextInt(HttpMethod.values().length)]);
            api.setIntroduction("随便写写");
            api.setVisible(ApiVisible.values()[new Random().nextInt(ApiVisible.values().length)]);
            api.setQuery(new ArrayList());
            api.setParam(new ArrayList());
            api.setRequestHeader(new ArrayList());
            api.setRequestBody(new ApiRequestBody(HttpRequestBodyType.NONE, new ArrayList(), new ArrayList(), new ApiRawBody(HttpBodyRawType.JSON, ""), "", ""));
            api.setResponseHeader(new ArrayList());
            api.setResponseBody(new ApiResponseBody(HttpResponseBodyType.NONE, new ApiRawBody(HttpBodyRawType.JSON, ""), "", ""));

            apiMapper.insert(api);
        }
    }

    @Test
    void addApiInvokeApply() {
        List<Api> apis = apiMapper.selectAll();

        for (int i = 0; i < 100; i++) {
            ApiInvokeApply apiInvokeApply = new ApiInvokeApply();
            apiInvokeApply.setApiId(apis.get(new Random().nextInt(apis.size())).getId());
            apiInvokeApply.setUserId("4550aa8ee620484dad157d039d8b6909");
            apiInvokeApply.setApplyStatus(ApplyStatus.values()[new Random().nextInt(ApplyStatus.values().length)]);
            apiInvokeApply.setInvokeStatus(ApiInvokeStatus.values()[new Random().nextInt(ApiInvokeStatus.values().length)]);
            apiInvokeApply.setRange(ApiInvokeRange.SELF);
            apiInvokeApply.setStartTime(new Date(2024, Calendar.MARCH, 21));
            apiInvokeApply.setEndTime(new Date(2024, Calendar.APRIL, 25));

            apiInvokeApplyMapper.insert(apiInvokeApply);
        }
    }
}
