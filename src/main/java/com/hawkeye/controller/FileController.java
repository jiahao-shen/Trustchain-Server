package com.hawkeye.controller;

import com.alibaba.fastjson2.JSONObject;
import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.vo.BaseResponse;
import com.hawkeye.service.MinioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MinioService minioService;

    private static final Logger logger = LogManager.getLogger(FileController.class);

    @PostMapping("/upload")
    @ResponseBody
    public BaseResponse<String> upload(@RequestPart MultipartFile file) {
        logger.info("fileSize: " + file.getSize());
        String url = minioService.upload(file);
        if (url != null) {
            return new BaseResponse(StatusCode.SUCCESS, "上传成功", url);
        } else {
            return new BaseResponse(StatusCode.UPLOAD_FILE_FAILED, "上传失败", null);
        }
    }

    @PostMapping("/exist")
    @ResponseBody
    public BaseResponse<String> exist(@RequestBody JSONObject request) {
        String hash = request.getString("hash");

        String url = minioService.exist(hash);

        return new BaseResponse(StatusCode.SUCCESS, "", url);
    }
}
