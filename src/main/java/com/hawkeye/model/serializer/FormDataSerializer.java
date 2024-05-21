package com.hawkeye.model.serializer;

import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.hawkeye.model.vo.ApiFormDataItemVO;
import com.hawkeye.service.MinioService;
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
            List<ApiFormDataItemVO> forms = (List<ApiFormDataItemVO>) object;
            forms.forEach(item -> {
                if (item.getType().equals("File")) {
                    item.setValue(minioService.presignedUrl(item.getValue()));
                }
            });
            writer.write(forms);
        }
    }
}
