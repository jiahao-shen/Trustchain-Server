package com.trustchain.model.convert;


import com.trustchain.model.dto.CertificateDTO;
import com.trustchain.model.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CertificateConvert {
    CertificateConvert INSTANCE = Mappers.getMapper(CertificateConvert.class);

    CertificateDTO certificateToCertificateDTO(Certificate certificate);
}
