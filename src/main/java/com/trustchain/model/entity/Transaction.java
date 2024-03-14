package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.TransactionMethod;
import com.trustchain.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "transaction")
public class Transaction {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("log_id")
    private String logId;  // 交易日志ID

    @Column("wallet_id")
    private String walletId;    // 钱包ID

    @RelationOneToOne(selfField = "walletId", targetField = "id")
    private Wallet wallet;  // 钱包

    @Column("txId")
    private String txId;   // 交易ID(对应支付宝, 微信支付, API调用的流水号)

    @Column("amount")
    private double amount;  // 交易金额

    @Column("balance")
    private double balance; // 交易后的余额

    @Column("交易备注")
    private String comment; // 交易备注

    @Column("type")
    private TransactionType type;   // 交易类型

    @Column("method")
    private TransactionMethod method;   // 交易方式

    @Column(value = "time", onInsertValue = "now()")
    private Date time;

    @Column(value = "is_delete", isLogicDelete = true)
    private Boolean isDelete;   // 逻辑删除标志位
}
