package com.trustchain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.convert.CertificateConvert;
import com.trustchain.model.dto.CertificateDTO;
import com.trustchain.model.entity.ApiInvokeLog;
import com.trustchain.model.entity.Certificate;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.service.CertificateService;
import com.trustchain.service.ChainService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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

        return invoke(JSONObject.of(
                "appKey", "d28a9604b82f4a909b5204188806e99f",
                "secretKey", "7d273743fb7f4434b58c0ab0b574f500",
                "requestBody", JSONObject.of(
                        "type", "RAW",
                        "rawBody", JSONObject.of(
                                "type", "JSON",
                                "body", JSONObject.of(
                                        "key", certificate.getProductionId(),
                                        "field", "certificate",
                                        "value", JSON.toJSONString(certificate),
                                        "txId", certificate.getVersion()
                                )
                        )
                )
        ).toJSONString());
    }

    @Override
    public List<CertificateDTO> history(String productionId) {
        Certificate latest = JSON.parseObject(invoke(JSONObject.of(
                "appKey", "559c3c55bf9f42d88ac2b363bcec0883",
                "secretKey", "c12a56596f92491abe196002dbdc9956",
                "requestBody", JSONObject.of(
                        "type", "RAW",
                        "rawBody", JSONObject.of(
                                "type", "JSON",
                                "body", JSONObject.of(
                                        "key", productionId,
                                        "field", "certificate"
                                )
                        )
                )
        ).toJSONString()), Certificate.class);

        JSONArray histories = JSON.parseArray(invoke(JSONObject.of(
                "appKey", "82b22f330a93477abd27807ee8b1d9db",
                "secretKey", "2a117cdccfc443bfb66c5abbc60e1ebd",
                "requestBody", JSONObject.of(
                        "type", "RAW",
                        "rawBody", JSONObject.of(
                                "type", "JSON",
                                "body", JSONObject.of(
                                        "key", productionId,
                                        "field", "certificate"
                                )
                        )
                )
        ).toJSONString()));

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
        Certificate latest = JSON.parseObject(invoke(JSONObject.of(
                "appKey", "559c3c55bf9f42d88ac2b363bcec0883",
                "secretKey", "c12a56596f92491abe196002dbdc9956",
                "requestBody", JSONObject.of(
                        "type", "RAW",
                        "rawBody", JSONObject.of(
                                "type", "JSON",
                                "body", JSONObject.of(
                                        "key", productionId,
                                        "field", "certificate"
                                )
                        )
                )
        ).toJSONString()), Certificate.class);

        CertificateDTO cert;

        if (version.isEmpty() || version.equals("@latest")) {
            cert = CertificateConvert.INSTANCE.certificateToCertificateDTO((latest));
        } else {
            cert = JSON.parseObject(invoke(JSONObject.of(
                    "appKey", "559c3c55bf9f42d88ac2b363bcec0883",
                    "secretKey", "c12a56596f92491abe196002dbdc9956",
                    "requestBody", JSONObject.of(
                            "type", "RAW",
                            "rawBody", JSONObject.of(
                                    "type", "JSON",
                                    "body", JSONObject.of(
                                            "txId", version
                                    )
                            )
                    )
            ).toJSONString()), CertificateDTO.class);
        }
        cert.setLatest(cert.getVersion().equals(latest.getVersion()));

        return cert;
    }

    @Override
    public String update(Certificate certificate) {
        certificate.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        certificate.setLastModified(new Date());

        return invoke(JSONObject.of(
                "appKey", "d28a9604b82f4a909b5204188806e99f",
                "secretKey", "7d273743fb7f4434b58c0ab0b574f500",
                "requestBody", JSONObject.of(
                        "type", "RAW",
                        "rawBody", JSONObject.of(
                                "type", "JSON",
                                "body", JSONObject.of(
                                        "key", certificate.getProductionId(),
                                        "field", "certificate",
                                        "value", JSON.toJSONString(certificate),
                                        "txId", certificate.getVersion()
                                )
                        )
                )
        ).toJSONString());
    }

    public String invoke(String request) {
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(new Request.Builder()
                    .url("http://localhost:8081/api/invoke/sdk")
                    .post(okhttp3.RequestBody.create(MediaType.get("application/json"), request))
                    .build()).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                return JSONObject.parseObject(body).getJSONObject("data").getJSONObject("responseBody").getJSONObject("rawBody").getJSONObject("body").getString("data");
            } else {
                logger.info(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
