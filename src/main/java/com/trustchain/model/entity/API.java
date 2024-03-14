package com.trustchain.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.trustchain.model.enums.BodyType;
import com.trustchain.model.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("api")
public class API {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    @Column("id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @Column("name")
    private String name;

    @Column("author")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long author;

    @Column("organization")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long organization;

    @Column("url")
    private String url;

    @Column("method")
    private HttpMethod method;

    @Column("introduction")
    private String introducation;

    @Column("category")
    private String category;

    @Column("authorize")
    private String authorize;

    @Column("version")
    private String version;

    @Column("header_type")
    private BodyType headerType;

    @Column("header")
    private String header;

    @Column("request_type")
    private BodyType requestType;

    @Column("request")
    private String request;

    @Column("response_type")
    private BodyType responseType;

    @Column("response")
    private String response;

    @Column("created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;   // 创建时间
}
