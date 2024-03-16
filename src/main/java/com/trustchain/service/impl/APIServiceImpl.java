package com.trustchain.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.mapper.APIMapper;
import com.trustchain.mapper.APIRegisterMapper;
import com.trustchain.model.entity.API;
import com.trustchain.model.entity.APIRegister;
import com.trustchain.service.APIService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class APIServiceImpl implements APIService {
    @Autowired
    private APIMapper apiMapper;
    @Autowired
    private APIRegisterMapper apiRegMapper;

    private static final Logger logger = LogManager.getLogger(APIServiceImpl.class);

    @Override
    public boolean register(API api) {
        int count = apiMapper.insert(api);

        return count != 0;
    }

    @Override
    public boolean registerApply(APIRegister apiReg) {
        int count = apiRegMapper.insert(apiReg);

        return count != 0;
    }

    @Override
    public List<APIRegister> registerApplyList(String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(APIRegister.class)
                .where(APIRegister::getUserId).eq(userId);

        RelationManager.setMaxDepth(1);
        return apiRegMapper.selectListByQuery(query);
    }

    @Override
    public APIRegister registerApplyDetail(String regId) {
        return apiRegMapper.selectOneWithRelationsById(regId);
    }
}
