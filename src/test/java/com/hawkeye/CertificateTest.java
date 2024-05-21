package com.hawkeye;

import com.mybatisflex.core.query.QueryWrapper;
import com.hawkeye.mapper.CertificateMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CertificateTest {
    @Autowired
    private CertificateMapper certificateMapper;
    private static final Logger logger = LogManager.getLogger(MyBatisFlexTest.class);

    @Test
    void testCertificate() {
        QueryWrapper query = QueryWrapper.create()
                .select();
        logger.info(certificateMapper.selectOneByQuery(query));
    }

}
