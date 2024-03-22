package com.trustchain;

import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.model.entity.Organization;
import com.trustchain.model.entity.OrganizationRegister;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.model.enums.OrganizationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class MockDataTest {
    @Autowired
    OrganizationRegisterMapper orgRegMapper;
    @Autowired
    OrganizationMapper orgMapper;

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
            logger.info(org.getCreationTime());
            org.setRegistrationTime(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(i)));
            logger.info(org.getRegistrationTime());
            org.setFile("tmp/333352dd75a641ec8b99c0fb333e74a9.zip");

            orgMapper.insert(org);
        }
    }
}
