package com.trustchain.model.serializer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.trustchain.model.enums.StatusCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@NoArgsConstructor
public class StatusCodeSerializer implements ObjectWriter {
    @Override
    public void write(JSONWriter writer, Object object, Object fieldName, Type fieldType, long features) {
        writer.writeInt32(StatusCode.valueOf(object.toString()).getCode());
    }
}
