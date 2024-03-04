package com.trustchain.VO;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
}
