package com.hawkeye.model.entity;

import com.hawkeye.model.enums.HttpBodyRawType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRawBody {
    private HttpBodyRawType type;
    private String body;
}
