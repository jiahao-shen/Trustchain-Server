package com.hawkeye.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionStatisticsItemVO {
    Double income;
    Double expense;
    Double balance;
}
