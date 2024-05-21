package com.hawkeye.model.convert;

import com.hawkeye.model.dto.ApiDTO;
import com.hawkeye.model.entity.*;
import com.hawkeye.model.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ApiConvert {
    ApiConvert INSTANCE = Mappers.getMapper(ApiConvert.class);

    Api apiRegToApi(ApiRegister api);

    ApiRegisterVO apiRegToApiRegVO(ApiRegister apiRegister);

    List<ApiRegisterVO> apiRegListToApiRegVOList(List<ApiRegister> apiRegisterList);

    ApiVO apiToApiVO(Api api);

    ApiDTO apiToApiDTO(Api api);

    List<ApiDTO> apiListToApiDTOList(List<Api> apiList);

    ApiVO apiDTOToApiVO(ApiDTO apiDTO);

    List<ApiVO> apiDTOListToApiVOList(List<ApiDTO> apiDTOList);

    List<ApiVO> apiListToApiVOList(List<Api> apiList);

    ApiInvokeApplyVO apiInvokeApplyToApiInvokeApplyVO(ApiInvokeApply apiInvokeApply);

    List<ApiInvokeApplyVO> apiInvokeApplyListToApiInvokeApplyVOList(List<ApiInvokeApply> apiInvokeApplyList);

    ApiInvokeLogVO apiInvokeLogToApiInvokeLogVO(ApiInvokeLog apiInvokeLog);

    List<ApiInvokeLogVO> apiInvokeLogListToApiInvokeLogVOList(List<ApiInvokeLog> apiInvokeLogList);

    ApiParamItemVO apiParamItemToApiParamItemVO(ApiParamItem apiParamItem);

    List<ApiParamItemVO> apiParamItemListToApiParamItemVOList(List<ApiParamItem> apiParamItemList);

    ApiQueryItemVO apiQueryItemToApiQueryItemVO(ApiQueryItem apiQueryItem);

    List<ApiQueryItemVO> apiQueryItemListToApiQueryItemVOList(List<ApiQueryItem> apiQueryItemList);

    ApiHeaderItemVO apiHeaderItemToApiHeaderItemVO(ApiHeaderItem apiHeaderItem);

    List<ApiHeaderItemVO> apiHeaderItemListToApiHeaderItemVOList(List<ApiHeaderItem> apiHeaderItemList);

    ApiRequestBodyVO apiRequestBodyToApiRequestBodyVO(ApiRequestBody apiRequestBody);

    ApiResponseBodyVO apiResponseBodyToApiResponseBodyVO(ApiResponseBody apiResponseBody);

    ApiFormDataItemVO apiFormDataItemToApiFormDataItemVO(ApiFormDataItem apiFormDataItem);

    List<ApiFormDataItemVO> apiFormDataItemListToApiFormDataItemVOList(List<ApiFormDataItem> apiFormDataItemList);

    ApiXwwwFormUrlEncodedItemVO apiXwwwFormUrlEncodedItemToApiXwwwFormUrlEncodedItemVO(ApiXwwwFormUrlEncodedItem apiXwwwFormUrlEncodedItem);

    List<ApiXwwwFormUrlEncodedItemVO> apiXwwwFormUrlEncodedItemToApiXwwwFormUrlEncodedItemVOList(List<ApiXwwwFormUrlEncodedItem> apiXwwwFormUrlEncodedItemList);

    ApiRawBodyVO apiRawBodyToApiRawBodyVO(ApiRawBody apiRawBody);
}
