package com.trustchain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionStatisticsItemDTO {
    Double income;
    Double expense;
    Double balance;
}
