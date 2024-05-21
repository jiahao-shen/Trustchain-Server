package com.hawkeye.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.hawkeye.exception.NoPermissionException;
import com.hawkeye.mapper.*;
import com.hawkeye.model.convert.ApiConvert;
import com.hawkeye.model.convert.UserConvert;
import com.hawkeye.model.dto.ApiDTO;
import com.hawkeye.model.entity.*;
import com.hawkeye.model.enums.*;
import com.hawkeye.service.ApiService;
import com.hawkeye.service.MinioService;
import com.hawkeye.service.WalletService;
import com.hawkeye.service.ChainService;
import com.hawkeye.util.AuthUtil;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.hawkeye.model.entity.table.ApiInvokeApplyTableDef.API_INVOKE_APPLY;
import static com.hawkeye.model.entity.table.ApiInvokeLogTableDef.API_INVOKE_LOG;
import static com.hawkeye.model.entity.table.ApiTableDef.API;
import static com.hawkeye.model.entity.table.OrganizationTableDef.ORGANIZATION;
import static com.hawkeye.model.entity.table.UserTableDef.USER;
import static com.hawkeye.model.entity.table.ApiRegisterTableDef.API_REGISTER;

@Service
public class ApiServiceImpl implements ApiService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiRegisterMapper apiRegMapper;
    @Autowired
    private ApiInvokeApplyMapper apiInvokeApplyMapper;
    @Autowired
    private ApiInvokeLogMapper apiInvokeLogMapper;
    @Autowired
    private MinioService minioService;
    @Autowired
    private ChainService chainService;

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
                .select(API_REGISTER.APPLY_ID,
                        API_REGISTER.NAME,
                        API_REGISTER.VISIBLE,
                        API_REGISTER.METHOD,
                        API_REGISTER.APPLY_STATUS,
                        API_REGISTER.APPLY_TIME)
                .from(API_REGISTER)
                .where(API_REGISTER.USER_ID.eq(userId));

        return apiRegMapper.selectListByQuery(query);
    }

    @Override
    public Page<ApiRegister> registerApplyList(String userId,
                                               Integer pageNumber,
                                               Integer pageSize,
                                               Map<String, List<String>> filter,
                                               Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(API_REGISTER.APPLY_ID,
                        API_REGISTER.NAME,
                        API_REGISTER.VISIBLE,
                        API_REGISTER.METHOD,
                        API_REGISTER.APPLY_STATUS,
                        API_REGISTER.APPLY_TIME)
                .from(API_REGISTER)
                .where(API_REGISTER.USER_ID.eq(userId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "method": {
                    query.where(API_REGISTER.METHOD.in(value.stream().map(HttpMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "applyStatus": {
                    query.where(API_REGISTER.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "visible": {
                    query.where(API_REGISTER.VISIBLE.in(value.stream().map(ApiVisible::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(API_REGISTER.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public ApiRegister registerApplyDetail(String applyId) {
        return apiRegMapper.selectOneById(applyId);
    }

    @Override
    public List<ApiRegister> registerApprovalList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .select(API_REGISTER.APPLY_ID,
                        API_REGISTER.NAME,
                        API_REGISTER.METHOD,
                        USER.USERNAME,
                        API_REGISTER.VISIBLE,
                        API_REGISTER.APPLY_STATUS,
                        API_REGISTER.APPLY_TIME)
                .from(API_REGISTER)
                .leftJoin(USER)
                .on(USER.ID.eq(API_REGISTER.USER_ID)
                        .and(USER.ORGANIZATION_ID.eq(orgId)));

        RelationManager.setMaxDepth(1);

        return apiRegMapper.selectListWithRelationsByQuery(query);
    }

    @Override
    public Page<ApiRegister> registerApprovalList(String orgId,
                                                  Integer pageNumber,
                                                  Integer pageSize,
                                                  Map<String, List<String>> filter,
                                                  Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(API_REGISTER.APPLY_ID,
                        API_REGISTER.NAME,
                        API_REGISTER.METHOD,
                        USER.USERNAME,
                        API_REGISTER.VISIBLE,
                        API_REGISTER.APPLY_STATUS,
                        API_REGISTER.APPLY_TIME)
                .from(API_REGISTER)
                .leftJoin(USER)
                .on(USER.ID.eq(API_REGISTER.USER_ID))
                .and(USER.ORGANIZATION_ID.eq(orgId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "method": {
                    query.where(API_REGISTER.METHOD.in(value.stream().map(HttpMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "applyStatus": {
                    query.where(API_REGISTER.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "visible": {
                    query.where(API_REGISTER.VISIBLE.in(value.stream().map(ApiVisible::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API_REGISTER.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(API_REGISTER.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiRegMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public ApiRegister registerApprovalDetail(String applyId) {
        RelationManager.setMaxDepth(1);

        ApiRegister apiReg = apiRegMapper.selectOneWithRelationsById(applyId);

        apiReg.setUrl(null);

        return apiReg;
    }

    @Override
    @Transactional
    public void registerReply(String applyId, ApplyStatus reply, String reason) {
        ApiRegister apiReg = apiRegMapper.selectOneById(applyId);
        if (apiReg == null) {
            throw new RuntimeException("API注册申请不存在");
        }
        if (reply == ApplyStatus.ALLOW) {
            Api api = ApiConvert.INSTANCE.apiRegToApi(apiReg);
            api.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());

            apiMapper.insert(api);
            api = apiMapper.selectOneById(api.getId());

            chainService.putState(api.getId(), "api", JSON.toJSONString(api), api.getVersion());

            apiReg.setId(api.getId());
            apiReg.setApplyStatus(ApplyStatus.ALLOW);
            apiReg.setReplyTime(new Date());

            apiRegMapper.update(apiReg);
        } else if (reply == ApplyStatus.REJECT) {
            apiReg.setApplyStatus(ApplyStatus.REJECT);
            apiReg.setReplyTime(new Date());
            apiReg.setReplyReason(reason);
            apiRegMapper.update(apiReg);
        }
    }

    @Override
    public List<Api> myApiList(User user) {
        QueryWrapper query = QueryWrapper.create()
                .select(API.ID,
                        API.NAME,
                        API.METHOD,
                        API.VISIBLE,
                        API.REGISTRATION_TIME,
                        API.LAST_MODIFIED)
                .from(API)
                .where(API.USER_ID.eq(user.getId()));

        return apiMapper.selectListByQuery(query);
    }

    @Override
    public Page<Api> myApiList(User user,
                               Integer pageNumber,
                               Integer pageSize,
                               Map<String, List<String>> filter,
                               Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(API.ID,
                        API.NAME,
                        API.METHOD,
                        API.VISIBLE,
                        API.REGISTRATION_TIME,
                        API.LAST_MODIFIED)
                .from(API)
                .where(API.USER_ID.eq(user.getId()));

        filter.forEach((key, value) -> {
            switch (key) {
                case "method": {
                    query.where(API.METHOD.in(value.stream().map(HttpMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "visible": {
                    query.where(API.VISIBLE.in(value.stream().map(ApiVisible::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API.REGISTRATION_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "registrationTime": {
                        query.orderBy(API.REGISTRATION_TIME, "ascending".equals(value));
                        break;
                    }
                    case "lastModified": {
                        query.orderBy(API.LAST_MODIFIED, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public List<Api> allApiList(User user) {
        QueryWrapper query = QueryWrapper.create()
                .select(API.ID,
                        API.NAME,
                        API.METHOD,
                        API.REGISTRATION_TIME,
                        USER.USERNAME,
                        ORGANIZATION.NAME,
                        ORGANIZATION.TYPE
                )
                .from(API)
                .leftJoin(USER)
                .on(USER.ID.eq(API.USER_ID))
                .leftJoin(ORGANIZATION)
                .on(ORGANIZATION.ID.eq(USER.ORGANIZATION_ID))
                .where(API.VISIBLE.eq(ApiVisible.PUBLIC))
                .or(API.VISIBLE.eq(ApiVisible.PRIVATE).and(USER.ORGANIZATION_ID.eq(user.getOrganizationId())));

        return apiMapper.selectListByQuery(query);
    }

    @Override
    public Page<Api> allApiList(User user,
                                String search,
                                Integer pageNumber,
                                Integer pageSize,
                                Map<String, List<String>> filter,
                                Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(API.ID,
                        API.NAME,
                        API.METHOD,
                        API.REGISTRATION_TIME,
                        USER.USERNAME,
                        ORGANIZATION.NAME,
                        ORGANIZATION.TYPE
                )
                .from(API)
                .leftJoin(USER)
                .on(USER.ID.eq(API.USER_ID))
                .leftJoin(ORGANIZATION)
                .on(ORGANIZATION.ID.eq(USER.ORGANIZATION_ID))
                .where(API.VISIBLE.eq(ApiVisible.PUBLIC).or(API.VISIBLE.eq(ApiVisible.PRIVATE).and(USER.ORGANIZATION_ID.eq(user.getOrganizationId()))));

        if (!search.isEmpty()) {
            String match = "%" + search.replaceAll("(?<=.)(?=.)", "%") + "%";
            query.where(API.NAME.like(match).or(ORGANIZATION.NAME.like(match)));
        }

        filter.forEach((key, value) -> {
            switch (key) {
                case "method": {
                    query.where(API.METHOD.in(value.stream().map(HttpMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "orgType": {
                    query.where(ORGANIZATION.TYPE.in(value.stream().map(OrganizationType::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API.REGISTRATION_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "registrationTime": {
                        query.orderBy(API.REGISTRATION_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public ApiDTO informationDetail(String apiId, String version, String userId) {
        ApiDTO apiDTO;
        // TODO: 对接长安链
        RelationManager.setMaxDepth(1);
        Api latest = apiMapper.selectOneWithRelationsById(apiId);

        if (version.equals("@latest")) {
            apiDTO = ApiConvert.INSTANCE.apiToApiDTO(latest);
        } else {
            apiDTO = JSON.parseObject(chainService.getState(version), ApiDTO.class);
            User user = userMapper.selectOneById(apiDTO.getUserId());
            apiDTO.setUser(UserConvert.INSTANCE.userToUserDTO(user));
        }

        apiDTO.setLatest(apiDTO.getVersion().equals(latest.getVersion()));

        return apiDTO;
    }

    @Override
    public boolean informationUpdate(Api api) {
        if (!AuthUtil.getUser().getId().equals(apiMapper.selectOneByEntityId(api).getUserId())) {
            throw new NoPermissionException("非API的所有者无法修改API信息");
        }

        api.setVersion(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        if (apiMapper.update(api, true) != 0) {
            api = apiMapper.selectOneById(api.getId());
            try {
                chainService.putState(api.getId(), "api", JSON.toJSONString(api), api.getVersion());
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<ApiDTO> informationHistory(String apiId) {
        Api latest = apiMapper.selectOneById(apiId);

        List<ApiDTO> apis = new ArrayList<>();

        JSONArray histories = JSON.parseArray(chainService.getHistory(apiId, "api"));

        histories.forEach(item -> {
            JSONObject tmp = (JSONObject) item;
            ApiDTO api = JSON.parseObject(tmp.getString("value"), ApiDTO.class);
            api.setLatest(api.getVersion().equals(latest.getVersion()));
            apis.add(api);
        });

        apis.sort(Comparator.comparing(ApiDTO::getLastModified).reversed());

        return apis;
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
                .select(API_INVOKE_APPLY.APPLY_ID,
                        API_INVOKE_APPLY.APPLY_STATUS,
                        API_INVOKE_APPLY.INVOKE_STATUS,
                        API_INVOKE_APPLY.APPLY_TIME,
                        API.NAME
                )
                .from(API_INVOKE_APPLY)
                .where(API_INVOKE_APPLY.USER_ID.eq(userId))
                .leftJoin(API).on(API.ID.eq(API_INVOKE_APPLY.API_ID))
                .orderBy(API_INVOKE_APPLY.APPLY_TIME, false);

        return apiInvokeApplyMapper.selectListByQuery(query);
    }

    @Override
    public Page<ApiInvokeApply> invokeApplyList(String userId,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                Map<String, List<String>> filter,
                                                Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(API_INVOKE_APPLY.APPLY_ID,
                        API_INVOKE_APPLY.APPLY_STATUS,
                        API_INVOKE_APPLY.INVOKE_STATUS,
                        API_INVOKE_APPLY.APPLY_TIME,
                        API.ID,
                        API.NAME)
                .from(API_INVOKE_APPLY)
                .where(API_INVOKE_APPLY.USER_ID.eq(userId))
                .leftJoin(API).on(API.ID.eq(API_INVOKE_APPLY.API_ID));

        filter.forEach((key, value) -> {
            switch (key) {
                case "applyStatus": {
                    query.where(API_INVOKE_APPLY.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "invokeStatus": {
                    query.where(API_INVOKE_APPLY.INVOKE_STATUS.in(value.stream().map(ApiInvokeStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API_INVOKE_APPLY.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(API_INVOKE_APPLY.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiInvokeApplyMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public ApiInvokeApply invokeApplyDetail(String applyId) {
        RelationManager.setMaxDepth(3);

        return apiInvokeApplyMapper.selectOneWithRelationsById(applyId);
    }

    @Override
    public List<ApiInvokeApply> invokeApprovalList(String userId) {
        QueryWrapper query = QueryWrapper.create()
                .from(API_INVOKE_APPLY)
                .leftJoin(API).on(API_INVOKE_APPLY.API_ID.eq(API.ID))
                .and(API.USER_ID.eq(userId));

        RelationManager.setMaxDepth(2);

        List<ApiInvokeApply> apiInvokeAppovalList = apiInvokeApplyMapper.selectListWithRelationsByQuery(query);

        apiInvokeAppovalList.forEach(item -> {
            item.setAppKey(null);
            item.setSecretKey(null);
        });

        return apiInvokeAppovalList;
    }

    @Override
    public Page<ApiInvokeApply> invokeApprovalList(String userId,
                                                   Integer pageNumber,
                                                   Integer pageSize,
                                                   Map<String, List<String>> filter,
                                                   Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .select(API_INVOKE_APPLY.APPLY_ID,
                        API_INVOKE_APPLY.APPLY_STATUS,
                        API_INVOKE_APPLY.APPLY_TIME,
                        API_INVOKE_APPLY.API_ID,
                        API_INVOKE_APPLY.USER_ID,
                        API.NAME
                )
                .from(API_INVOKE_APPLY)
                .leftJoin(API).on(API.ID.eq(API_INVOKE_APPLY.API_ID))
                .where(API.USER_ID.eq(userId));

        filter.forEach((key, value) -> {
            switch (key) {
                case "applyStatus": {
                    query.where(API_INVOKE_APPLY.APPLY_STATUS.in(value.stream().map(ApplyStatus::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API_INVOKE_APPLY.APPLY_TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "applyTime": {
                        query.orderBy(API_INVOKE_APPLY.APPLY_TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiInvokeApplyMapper.paginate(new Page(pageNumber, pageSize), query,
                fieldQueryBuilder -> fieldQueryBuilder
                        .field(ApiInvokeApply::getUser)
                        .queryWrapper(apiInvokeApply -> QueryWrapper.create()
                                .select(USER.ID, USER.USERNAME, USER.ORGANIZATION_ID)
                                .from(USER)
                                .where(USER.ID.eq(apiInvokeApply.getUserId()))),
                fieldQueryBuilder -> fieldQueryBuilder
                        .nestedField(User::getOrganization)
                        .queryWrapper(user -> QueryWrapper.create()
                                .select(ORGANIZATION.ID, ORGANIZATION.NAME)
                                .from(ORGANIZATION)
                                .where(ORGANIZATION.ID.eq(user.getOrganizationId())))
        );
    }

    @Override
    public ApiInvokeApply invokeApprovalDetail(String applyId) {
        RelationManager.setMaxDepth(3);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (!AuthUtil.getUser().getId().equals(apiInvokeApply.getApi().getUserId())) {
            throw new NoPermissionException("非本API的所有者无法查看API调用审批详情");
        }

        apiInvokeApply.setAppKey(null);
        apiInvokeApply.setSecretKey(null);

        return apiInvokeApply;
    }

    @Override
    @Transactional
    public void invokeReply(String applyId, ApplyStatus reply, String reason) {
        RelationManager.setMaxDepth(3);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (apiInvokeApply == null) {
            throw new RuntimeException("API调用申请不存在");
        }

        if (!AuthUtil.getUser().getId().equals(apiInvokeApply.getApi().getUserId())) {
            throw new NoPermissionException("非本API的所有者无法进行API调用审批");
        }

        apiInvokeApply.setApplyStatus(reply);
        apiInvokeApply.setReplyTime(new Date());

        if (reply == ApplyStatus.ALLOW) {
            apiInvokeApply.setAppKey(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            apiInvokeApply.setSecretKey(UUID.randomUUID().toString().replace("-", "").toLowerCase());

            Date now = new Date();
            if (now.after(apiInvokeApply.getStartTime()) && now.before(apiInvokeApply.getEndTime())) {
                apiInvokeApply.setInvokeStatus(ApiInvokeStatus.VALID);
            } else if (now.before(apiInvokeApply.getStartTime())) {
                apiInvokeApply.setInvokeStatus(ApiInvokeStatus.PENDING);
            } else if (now.after(apiInvokeApply.getEndTime())) {
                apiInvokeApply.setInvokeStatus(ApiInvokeStatus.EXPIRED);
            }
        } else if (reply == ApplyStatus.REJECT) {
            apiInvokeApply.setReplyReason(reason);
        }

        apiInvokeApplyMapper.update(apiInvokeApply);
    }

    @Override
    public ApiInvokeApply invokeInitialize(String applyId) {
        RelationManager.setMaxDepth(1);

        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        apiInvokeApply.getApi().setUrl(null);

        return apiInvokeApply;
    }

    @Override
    public List<ApiInvokeLog> invokeLogList(String userId, String applyId) {
        return null;
    }

    @Override
    public Page<ApiInvokeLog> invokeLogList(String userId,
                                            String applyId,
                                            String search,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            Map<String, List<String>> filter,
                                            Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .select(API_INVOKE_LOG.ID,
                        API_INVOKE_LOG.APPLY_ID,
                        API_INVOKE_LOG.INVOKE_USER_ID,
                        API_INVOKE_LOG.TIME,
                        API_INVOKE_LOG.STATUS_CODE,
                        API_INVOKE_LOG.INVOKE_METHOD,
                        API_INVOKE_APPLY.API_ID,
                        API.NAME,
                        API.USER_ID
                ).from(API_INVOKE_LOG)
                .leftJoin(API_INVOKE_APPLY).on(API_INVOKE_APPLY.APPLY_ID.eq(API_INVOKE_LOG.APPLY_ID))
                .leftJoin(API).on(API.ID.eq(API_INVOKE_APPLY.API_ID));

        if (applyId != null && !applyId.isEmpty()) {
            query.where(API_INVOKE_LOG.APPLY_ID.eq(applyId));
        } else {
            query.where(API_INVOKE_LOG.INVOKE_USER_ID.eq(userId).or(API.USER_ID.eq(userId)));
        }

        if (search != null && !search.isEmpty()) {
            query.where(API_INVOKE_LOG.ID.eq(search));
        }

        filter.forEach((key, value) -> {
            switch (key) {
                case "invokeMethod": {
                    query.where(API_INVOKE_LOG.INVOKE_METHOD.in(value.stream().map(ApiInvokeMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(API_INVOKE_LOG.TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "time": {
                        query.orderBy(API_INVOKE_LOG.TIME, "ascending".equals(value));
                        break;
                    }
                }
            });
        }

        return apiInvokeLogMapper.paginate(new Page(pageNumber, pageSize), query,
                fieldQueryBuilder -> fieldQueryBuilder
                        .field(ApiInvokeLog::getInvokeUser)
                        .queryWrapper(apiInvokeLog -> QueryWrapper.create()
                                .select(USER.ID, USER.USERNAME)
                                .from(USER)
                                .where(USER.ID.eq(apiInvokeLog.getInvokeUserId())))
        );
    }

    @Override
    public ApiInvokeLog invokeLogDetail(String id) {
        RelationManager.setMaxDepth(2);

        return apiInvokeLogMapper.selectOneWithRelationsById(id);
    }


    @Override
    public void invokeWeb(String applyId, String invokeUserId, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body) throws IOException {
        RelationManager.setMaxDepth(3);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (apiInvokeApply.getInvokeStatus() != ApiInvokeStatus.VALID) {
            // TODO: 不在有效期内就抛出异常
            throw new RuntimeException("调用申请不在有效期内");
        }
        // 获取调用申请Api
        Api api = apiInvokeApply.getApi();

        RelationManager.setMaxDepth(2);
        User invokeUser = userMapper.selectOneWithRelationsById(invokeUserId);
        // 获取调用者的钱包
        Wallet invokeUserWallet = invokeUser.getWallet();
        // 检查用户余额
        if (invokeUserWallet.getBalance() < api.getPrice()) {
            throw new RuntimeException("您的余额不足");
        }
        // 处理Param和Query
        HttpUrl requestUrl = handleParamAndQuery(api.getProtocol(), api.getUrl(), param, query);
        // 处理Header
        Request.Builder requestBuilder = handleRequestHeader(requestUrl, header);
        // 处理RequestBody
        RequestBody requestBody = handleRequestBody(body);
        // 处理调用
        Response response = handleMethodAndExecute(api.getMethod(), requestBuilder, requestBody);
        // 处理响应并写入日志
        handleResponse(apiInvokeApply, invokeUser, ApiInvokeMethod.WEB, param, query, header, body, response);
    }

    @SneakyThrows
    @Override
    public ApiInvokeLog invokeSDK(String appKey, String secretKey, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body) {
        QueryWrapper condition = QueryWrapper.create()
                .from(API_INVOKE_APPLY)
                .where(API_INVOKE_APPLY.APP_KEY.eq(appKey).and(API_INVOKE_APPLY.SECRET_KEY.eq(secretKey)));

        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsByQuery(condition);

        if (apiInvokeApply == null) {
            // TODO:
            throw new RuntimeException("AppKey或SecretKey不正确");
        }
        if (apiInvokeApply.getInvokeStatus() != ApiInvokeStatus.VALID) {
            // TODO: 不在有效期内就抛出异常
            throw new RuntimeException("调用申请不在有效期内");
        }

        // 获取调用申请Api
        Api api = apiInvokeApply.getApi();

        RelationManager.setMaxDepth(2);
        User invokeUser = userMapper.selectOneWithRelationsById(apiInvokeApply.getUserId());

        // 获取调用者的钱包
        Wallet invokeUserWallet = invokeUser.getWallet();
        // 检查用户余额
        if (invokeUserWallet.getBalance() < api.getPrice()) {
            throw new RuntimeException("您的余额不足");
        }
        // 处理Param和Query
        HttpUrl requestUrl = handleParamAndQuery(api.getProtocol(), api.getUrl(), param, query);
        // 处理Header
        Request.Builder requestBuilder = handleRequestHeader(requestUrl, header);
        // 处理RequestBody
        RequestBody requestBody = handleRequestBody(body);
        // 处理调用
        Response response = handleMethodAndExecute(api.getMethod(), requestBuilder, requestBody);
        // 处理响应并写入日志
        return handleResponse(apiInvokeApply, invokeUser, ApiInvokeMethod.SDK, param, query, header, body, response);
    }

    /**
     * @param protocol 协议
     * @param url      请求地址
     * @param param    Param参数
     * @param query    Query参数
     * @return 处理后的Url
     */
    private HttpUrl handleParamAndQuery(InternetProtocol protocol, String url, List<ApiParamItem> param, List<ApiQueryItem> query) {
        // 拼接协议和url
        String baseUrl = protocol.toString().toLowerCase() + "://" + url;

        // 处理param
        if (param != null) {
            for (ApiParamItem item : param) {
                baseUrl = baseUrl.replaceAll("\\{" + item.getKey() + "\\}", item.getValue());
            }
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();

        // 处理query
        if (query != null) {
            for (ApiQueryItem item : query) {
                urlBuilder.addQueryParameter(item.getKey(), item.getValue());
            }
        }

        return urlBuilder.build();
    }

    /**
     * @param url    请求地址
     * @param header 请求标头
     * @return
     */
    private Request.Builder handleRequestHeader(HttpUrl url, List<ApiHeaderItem> header) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (header != null) {
            for (ApiHeaderItem item : header) {
                requestBuilder.addHeader(item.getKey(), item.getValue());
            }
        }

        return requestBuilder;
    }

    /**
     * @param body 请求体
     * @return 处理后的请求体
     * @throws IOException
     */
    private RequestBody handleRequestBody(ApiRequestBody body) throws IOException {
        RequestBody requestBody = null;

        if (body != null) {
            switch (body.getType()) {
                case NONE:
                    break;
                case FORM_DATA: {
                    MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder();
                    for (ApiFormDataItem item : body.getFormDataBody()) {
                        if (item.getType().equals("File")) {    // 单独处理文件
                            String path = item.getValue();
                            byte[] bytes = IOUtils.toByteArray(minioService.get(path));
                            MediaType mediaType = MediaType.parse(new Tika().detect(bytes));
                            String fileName = path.substring(path.lastIndexOf("/") + 1);
                            formBodyBuilder.addFormDataPart(item.getKey(), fileName,
                                    RequestBody.create(bytes, mediaType));
                        } else {
                            formBodyBuilder.addFormDataPart(item.getKey(), item.getValue());
                        }
                    }
                    requestBody = formBodyBuilder.build();
                    break;
                }
                case X_WWW_FORM_URLENCODED: {
                    FormBody.Builder xwwwBodyBuilder = new FormBody.Builder();
                    for (ApiXwwwFormUrlEncodedItem item : body.getXwwwFormUrlEncodedBody()) {
                        xwwwBodyBuilder.add(item.getKey(), item.getValue());
                    }
                    requestBody = xwwwBodyBuilder.build();
                    break;
                }
                case RAW: {
                    MediaType mediaType = null;
                    switch (body.getRawBody().getType()) {
                        case TEXT: {
                            mediaType = MediaType.parse("text/plain");
                            break;
                        }
                        case JSON: {
                            mediaType = MediaType.parse("application/json");
                            break;
                        }
                        case HTML: {
                            mediaType = MediaType.parse("text/html");
                            break;
                        }
                        case XML: {
                            mediaType = MediaType.parse("text/xml");
                            break;
                        }
                        case JAVASCRIPT: {
                            // TODO:
                            mediaType = MediaType.parse("text/javascript");
                            break;
                        }
                    }
                    requestBody = RequestBody.create(body.getRawBody().getBody(), mediaType);
                    break;
                }
                case BINARY: {
                    String path = body.getBinaryBody();
                    InputStream io = minioService.get(path);
                    byte[] fileArray = IOUtils.toByteArray(io);
                    MediaType mediaType = MediaType.parse(new Tika().detect(fileArray));
                    requestBody = RequestBody.create(fileArray, mediaType);
                    break;
                }
                case GRAPHQL: {
                    MediaType mediaType = MediaType.parse("application/graphql");
                    requestBody = RequestBody.create(body.getRawBody().getBody(), mediaType);
                    break;
                }

            }
        }
        return requestBody;
    }

    /**
     * @param method         请求方法
     * @param requestBuilder 请求构造对象
     * @param requestBody    请求体
     * @return 响应对象
     * @throws IOException
     */
    private Response handleMethodAndExecute(HttpMethod method, Request.Builder requestBuilder, RequestBody requestBody) throws IOException {
        Request request = null;
        switch (method) {
            case GET: {
                request = requestBuilder.get().build();
                break;
            }
            case POST: {
                request = requestBuilder.post(requestBody).build();
                break;
            }
            case PUT: {
                request = requestBuilder.put(requestBody).build();
                break;
            }
            case DELETE: {
                request = requestBuilder.delete(requestBody).build();
                break;
            }
            case HEAD: {
                request = requestBuilder.head().build();
                break;
            }
            case PATCH: {
                request = requestBuilder.patch(requestBody).build();
                break;
            }
            case TRACE: {
                // TODO:
                break;
            }
            case CONNECT: {
                // TODO:
                break;
            }
            case OPTIONS: {
                // TODO:
                break;
            }
        }

        // TODO: 定制化策略
        OkHttpClient client = new OkHttpClient.Builder().build();

        return client.newCall(request).execute();
    }

    /**
     * 处理响应
     * <p>
     * applyId  调用申请号
     *
     * @param param    请求Param参数
     * @param query    请求Query参数
     * @param header   请求标头
     * @param body     请求体
     * @param response 响应体
     */
    @Transactional
    public ApiInvokeLog handleResponse(ApiInvokeApply apiInvokeApply,
                                       User invokeUser,
                                       ApiInvokeMethod invokeMethod,
                                       List<ApiParamItem> param,
                                       List<ApiQueryItem> query,
                                       List<ApiHeaderItem> header,
                                       ApiRequestBody body,
                                       Response response) throws IOException {
        // 创建调用日志
        ApiInvokeLog apiInvokeLog = new ApiInvokeLog();
        apiInvokeLog.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        apiInvokeLog.setApplyId(apiInvokeApply.getApplyId());
        apiInvokeLog.setInvokeUserId(invokeUser.getId());
        apiInvokeLog.setInvokeMethod(invokeMethod);
        apiInvokeLog.setStatusCode(response.code());
        apiInvokeLog.setParam(param);
        apiInvokeLog.setQuery(query);
        apiInvokeLog.setRequestHeader(header);

        // 移动临时文件
        if (body != null && body.getFormDataBody() != null) {
            body.getFormDataBody().forEach(item -> {
                if (item.getType().equals("File")) {
                    String oldPath = item.getValue();
                    String newPath = "api/invoke/log/" + apiInvokeLog.getId() + "/" + oldPath.substring(oldPath.lastIndexOf("/") + 1);
                    minioService.copy(oldPath, newPath);
                    item.setValue(newPath);
                }
            });
        }
        // 移动临时文件
        if (body != null && body.getBinaryBody() != null) {
            String oldPath = body.getBinaryBody();
            String newPath = "api/invoke/log/" + apiInvokeLog.getId() + "/" + oldPath.substring(oldPath.lastIndexOf("/") + 1);
            minioService.copy(oldPath, newPath);
            body.setBinaryBody(newPath);
        }
        apiInvokeLog.setRequestBody(body);

        // 获取对应的API
        Api api = apiInvokeApply.getApi();

        List<ApiHeaderItem> responseHeader = api.getResponseHeader();
        // 处理响应标头
        response.headers().forEach(item -> {
            ApiHeaderItem headerItem = responseHeader.stream().filter(h -> h.getKey().equals(item.getFirst())).findFirst().orElse(null);
            if (headerItem == null) {
                responseHeader.add(new ApiHeaderItem(item.getFirst(), "String", item.getSecond(), false, ""));
            } else {
                headerItem.setValue(item.getSecond());
            }
        });
        apiInvokeLog.setResponseHeader(responseHeader);

        if (response.isSuccessful()) {
            // 处理响应体
            ApiResponseBody responseBody = api.getResponseBody();
            switch (responseBody.getType()) {
                case NONE:
                    break;
                case RAW: {
                    String result = response.body().string();
                    responseBody.getRawBody().setBody(result);
                    break;
                }
                case BINARY: {
                    InputStream is = response.body().byteStream();
                    String path = minioService.upload(is, "api/invoke/log/" + apiInvokeLog.getId() + "/");
                    responseBody.setBinaryBody(path);
                    break;
                }
                case GRAPHQL: {
                    responseBody.setGraphQLBody(response.body().string());
                    break;
                }
            }
            apiInvokeLog.setResponseBody(responseBody);
            apiInvokeLogMapper.insert(apiInvokeLog);

            // 处理调用人交易
            walletService.purchase(invokeUser.getId(), api.getPrice(), TransactionChannel.API_INVOKE, apiInvokeLog.getId());
            // 处理所有者交易
            walletService.sale(api.getUserId(), api.getPrice(), TransactionChannel.API_INVOKE, apiInvokeLog.getId());
        } else {
            // 处理错误消息
            logger.info(response.message());
            apiInvokeLog.setErrorMessage(response.body().string());
            apiInvokeLogMapper.insert(apiInvokeLog);
        }
        response.close();

        return apiInvokeLogMapper.selectOneById(apiInvokeLog.getId());
    }
}
