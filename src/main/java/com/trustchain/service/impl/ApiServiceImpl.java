package com.trustchain.service.impl;

import com.ibm.cloud.sdk.core.http.InputStreamRequestBody;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.exception.NoPermissionException;
import com.trustchain.mapper.ApiInvokeApplyMapper;
import com.trustchain.mapper.ApiMapper;
import com.trustchain.mapper.ApiRegisterMapper;
import com.trustchain.model.convert.ApiConvert;
import com.trustchain.model.dto.*;
import com.trustchain.model.entity.Api;
import com.trustchain.model.entity.ApiInvokeApply;
import com.trustchain.model.entity.ApiRegister;
import com.trustchain.model.entity.User;
import com.trustchain.model.enums.ApiInvokeStatus;
import com.trustchain.model.enums.ApiVisible;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.service.ApiService;
import com.trustchain.service.MinioService;
import com.trustchain.util.AuthUtil;
import okhttp3.*;
import okio.Buffer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
                .from(ApiRegister.class)
                .where(ApiRegister::getUserId).eq(userId);

        RelationManager.setMaxDepth(1);

        return apiRegMapper.selectListWithRelationsByQuery(query);
    }

    @Override
    public Page<ApiRegister> registerApplyList(String userId,
                                               Integer pageNumber,
                                               Integer pageSize,
                                               Map<String, List<String>> filter,
                                               Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .from(ApiRegister.class)
                .where(ApiRegister::getUserId).eq(userId);

        filter.forEach((key, value) -> {

        });

        sort.forEach((key, value) -> {

        });

        RelationManager.setMaxDepth(1);

        return apiRegMapper.paginateWithRelations(pageNumber, pageSize, query);
    }

    @Override
    public ApiRegister registerApplyDetail(String applyId) {
        return apiRegMapper.selectOneWithRelationsById(applyId);
    }

    @Override
    public List<ApiRegister> registerApprovalList(String orgId) {
        QueryWrapper query = QueryWrapper.create()
                .select(API_REGISTER.ALL_COLUMNS)
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
                .select(API_REGISTER.ALL_COLUMNS)
                .from(API_REGISTER)
                .leftJoin(USER)
                .on(USER.ID.eq(API_REGISTER.USER_ID)
                        .and(USER.ORGANIZATION_ID.eq(orgId)));

        filter.forEach((key, value) -> {

        });

        sort.forEach((key, value) -> {

        });

        RelationManager.setMaxDepth(1);

        Page<ApiRegister> apiRegs = apiRegMapper.paginateWithRelations(pageNumber, pageSize, query);

        return apiRegs;
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
                .from(Api.class)
                .where(Api::getUserId).eq(user.getId());

        return apiMapper.selectListByQuery(query);
    }

    @Override
    public Page<Api> myApiList(User user,
                               Integer pageNumber,
                               Integer pageSize,
                               Map<String, List<String>> filter,
                               Map<String, String> sort) {

        QueryWrapper query = QueryWrapper.create()
                .from(Api.class)
                .where(Api::getUserId).eq(user.getId());

        filter.forEach((key, value) -> {

        });

        sort.forEach((key, value) -> {

        });

        return apiMapper.paginate(pageNumber, pageSize, query);
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
                item.setUrl(null);
            }
        });
        return apis;
    }

    @Override
    public Page<Api> allApiList(User user,
                                Integer pageNumber,
                                Integer pageSize,
                                Map<String, List<String>> filter,
                                Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .from(API)
                .leftJoin(USER)
                .on(USER.ID.eq(API.USER_ID))
                .where(API.VISIBLE.eq(ApiVisible.PUBLIC))
                .or(API.VISIBLE.eq(ApiVisible.PRIVATE).and(USER.ORGANIZATION_ID.eq(user.getOrganizationId())));

        filter.forEach((key, value) -> {

        });

        sort.forEach(((key, value) -> {

        }));

        RelationManager.setMaxDepth(2);

        Page<Api> apis = apiMapper.paginateWithRelations(pageNumber, pageSize, query);

//        apis.forEach(item -> {
//            if (!item.getUserId().equals(user.getId())) {
//                item.setUrl(null);
//            }
//        });

        return apis;
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
    public Page<ApiInvokeApply> invokeApplyList(String userId,
                                                Integer pageNumber,
                                                Integer pageSize,
                                                Map<String, List<String>> filter,
                                                Map<String, String> sort) {
        QueryWrapper query = QueryWrapper.create()
                .from(ApiInvokeApply.class)
                .where(ApiInvokeApply::getUserId).eq(userId);

        filter.forEach((key, value) -> {

        });

        sort.forEach((key, value) -> {

        });

        RelationManager.setMaxDepth(1);

        Page<ApiInvokeApply> apiInvokeApplyList = apiInvokeApplyMapper.paginate(pageNumber, pageSize, query);

        apiInvokeApplyList.getRecords().forEach(item -> {
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
                .from(API_INVOKE_APPLY)
                .leftJoin(API).on(API_INVOKE_APPLY.API_ID.eq(API.ID))
                .and(API.USER_ID.eq(userId));

        filter.forEach((key, value) -> {

        });

        sort.forEach((key, value) -> {

        });

        RelationManager.setMaxDepth(2);

        Page<ApiInvokeApply> apiInvokeAppovalList = apiInvokeApplyMapper.paginateWithRelations(pageNumber, pageSize, query);

        apiInvokeAppovalList.getRecords().forEach(item -> {
            item.setAppKey(null);
            item.setSecretKey(null);
        });

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
    public void invokeWeb(String applyId, List<ApiParamItem> param, List<ApiQueryItem> query, List<ApiHeaderItem> header, ApiBody body) throws IOException {
        RelationManager.setMaxDepth(1);
        ApiInvokeApply apiInvokeApply = apiInvokeApplyMapper.selectOneWithRelationsById(applyId);

        if (apiInvokeApply.getInvokeStatus() != ApiInvokeStatus.VALID) {
            // TODO: 不在有效期内就抛出异常
            return;
        }

        Api api = apiInvokeApply.getApi();

        // 拼接协议和url
        String baseUrl = api.getProtocol().toString().toLowerCase() + "://" + api.getUrl();
        logger.info("initial url:" + baseUrl);

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
        logger.info(requestUrl.url());

        // 处理requestHeader
        Request.Builder requestBuilder = new Request.Builder().url(requestUrl);
        for (ApiHeaderItem item : header) {
            requestBuilder.addHeader(item.getKey(), item.getValue());
        }

        // TODO: 处理requestBody
        RequestBody requestBody = null;
        switch (api.getRequestBody().getType()) {
            case NONE:
                break;
            case FORM_DATA: {
                MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder();
                for (ApiFormDataItem item : body.getFormDataBody()) {
                    if (item.getType().equals("File")) {    // 单独处理文件
                        String path = item.getValue();
                        InputStream io = minioService.get(path);
                        byte[] fileArray = IOUtils.toByteArray(io);
                        MediaType mediaType = MediaType.parse(new Tika().detect(fileArray));
                        String fileName = path.substring(path.lastIndexOf("/") + 1);
                        formBodyBuilder.addFormDataPart(item.getKey(), fileName,
                                RequestBody.create(fileArray, mediaType));
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
                switch (api.getRequestBody().getRawBody().getType()) {
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
                        break;
                    }
                }
                requestBody = RequestBody.create(body.getRawBody().getBody(), mediaType);
                break;
            }
            case BINARY: {
                String path = body.getBinaryBody();
                InputStream io = minioService.get(path);
                byte[] fileArray = new byte[0];
                fileArray = IOUtils.toByteArray(io);
                MediaType mediaType = MediaType.parse(new Tika().detect(fileArray));
                requestBody = RequestBody.create(fileArray, mediaType);
                break;
            }
            case GRAPHQL:
                break;
        }

        Request request = null;
        switch (api.getMethod()) {
            case GET: {
                request = requestBuilder.get().build();
                break;
            }
            case POST: {
                request = requestBuilder.post(requestBody).build();
                break;
            }
            case PUT: {
                request = requestBuilder.put(null).build();
                break;
            }
            case DELETE: {
                request = requestBuilder.delete(null).build();
                break;
            }
            case HEAD: {
                request = requestBuilder.head().build();
                break;
            }
            case PATCH: {
                request = requestBuilder.patch(null).build();
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
        logger.info("request headers: " + request.headers());
        if (request.body() != null) {
            logger.info("request body: " + request.body().toString());
        }
        logger.info("response headers: " + response.headers());
        if (response.body() != null) {
            logger.info("response body: " + response.body().toString());
        }

    }

}
