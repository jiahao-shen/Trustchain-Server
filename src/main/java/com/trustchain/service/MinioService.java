package com.trustchain.service;

import io.minio.*;
import com.trustchain.minio.MinioConfig;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@Service
public class MinioService {
    private MinioConfig config;

    private MinioClient client;
    private static final Logger logger = LogManager.getLogger(MinioService.class);

    @Autowired
    MinioService(MinioConfig config) {
        this.config = config;
        this.client = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }

    //    @Async
    public String upload(MultipartFile file) {
        try {
            int suffixIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
            String suffix = file.getOriginalFilename().substring(suffixIndex);
            String fileName = "tmp/" + UUID.randomUUID().toString().replace("-", "") + suffix;
            client.putObject(PutObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            if (this.presignedUrl(fileName) != null) {
                return fileName;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public Boolean copy(String oldPath, String newPath) {
        try {
            client.copyObject(CopyObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(newPath)
                    .source(CopySource.builder().bucket(config.getBucket()).object(oldPath).build())
                    .build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean move(String oldPath, String newPath) {
        return false;
    }

    public String presignedUrl(String file) {
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(config.getBucket())
                    .object(file).
                    build());
        } catch (Exception e) {
            return null;
        }
    }

}
