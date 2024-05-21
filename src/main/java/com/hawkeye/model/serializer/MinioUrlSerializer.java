package com.hawkeye.model.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.hawkeye.service.MinioService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@NoArgsConstructor
public class MinioUrlSerializer implements ObjectWriter {
    private static MinioService minioService;

    @Autowired
    public MinioUrlSerializer(MinioService service) {
        MinioUrlSerializer.minioService = service;
    }

    @Override
    public void write(JSONWriter writer, Object object, Object fieldName, Type fieldType, long features) {
        if (object == null) {
            writer.writeNull();
        } else {
            writer.writeString(minioService.presignedUrl(object.toString()));
        }
    }
}
