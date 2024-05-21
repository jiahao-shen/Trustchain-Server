package com.hawkeye.model.convert;


import com.hawkeye.model.dto.CertificateDTO;
import com.hawkeye.model.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CertificateConvert {
    CertificateConvert INSTANCE = Mappers.getMapper(CertificateConvert.class);

    CertificateDTO certificateToCertificateDTO(Certificate certificate);
}
