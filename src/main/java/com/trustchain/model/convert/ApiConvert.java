package com.trustchain.model.convert;

import com.trustchain.model.entity.*;
import com.trustchain.model.vo.*;
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

    ApiInvokeLogVO toApiInvokeLogVO(ApiInvokeLog apiInvokeLog);

    List<ApiInvokeLogVO> toApiInvokeLogVOList(List<ApiInvokeLog> apiInvokeLogList);

    ApiParamItemVO toApiParamItemVO(ApiParamItem apiParamItem);

    List<ApiParamItemVO> toApiParamItemVOList(List<ApiParamItem> apiParamItemList);

    ApiQueryItemVO toApiQueryItemVO(ApiQueryItem apiQueryItem);

    List<ApiQueryItemVO> toApiQueryItemVOList(List<ApiQueryItem> apiQueryItemList);

    ApiHeaderItemVO toApiHeaderItemVO(ApiHeaderItem apiHeaderItem);

    List<ApiHeaderItemVO> toApiHeaderItemVOList(List<ApiHeaderItem> apiHeaderItemList);

    ApiRequestBodyVO toApiRequestBodyVO(ApiRequestBody apiRequestBody);

    ApiResponseBodyVO toApiResponseBodyVO(ApiResponseBody apiResponseBody);

    ApiFormDataItemVO toApiFormDataItemVO(ApiFormDataItem apiFormDataItem);

    List<ApiFormDataItemVO> toApiFormDataItemVOList(List<ApiFormDataItem> apiFormDataItemList);

    ApiXwwwFormUrlEncodedItemVO toApiXwwwFormUrlEncodedItemVO(ApiXwwwFormUrlEncodedItem apiXwwwFormUrlEncodedItem);

    List<ApiXwwwFormUrlEncodedItemVO> toApiXwwwFormUrlEncodedItemVOList(List<ApiXwwwFormUrlEncodedItem> apiXwwwFormUrlEncodedItemList);

    ApiRawBodyVO toApiRawBodyVO(ApiRawBody apiRawBody);
}
