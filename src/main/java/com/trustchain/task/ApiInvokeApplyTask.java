package com.trustchain.task;

import com.mybatisflex.core.query.QueryWrapper;
import com.trustchain.mapper.ApiInvokeApplyMapper;
import com.trustchain.model.entity.ApiInvokeApply;
import com.trustchain.model.enums.ApiInvokeStatus;
import com.trustchain.model.enums.ApplyStatus;
import com.trustchain.service.impl.ApiServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.trustchain.model.entity.table.ApiInvokeApplyTableDef.API_INVOKE_APPLY;

@Component
public class ApiInvokeApplyTask {
    @Autowired
    ApiInvokeApplyMapper apiInvokeApplyMapper;
    private static final Logger logger = LogManager.getLogger(ApiInvokeApplyTask.class);

    @Scheduled(cron = "0 1 * * * *")
    void updateInvokeStatus() { // 整点运行
        // 筛选出所有申请状态为ALLOW, 且调用状态为PENDING, 且当前时间介于有效期内的请求
        QueryWrapper query1 = QueryWrapper.create()
                .from(API_INVOKE_APPLY)
                .where(API_INVOKE_APPLY.APPLY_STATUS.eq(ApplyStatus.ALLOW))
                .and(API_INVOKE_APPLY.INVOKE_STATUS.eq(ApiInvokeStatus.PENDING))
                .and(API_INVOKE_APPLY.START_TIME.le(new Date()))
                .and(API_INVOKE_APPLY.END_TIME.ge(new Date()));

        logger.info("Pending Applys");

        List<ApiInvokeApply> pendingApplys = apiInvokeApplyMapper.selectListByQuery(query1);
        pendingApplys.forEach(item -> {
            // 将调用状态更新为VALID
            item.setInvokeStatus(ApiInvokeStatus.VALID);
            apiInvokeApplyMapper.update(item);
        });

        // 筛选出所有调用状态为VALID, 且当前时间超出有效期内的请求
        QueryWrapper query2 = QueryWrapper.create()
                .from(API_INVOKE_APPLY)
                .where(API_INVOKE_APPLY.INVOKE_STATUS.eq(ApiInvokeStatus.VALID))
                .and(API_INVOKE_APPLY.START_TIME.gt(new Date()).or(API_INVOKE_APPLY.END_TIME.lt(new Date())));

        logger.info("Valid Applys");

        List<ApiInvokeApply> validApplys = apiInvokeApplyMapper.selectListByQuery(query2);
        validApplys.forEach(item -> {
            if (item.getStartTime().after(new Date())) {
                // 未到开始时间的调用状态更新为PENDING
                item.setInvokeStatus(ApiInvokeStatus.PENDING);
            } else if (item.getEndTime().before(new Date())) {
                // 已过结束时间的调用状态更新为EXPIRED
                item.setInvokeStatus(ApiInvokeStatus.EXPIRED);
            }
            apiInvokeApplyMapper.update(item);
        });
    }
}
