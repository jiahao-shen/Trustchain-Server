package com.trustchain.model.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.trustchain.model.enums.WalletState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
public class WalletVO {
    private String id;  // 钱包ID

    private String userId;  // 用户ID

    private UserVO user;  // 钱包

    private List<TransactionVO> transactions;

    private Double balance = 0.0; // 钱包余额

    private WalletState state = WalletState.ENABLE;  // 钱包状态

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;  // 注册时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;  // 最后修改时间

}
