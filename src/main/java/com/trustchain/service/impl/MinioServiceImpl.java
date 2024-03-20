package com.trustchain.service.impl;

import com.trustchain.config.MinioConfig;
import com.trustchain.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioServiceImpl implements MinioService {

    private final MinioConfig config;

    private final MinioClient client;
    private static final Logger logger = LogManager.getLogger(MinioServiceImpl.class);

    @Autowired
    MinioServiceImpl(MinioConfig config) {
        this.config = config;
        this.client = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            // 分析文件类型
            MimeType mediaType = MimeTypes.getDefaultMimeTypes().forName(new Tika().detect(file.getBytes()));
            // 获取文件后缀
            String extension = mediaType.getExtension();
            // 获取文件MD5值解决重复上传问题
            String fileName = "tmp/" + DigestUtils.md5Hex(file.getInputStream()) + extension;
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
        } catch (IOException | MimeTypeException | ErrorResponseException | InsufficientDataException |
                 InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException |
                 ServerException | XmlParserException e) {
            return null;
        }
    }

    @Override
    public boolean copy(String oldPath, String newPath) {
        try {
            client.copyObject(CopyObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(newPath)
                    .source(CopySource.builder().bucket(config.getBucket()).object(oldPath).build())
                    .build());
            return true;
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            return false;
        }
    }

    @Override
    public boolean move(String oldPath, String newPath) {
        return false;
    }

    @Override
    public String presignedUrl(String file) {
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(config.getBucket())
                    .object(file)
                    .build());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isUrl(String path) {
        try {
            new URL(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream get(String file) {
        try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(file)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            return null;
        }
    }
}
