package com.hawkeye.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionStatisticsItemDTO {
    Double income;
    Double expense;
    Double balance;
}
