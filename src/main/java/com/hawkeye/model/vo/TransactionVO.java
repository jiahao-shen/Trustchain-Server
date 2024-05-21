package com.hawkeye.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.hawkeye.model.enums.TransactionChannel;
import com.hawkeye.model.enums.TransactionMethod;
import com.hawkeye.model.enums.BalanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVO {
    private String id;  // 交易日志ID

    private String walletId;    // 钱包ID

    private String txId;   // 交易ID, 对应支付宝, 微信支付, API调用的流水号

    private Double amount;  // 交易金额

    private Double balance; // 交易后的余额

    private BalanceType balanceType;   // 交易类型

    private TransactionMethod method;   // 交易方式

    private TransactionChannel channel;    // 交易渠道

    private String comment; // 交易备注

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
