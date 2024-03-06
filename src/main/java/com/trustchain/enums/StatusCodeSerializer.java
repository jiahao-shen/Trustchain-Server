package com.trustchain.enums;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
