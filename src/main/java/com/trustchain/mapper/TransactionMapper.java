package com.trustchain.mapper;

import com.mybatisflex.core.BaseMapper;
import com.trustchain.model.entity.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMapper extends BaseMapper<Transaction> {
}
