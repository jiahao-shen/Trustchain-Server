package com.trustchain.service;

import io.minio.MinioClient;
import com.trustchain.minio.MinioConfig;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private MinioConfig config;

    private MinioClient client;

    @Autowired
    FileService(MinioConfig config) {
        this.config = config;
        this.client = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }

    @Async
    public Boolean upload(MultipartFile file, String path) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }


}
