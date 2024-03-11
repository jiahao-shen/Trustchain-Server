package com.trustchain.util;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.trustchain.service.MinioService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@NoArgsConstructor
public class MinioURLSerializer implements ObjectSerializer {
    private static MinioService minioService;

    @Autowired
    public MinioURLSerializer(MinioService service) {
        MinioURLSerializer.minioService = service;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type type, int features) {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        serializer.write(minioService.presignedUrl(object.toString()));
    }
}
