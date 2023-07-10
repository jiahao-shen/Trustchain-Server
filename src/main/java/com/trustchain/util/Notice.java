package com.trustchain.util;

import lombok.Data;

import java.util.List;

@Data
public class Notice {

    private int status;
    private Object msg;
    private List<DataBean> data;

}
