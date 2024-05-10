package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.trustchain.model.entity.Certificate;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    @PostMapping("/generate")
    public BaseResponse<Object> generateCertificate(@RequestBody JSONObject request){
        Certificate certificate = request.toJavaObject(Certificate.class);
        boolean success = certificateService.generate(certificate);
        return new BaseResponse<>(StatusCode.SUCCESS, "", success);
    }

    @PostMapping("/information/history")
    public BaseResponse<Object> getCertificateHistory(@RequestBody JSONObject request){
        String certificateId = request.getString("certificateId");
        List<Certificate> certificateList = certificateService.getCertificateHistory(certificateId);
        return new BaseResponse<>(StatusCode.SUCCESS, "", certificateList);
    }

    @PostMapping("/information/detail")
    public BaseResponse<Object> certificateInfoDetail(@RequestBody JSONObject request){
        String certificateId = request.getString("certificateId");
        String version = request.getString("version");
        Certificate certificate = certificateService.informationDetail(certificateId, version);
        return new BaseResponse<>(StatusCode.SUCCESS, "", certificate);
    }

    @PostMapping("/information/update")
    public BaseResponse<Object> certificateUpdate(@RequestBody JSONObject jsonObject){
        Certificate certificate = jsonObject.toJavaObject(Certificate.class);
        return new BaseResponse<>(StatusCode.SUCCESS, "", certificate);
    }

}
