package com.trustchain.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LogManager.getLogger(FileController.class);

    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam("bucket") String bucket,
                                         @RequestParam("file") MultipartFile file) {
//        logger.info(bucket);
//        logger.info(file.getName());
//        logger.info(file.getSize());
        // TODO:
        return null;
    }
}
