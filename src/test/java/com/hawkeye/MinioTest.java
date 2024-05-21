package com.hawkeye;

import com.hawkeye.service.MinioService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class MinioTest {
    @Autowired
    private MinioService minioService;

    private static final Logger logger = LogManager.getLogger(MinioTest.class);

    @Test
    void testPresignedUrl() {
        logger.info(minioService.presignedUrl("tmp/687b0f3a31097ee3f70bd5935a68c37a.jpg"));
//        logger.info(minioService.presignedUrl("organization/5e87c537c4b2403bb2247d8cac035bc9.jpg"));
//        logger.info(minioService.presignedUrl("http://127.0.0.1:9000/trustchain/user/a2ca69b817e341b78d975ba14f17242b.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=cW3wffjaswmJBZEnBU2j%2F20240313%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240313T144641Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=2f53843466a9775824ad6905e889fef4168d6e488b4cc4f91e3631a03689f854"));
//        logger.info(minioService.isUrl("http://127.0.0.1:9000/trustchain/user/a2ca69b817e341b78d975ba14f17242b.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=cW3wffjaswmJBZEnBU2j%2F20240313%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240313T144641Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=2f53843466a9775824ad6905e889fef4168d6e488b4cc4f91e3631a03689f854"));
    }

    @Test
    void testGetObject() {
        String path = "tmp/7e6a91659f645109dfcd40f574bd5737.zip";
        logger.info(minioService.presignedUrl(path));
        InputStream io = minioService.get(path);

        try {
            byte[] bytes = IOUtils.toByteArray(io);
            logger.info(bytes);
            logger.info(bytes.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
