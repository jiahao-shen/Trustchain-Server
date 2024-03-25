package com.trustchain.model.serializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.trustchain.model.vo.ApiFormDataItemVO;
import com.trustchain.service.MinioService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
@NoArgsConstructor
public class FormDataSerializer implements ObjectWriter {
    private static MinioService minioService;

    @Autowired
    public FormDataSerializer(MinioService service) {
        FormDataSerializer.minioService = service;
    }

    @Override
    public void write(JSONWriter writer, Object object, Object fieldName, Type fieldType, long features) {
        if (object == null) {
            writer.writeNull();
        } else {
            List<ApiFormDataItemVO> forms = JSON.parseObject(object.toString(), new TypeReference<>() {
            });
            forms.forEach(item -> {
                if (item.getType().equals("File")) {
                    item.setValue(minioService.presignedUrl(item.getValue()));
                }
            });
            writer.writeString(JSON.toJSONString(forms));
        }
    }
}
