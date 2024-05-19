package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.dto.CertificateDTO;
import com.trustchain.model.entity.Certificate;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trustchain.service.CertificateService;

import java.util.List;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    private static final Logger logger = LogManager.getLogger(CertificateController.class);

    @PostMapping("/generate")
    public BaseResponse<String> generate(@RequestBody JSONObject request) {
        Certificate certificate = request.toJavaObject(Certificate.class);
        String txId = certificateService.generate(certificate);
        return new BaseResponse<>(StatusCode.SUCCESS, "", txId);
    }

    @PostMapping("/history")
    public BaseResponse<List<CertificateDTO>> history(@RequestBody JSONObject request) {
        String productionId = request.getString("productionId");
        List<CertificateDTO> certificateList = certificateService.history(productionId);
        return new BaseResponse<>(StatusCode.SUCCESS, "", certificateList);
    }

    @PostMapping("/detail")
    public BaseResponse<CertificateDTO> detail(@RequestBody JSONObject request) {
        String productionId = request.getString("productionId");
        String version = request.getString("version");
        CertificateDTO certificate = certificateService.detail(productionId, version);
        return new BaseResponse<>(StatusCode.SUCCESS, "", certificate);
    }

    @PostMapping("/update")
    public BaseResponse<Object> update(@RequestBody JSONObject request) {
        Certificate certificate = request.toJavaObject(Certificate.class);
        String txId = certificateService.update(certificate);
        return new BaseResponse<>(StatusCode.SUCCESS, "", txId);
    }

}
