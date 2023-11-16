package com.trustchain.model;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trustchain.enums.BodyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_invoke_log")
public class APIInvokeLog {

    @TableId(value = "serial_number", type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long serialNumber;

    @TableField("id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("applicant")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long applicant;

    @TableField("request_type")
    private BodyType requestType;

    @TableField("request")
    private String request;

    @TableField("response_type")
    private BodyType responseType;

    @TableField("response")
    private String response;

    @TableField("header_type")
    private BodyType headerType;

    @TableField("header")
    private String header;

    @TableField("invoke_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date invokeTime;

}
