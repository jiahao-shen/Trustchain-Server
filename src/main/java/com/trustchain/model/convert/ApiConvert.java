package com.trustchain.model.convert;

import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiInvokeApply;
import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.vo.ApiInvokeApplyVO;
import com.trustchain.model.vo.ApiRegisterVO;
import com.trustchain.model.vo.ApiVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApiConvert {
    ApiConvert INSTANCE = Mappers.getMapper(ApiConvert.class);

    Api toApi(ApiRegister api);

    ApiRegisterVO toApiRegisterVO(ApiRegister apiRegister);

    List<ApiRegisterVO> toApiRegisterVOList(List<ApiRegister> apiRegisterList);

    ApiVO toApiVO(Api api);

    List<ApiVO> toApiVOList(List<Api> apiList);

    ApiInvokeApplyVO toApiInvokeApplyVO(ApiInvokeApply apiInvokeApply);

    List<ApiInvokeApplyVO> toApiInvokeApplyVOList(List<ApiInvokeApply> apiInvokeApplyList);
}
