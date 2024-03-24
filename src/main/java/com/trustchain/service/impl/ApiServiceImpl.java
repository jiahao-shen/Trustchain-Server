package com.trustchain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.mapper.ApiInvokeApplyMapper;
import com.trustchain.mapper.ApiInvokeLogMapper;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.mapper.ApiRegisterMapper;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.dto.*;
import com.trustchain.model.entity.*;
import com.trustchain.model.enums.*;
import com.trustchain.service.ApiService;
import com.trustchain.service.MinioService;
import com.trustchain.util.AuthUtil;
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
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.trustchain.model.entity.table.ApiInvokeApplyTableDef.API_INVOKE_APPLY;
import static com.trustchain.model.entity.table.ApiTableDef.API;
import static com.trustchain.model.entity.table.OrganizationTableDef.ORGANIZATION;
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
    private ApiInvokeLogMapper apiInvokeLogMapper;
    @Autowired
    private MinioService minioService;

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
            Api api = ApiConvert.INSTANCE.toApi(apiReg);
            apiMapper.insert(api);

            // TODO: 对接长安链
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
    public Api informationDetail(String apiId, String version, String userId) {
        // TODO: 对接长安链
        RelationManager.setMaxDepth(1);
        Api api = apiMapper.selectOneWithRelationsById(apiId);

        if (!api.getUserId().equals(userId)) {
            // 如果不是自己的API则隐藏Url
            api.setUrl(null);
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
    public Page<Api> informationHistory(String apiId, Integer pageNumber, Integer pageSize, Map<String, List<String>> filter, Map<String, String> sort) {
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
                        API_INVOKE_APPLY.USER_ID)
                .from(API_INVOKE_APPLY);

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
                        .field(ApiInvokeApply::getApi)
                        .queryWrapper(apiInvokeApply -> QueryWrapper.create()
                                .select(API.NAME)
                                .from(API)
                                .where(API.ID.eq(apiInvokeApply.getApiId()))),
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
        } else if (reply == ApplyStatus.REJECT) {
            apiInvokeApply.setReplyReason(reason);
        }
        apiInvokeApply.getInvokeStatus();

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
    public void invokeWeb(String applyId, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body) throws IOException {
        RelationManager.setMaxDepth(1);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (apiInvokeApply.getInvokeStatus() != ApiInvokeStatus.VALID) {
            // TODO: 不在有效期内就抛出异常
            return;
        }


        Api api = apiInvokeApply.getApi();
        // 处理Param和Query
        HttpUrl requestUrl = handleParamAndQuery(api.getProtocol(), api.getUrl(), param, query);
        // 处理Header
        Request.Builder requestBuilder = handleRequestHeader(requestUrl, header);
        // 处理RequestBody
        RequestBody requestBody = handleRequestBody(body);
        // 处理调用
        Response response = handleMethodAndExcute(api.getMethod(), requestBuilder, requestBody);

        handleReponse(applyId, api, param, query, header, body, response);

    }

    /**
     * @param protocol
     * @param url
     * @param param
     * @param query
     * @return
     */
    private HttpUrl handleParamAndQuery(InternetProtocol protocol, String url, List<ApiParamItem> param, List<ApiQueryItem> query) {
        // 拼接协议和url
        String baseUrl = protocol.toString().toLowerCase() + "://" + url;

        // 处理param
        for (ApiParamItem item : param) {
            baseUrl = baseUrl.replaceAll("\\{" + item.getKey() + "\\}", item.getValue());
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();

        // 处理query
        for (ApiQueryItem item : query) {
            urlBuilder.addQueryParameter(item.getKey(), item.getValue());
        }

        HttpUrl requestUrl = urlBuilder.build();

        return requestUrl;
    }

    /**
     * @param url
     * @param header
     * @return
     */
    private Request.Builder handleRequestHeader(HttpUrl url, List<ApiHeaderItem> header) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        for (ApiHeaderItem item : header) {
            requestBuilder.addHeader(item.getKey(), item.getValue());
        }

        return requestBuilder;
    }

    /**
     * @param body
     * @return
     * @throws IOException
     */
    private RequestBody handleRequestBody(ApiRequestBody body) throws IOException {
        RequestBody requestBody = null;

        switch (body.getType()) {
            case NONE:
                break;
            case FORM_DATA: {
                MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder();
                for (ApiFormDataItem item : body.getFormDataBody()) {
                    if (item.getType().equals("File")) {    // 单独处理文件
                        String path = item.getValue();
                        byte[] bytes = minioService.get(path).readAllBytes();
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
        return requestBody;
    }

    private Response handleMethodAndExcute(HttpMethod method, Request.Builder requestBuilder, RequestBody requestBody) throws IOException {
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

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response;
    }

    /**
     * 处理响应
     *
     * @param applyId
     * @param param
     * @param query
     * @param header
     * @param body
     * @param response
     * @throws IOException
     */
    private void handleReponse(String applyId, Api api, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiRequestBody body, Response response) throws IOException {
        // 创建调用日志
        ApiInvokeLog apiInvokeLog = new ApiInvokeLog();
        apiInvokeLog.setLogId(UUID.randomUUID().toString().replaceAll("-", ""));
        apiInvokeLog.setApplyId(applyId);
        apiInvokeLog.setInvokeUserId(AuthUtil.getUser().getId());

        if (response.isSuccessful()) {
            apiInvokeLog.setResult(ApiInvokeResult.SUCCESS);
            apiInvokeLog.setParam(param);
            apiInvokeLog.setQuery(query);
            apiInvokeLog.setRequestHeader(header);
            if (body.getFormDataBody() != null) {
                body.getFormDataBody().forEach(item -> {
                    if (item.getType().equals("File")) {
                        String oldPath = item.getValue();
                        String newPath = "api/invoke/log/" + apiInvokeLog.getLogId() + "/" + oldPath.substring(oldPath.lastIndexOf("/") + 1);
                        minioService.copy(oldPath, newPath);
                        item.setValue(newPath);
                    }
                });
            }
            if (body.getBinaryBody() != null) {
                String oldPath = body.getBinaryBody();
                String newPath = "api/invoke/log/" + apiInvokeLog.getLogId() + "/" + oldPath.substring(oldPath.lastIndexOf("/") + 1);
                minioService.copy(oldPath, newPath);
                body.setBinaryBody(newPath);
            }
            apiInvokeLog.setRequestBody(body);

            List<ApiHeaderItem> responseHeader = api.getResponseHeader();
            // 遍历响应标头
            response.headers().forEach(item -> {
                ApiHeaderItem headerItem = responseHeader.stream().filter(h -> h.getKey().equals(item.getFirst())).findFirst().orElse(null);
                if (headerItem == null) {
                    responseHeader.add(new ApiHeaderItem(item.getFirst(), item.getSecond(), "String", false, ""));
                } else {
                    headerItem.setValue(item.getSecond());
                }
            });
            apiInvokeLog.setResponseHeader(responseHeader);

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
                    String path = minioService.upload(is, "api/invoke/log/" + apiInvokeLog.getLogId() + "/");
                    responseBody.setBinaryBody(path);
                    break;
                }
                case GRAPHQL: {
                    responseBody.setGraphQLBody(response.body().string());
                    break;
                }
            }

            apiInvokeLog.setResponseBody(responseBody);
            logger.info(JSON.toJSONString(apiInvokeLog, JSONWriter.Feature.PrettyFormat));

        } else {
            apiInvokeLog.setResult(ApiInvokeResult.FAILED);
        }

        apiInvokeLogMapper.insert(apiInvokeLog);
    }
}
