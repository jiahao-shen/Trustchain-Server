package com.trustchain.model.convert;


import com.trustchain.model.entity.Certificate;
import com.trustchain.model.vo.CertificateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CertificateConvert {
    CertificateConvert INSTANCE = Mappers.getMapper(CertificateConvert.class);

    CertificateVO toCertificateVO(Certificate certificate);
}
