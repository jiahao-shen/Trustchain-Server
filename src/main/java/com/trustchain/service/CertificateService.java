package com.trustchain.service;

import com.trustchain.model.entity.Certificate;

import java.util.List;

public interface CertificateService {

    boolean generate(Certificate certificate);

    List<Certificate> getCertificateHistory(String certificateId);

    Certificate informationDetail(String certificateId, String version);

    Certificate informationUpdate(Certificate certificate);

}
