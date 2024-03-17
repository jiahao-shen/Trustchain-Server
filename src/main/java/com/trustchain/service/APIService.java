package com.trustchain.service;

import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiRegister;

import java.util.List;

public interface APIService {
    /**
     * @param api
     * @return
     */
    boolean register(Api api);

    /**
     * @param apiReg
     * @return
     */
    boolean registerApply(ApiRegister apiReg);

    List<ApiRegister> registerApplyList(String userId);

    ApiRegister registerApplyDetail(String regId);

    List<ApiRegister> registerApprovalList(String orgId);

    ApiRegister registerApprovalDetail(String regId);
}
