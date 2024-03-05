package com.trustchain.model.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginVO {
    private UserInformationVO user;
    private SaTokenInfo token;
}
