package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.WalletState;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Table(value = "wallet")
public class Wallet {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    @ColumnAlias("wallet_id")
    private String id;  // 钱包ID

    @Column("user_id")
    @ColumnAlias("wallet_user_id")
    private String userId;  // 用户ID

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;  // 钱包

    @RelationOneToMany(selfField = "id", targetField = "walletId")
    private List<Transaction> transactions;

    @Column("balance")
    @ColumnAlias("wallet_balance")
    private Double balance = 0.0; // 钱包余额

    @Column("state")
    @ColumnAlias("wallet_state")
    private WalletState state = WalletState.ENABLE;  // 钱包状态

    @Column(value = "registration_time", onInsertValue = "now()")
    @ColumnAlias("wallet_registration_time")
    private Date registrationTime;  // 注册时间

    @Column(value = "last_modified", onInsertValue = "now()", onUpdateValue = "now()")
    @ColumnAlias("wallet_last_modified")
    private Date lastModified;  // 最后修改时间

    @Column(value = "is_delete", isLogicDelete = true)
    @ColumnAlias("wallet_is_delete")
    private Boolean isDelete;   // 逻辑删除标志位
}
