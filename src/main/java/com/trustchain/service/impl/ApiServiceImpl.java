package com.trustchain.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.mapper.ApiInvokeApplyMapper;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.mapper.ApiRegisterMapper;
import com.trustchain.mapper.UserMapper;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiInvokeApply;
import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.ApiVisible;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.service.ApiService;
import com.trustchain.util.AuthUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.trustchain.model.entity.table.ApiInvokeApplyTableDef.API_INVOKE_APPLY;
import static com.trustchain.model.entity.table.ApiTableDef.API;
import static com.trustchain.model.entity.table.UserTableDef.USER;
import static com.trustchain.model.entity.table.ApiRegisterTableDef.API_REGISTER;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiRegisterMapper apiRegMapper;
    @Autowired
    private ApiInvokeApplyMapper apiInvokeApplyMapper;
    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(ApiServiceImpl.class);

    @Override
    public boolean register(Api api) {
        int count = apiMapper.insert(api);

        return count != 0;
    }

    @Override
    public boolean registerApply(ApiRegister apiReg) {
        int count = apiRegMapper.insert(apiReg);

        return count != 0;
    }

    @Override
    public List<ApiRegister> registerApplyList(String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(ApiRegister.class)
                .where(ApiRegister::getUserId).eq(userId);

        RelationManager.setMaxDepth(1);
        return apiRegMapper.selectListByQuery(query);
    }

    @Override
    public ApiRegister registerApplyDetail(String applyId) {
        return apiRegMapper.selectOneWithRelationsById(applyId);
    }

    @Override
    public List<ApiRegister> registerApprovalList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .from(API_REGISTER)
                .leftJoin(USER)
                .on(USER.ID.eq(API_REGISTER.USER_ID)
                        .and(USER.ORGANIZATION_ID.eq(orgId)));

        RelationManager.setMaxDepth(1);
        List<ApiRegister> apiRegs = apiRegMapper.selectListWithRelationsByQuery(query);
        apiRegs.forEach(item -> {
            item.setUrl(StringUtil.repeat('*', 32));
        });
        return apiRegs;
    }

    @Override
    public ApiRegister registerApprovalDetail(String applyId) {
        RelationManager.setMaxDepth(1);

        ApiRegister apiReg = apiRegMapper.selectOneWithRelationsById(applyId);
        apiReg.setUrl(StringUtil.repeat('*', 32));

        return apiReg;
    }

    @Override
    public boolean registerReply(String applyId, ApplyStatus reply, String reason) {
        ApiRegister apiReg = apiRegMapper.selectOneById(applyId);

        if (apiReg == null) {
            return false;
        }

        if (reply == ApplyStatus.ALLOW) {
            Api api = ApiConvert.INSTANCE.toApi(apiReg);

            int count = apiMapper.insert(api);

            if (count != 0) {
                // TODO: 对接长安链
                apiReg.setId(api.getId());
                apiReg.setApplyStatus(ApplyStatus.ALLOW);
                apiReg.setReplyTime(new Date());

                apiRegMapper.update(apiReg);

                return true;
            }
        } else if (reply == ApplyStatus.REJECT) {
            apiReg.setApplyStatus(ApplyStatus.REJECT);
            apiReg.setReplyTime(new Date());
            apiReg.setReplyReason(reason);

            apiRegMapper.update(apiReg);

            return true;
        }

        return false;
    }

    @Override
    public List<Api> myApiList(User user) {
        QueryWrapper query = QueryWrapper.create()
                .from(Api.class)
                .where(Api::getUserId).eq(user.getId());

        return apiMapper.selectListByQuery(query);
    }

    @Override
    public List<Api> allApiList(User user) {
        QueryWrapper query = QueryWrapper.create()
                .from(API)
                .leftJoin(USER)
                .on(USER.ID.eq(API.USER_ID))
                .where(API.VISIBLE.eq(ApiVisible.PUBLIC))
                .or(API.VISIBLE.eq(ApiVisible.PRIVATE).and(USER.ORGANIZATION_ID.eq(user.getOrganizationId())));

        RelationManager.setMaxDepth(2);
        List<Api> apis = apiMapper.selectListWithRelationsByQuery(query);
        apis.forEach(item -> {
            if (!item.getUserId().equals(user.getId())) {
                item.setUrl(StringUtil.repeat('*', 32));
            }
        });

        return apis;
    }

    @Override
    public Api informationDetail(String apiId, String version, String userId) {
        // TODO: 对接长安链
        RelationManager.setMaxDepth(1);
        Api api = apiMapper.selectOneWithRelationsById(apiId);

        if (!api.getUserId().equals(userId)) {
            // 如果不是自己的API则隐藏Url
            api.setUrl(StringUtil.repeat('*', 32));
        }

        return api;
    }

    @Override
    public boolean informationUpdate(Api api) {
        // TODO: 对接长安链
        if (!AuthUtil.getUser().getId().equals(apiMapper.selectOneByEntityId(api).getUserId())) {
            throw new NoPermissionException("非API的所有者无法修改API信息");
        }

        int count = apiMapper.update(api, true);

        return count != 0;
    }

    @Override
    public List<Api> informationHistory(String apiId) {
        if (!AuthUtil.getUser().getId().equals(apiMapper.selectOneById(apiId).getUserId())) {
            throw new NoPermissionException("非API的所有者无法查看API信息历史记录");
        }
        // 对接长安链
        return null;
    }

    @Override
    public boolean informationRollback(String apiId, String version) {
        if (!AuthUtil.getUser().getId().equals(apiMapper.selectOneById(apiId).getUserId())) {
            throw new NoPermissionException("非API的所有者无法查看API信息历史记录");
        }
        // TODO: 对接长安链
        return false;
    }

    @Override
    public boolean invokeApply(ApiInvokeApply apiInvokeApply) {
        int count = apiInvokeApplyMapper.insert(apiInvokeApply);

        return count != 0;
    }

    @Override
    public List<ApiInvokeApply> invokeApplyList(String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(ApiInvokeApply.class)
                .where(ApiInvokeApply::getUserId).eq(userId);

        RelationManager.setMaxDepth(1);

        List<ApiInvokeApply> apiInvokeApplyList = apiInvokeApplyMapper.selectListWithRelationsByQuery(query);

        apiInvokeApplyList.forEach(item -> {
            item.getInvokeStatus();
            apiInvokeApplyMapper.update(item);
        });

        return apiInvokeApplyList;
    }

    @Override
    public ApiInvokeApply invokeApplyDetail(String applyId) {
        RelationManager.setMaxDepth(3);

        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);
        apiInvokeApply.getInvokeStatus();
        apiInvokeApplyMapper.update(apiInvokeApply);

        return apiInvokeApply;
    }

    @Override
    public List<ApiInvokeApply> invokeApprovalList(String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(API_INVOKE_APPLY)
                .leftJoin(API).on(API_INVOKE_APPLY.API_ID.eq(API.ID))
                .and(API.USER_ID.eq(userId));

        RelationManager.setMaxDepth(2);

        List<ApiInvokeApply> apiInvokeAppovalList = apiInvokeApplyMapper.selectListWithRelationsByQuery(query);

        return apiInvokeAppovalList;
    }

    @Override
    public ApiInvokeApply invokeApprovalDetail(String applyId) {
        RelationManager.setMaxDepth(3);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (!AuthUtil.getUser().getId().equals(apiInvokeApply.getApi().getUserId())) {
            throw new NoPermissionException("非本API的所有者无法查看API调用审批详情");
        }

        apiInvokeApply.getInvokeStatus();
        apiInvokeApplyMapper.update(apiInvokeApply);

        return apiInvokeApply;
    }

    @Override
    public boolean invokeReply(String applyId, ApplyStatus reply, String reason) {
        RelationManager.setMaxDepth(3);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (!AuthUtil.getUser().getId().equals(apiInvokeApply.getApi().getUserId())) {
            throw new NoPermissionException("非本API的所有者无法进行API调用审批");
        }

        apiInvokeApply.setApplyStatus(reply);
        apiInvokeApply.setReplyTime(new Date());

        if (reply == ApplyStatus.ALLOW) {
            apiInvokeApply.setAppKey(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            apiInvokeApply.setSecretKey(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        } else if (reply == ApplyStatus.REJECT) {
            apiInvokeApply.setReplyReason(reason);
        }
        apiInvokeApply.getInvokeStatus();

        int count = apiInvokeApplyMapper.update(apiInvokeApply);

        return count != 0;
    }
}
