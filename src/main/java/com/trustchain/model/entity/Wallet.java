package com.trustchain.model.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.WalletState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//@Data(staticConstructor = "create")
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//@Table(value = "wallet")
@Table("tb_account")
@Accessors(chain = true)
@Data(staticConstructor = "create")
public class Wallet extends Model<Wallet> {
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
