package com.trustchain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.convert.CertificateConvert;
import com.trustchain.model.dto.CertificateDTO;
import com.trustchain.model.entity.Certificate;
import com.trustchain.service.CertificateService;
import com.trustchain.service.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private ChainService chainService;

    private static final Logger logger = LogManager.getLogger(CertificateServiceImpl.class);

    @Override
    public String generate(Certificate certificate) {
        certificate.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        certificate.setCreationTime(new Date());
        certificate.setLastModified(new Date());
        return chainService.putState(certificate.getProductionId(), "certificate", JSON.toJSONString(certificate), certificate.getVersion());
    }

    @Override
    public List<CertificateDTO> history(String productionId) {
        Certificate latest = JSON.parseObject(chainService.getState(productionId, "certificate"), Certificate.class);

        JSONArray histories = JSON.parseArray(chainService.getHistory(productionId, "certificate"));

        List<CertificateDTO> certificates = new ArrayList<>();

        histories.forEach(item -> {
            JSONObject tmp = (JSONObject) item;
            CertificateDTO cert = JSON.parseObject(tmp.getString("value"), CertificateDTO.class);
            cert.setLatest(cert.getVersion().equals(latest.getVersion()));
            certificates.add(cert);
        });

        certificates.sort(Comparator.comparing(CertificateDTO::getLastModified).reversed());

        return certificates;
    }

    @Override
    public CertificateDTO detail(String productionId, String version) {
        Certificate latest = JSON.parseObject(chainService.getState(productionId, "certificate"), Certificate.class);

        CertificateDTO cert;

        if (version.isEmpty() || version.equals("@latest")) {
            cert = CertificateConvert.INSTANCE.certificateToCertificateDTO((latest));
        } else {
            cert = JSON.parseObject(chainService.getState(version), CertificateDTO.class);
        }

        cert.setLatest(cert.getVersion().equals(latest.getVersion()));

        return cert;
    }

    @Override
    public String update(Certificate certificate) {
        certificate.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        certificate.setLastModified(new Date());
        return chainService.putState(certificate.getProductionId(), "certificate", JSON.toJSONString(certificate), certificate.getVersion());
    }
}
