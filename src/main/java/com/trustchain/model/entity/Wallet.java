package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.WalletState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "wallet")
public class Wallet {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    private String id;  // 钱包ID

    @Column("user_id")
    private String userId;  // 用户ID

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;  // 钱包

    @Column("balance")
    private Double balance; // 钱包余额

    @Column("state")
    private WalletState state;  // 钱包状态

    @Column(value = "is_delete", isLogicDelete = true)
    private Boolean isDelete;   // 逻辑删除标志位
}
