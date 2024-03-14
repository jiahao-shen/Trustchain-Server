package com.trustchain.model.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.trustchain.model.enums.StatusCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
@NoArgsConstructor
public class StatusCodeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fileName, Type type, int features) throws IOException {
        serializer.write(StatusCode.valueOf(object.toString()).getCode());
    }
}
