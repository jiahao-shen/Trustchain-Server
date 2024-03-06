package com.trustchain.service;

import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.controller.FileController;
import io.minio.*;
import com.trustchain.minio.MinioConfig;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {
    private MinioConfig config;

    private MinioClient client;
    private static final Logger logger = LogManager.getLogger(FileService.class);

    @Autowired
    FileService(MinioConfig config) {
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


    public void copy() {
        return;
    }

    public void move() {
        return;
    }

    public String presignedUrl(String file) {
        try {
            String url = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(config.getBucket())
                    .object(file).
                    build());
            return url;
        } catch (Exception e) {
            return null;
        }
    }

}
