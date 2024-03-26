package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.TransactionChannel;
import com.trustchain.model.enums.TransactionMethod;
import com.trustchain.model.enums.BalanceType;
import lombok.Data;

import java.util.Date;

@Data
@Table(value = "transaction")
public class Transaction {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    @ColumnAlias("transaction_id")
    private String id;  // 交易日志ID

    @Column("wallet_id")
    @ColumnAlias("transaction_wallet_id")
    private String walletId;    // 钱包ID

    @Column("tx_id")
    @ColumnAlias("transaction_tx_id")
    private String txId;   // 交易ID(对应支付宝, 微信支付, API调用的流水号)

    @Column("amount")
    @ColumnAlias("transaction_amount")
    private Double amount;  // 交易金额

    @Column("balance")
    @ColumnAlias("transaction_balance")
    private Double balance; // 交易后的余额

    @Column("balance_type")
    @ColumnAlias("transaction_balance_type")
    private BalanceType balanceType;   // 收支类型

    @Column("method")
    @ColumnAlias("transaction_method")
    private TransactionMethod method;   // 交易方式

    @Column("channel")
    @ColumnAlias("transaction_channel")
    private TransactionChannel channel;    // 交易渠道

    @Column("comment")
    @ColumnAlias("transaction_comment")
    private String comment; // 交易备注

    @Column(value = "time", onInsertValue = "now()")
    @ColumnAlias("transaction_time")
    private Date time;

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("transaction_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}
