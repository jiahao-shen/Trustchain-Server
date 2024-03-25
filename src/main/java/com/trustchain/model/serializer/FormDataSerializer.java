package com.trustchain.model.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.trustchain.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

public class FormDataSerializer implements ObjectWriter {
    private static MinioService minioService;

    @Autowired
    public FormDataSerializer(MinioService service) { FormDataSerializer.minioService = service;}

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {

    }
}
