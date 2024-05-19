package com.trustchain.service;

import com.trustchain.model.dto.CertificateDTO;
import com.trustchain.model.entity.Certificate;

import java.util.List;

public interface CertificateService {

    String generate(Certificate certificate);

    List<CertificateDTO> history(String productionId);

    CertificateDTO detail(String productionId, String version);

    String update(Certificate certificate);

}
