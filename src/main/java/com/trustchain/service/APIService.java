package com.trustchain.service;

import com.trustchain.model.entity.API;
import com.trustchain.model.entity.APIRegister;

import java.util.List;

public interface APIService {
    /**
     * @param api
     * @return
     */
    boolean register(API api);

    /**
     * @param apiReg
     * @return
     */
    boolean registerApply(APIRegister apiReg);

    List<APIRegister> registerApplyList(String userId);
}
