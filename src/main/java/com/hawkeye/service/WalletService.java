package com.hawkeye.service;

import com.mybatisflex.core.paginate.Page;
import com.hawkeye.model.dto.TransactionStatisticsItemDTO;
import com.hawkeye.model.entity.Transaction;
import com.hawkeye.model.entity.Wallet;
import com.hawkeye.model.enums.DateRange;
import com.hawkeye.model.enums.TransactionChannel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface WalletService {

    /**
     * @param userId 用户ID
     */
    Wallet create(String userId);

    /**
     * 用户钱包
     *
     * @param userId 用户ID
     * @return
     */
    Wallet detail(String userId);

    /**
     * 充值
     *
     * @param userId  用户ID
     * @param amount  充值金额
     * @param channel 充值渠道
     * @return
     */
    void topUp(String userId, Double amount, TransactionChannel channel);

    /**
     * 提现
     *
     * @param userId  用户ID
     * @param amount  提现金额
     * @param channel 提现渠道
     * @return
     */
    void withdraw(String userId, Double amount, TransactionChannel channel);

    @Transactional
    void sale(String userId, Double amount, TransactionChannel channel, String txId);

    @Transactional
    void purchase(String userId, Double amount, TransactionChannel channel, String txId);

    /**
     * 查看交易列表
     *
     * @param userId     用户ID
     * @param pageNumber 页数
     * @param pageSize   页大小
     * @param filter     过滤
     * @param sort       排序
     * @return
     */
    Page<Transaction> transactionList(String userId,
                                      Integer pageNumber,
                                      Integer pageSize,
                                      Map<String, List<String>> filter,
                                      Map<String, String> sort);

    /**
     * 查看交易详情
     *
     * @param id 交易ID
     * @return
     */
    Transaction transactionDetail(String id);


    /**
     * 交易统计
     *
     * @param userId 用户ID
     * @param range  统计范围
     */
    Map<String, TransactionStatisticsItemDTO> transactionStatistics(String userId, DateRange range);
}
