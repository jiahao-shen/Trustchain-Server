package com.hawkeye.service.impl;

import com.hawkeye.service.ChainService;
import lombok.SneakyThrows;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChainServiceImpl implements ChainService {
    private final String CONTRACT_NAME = "testOrg1";
    private final long RPC_CALL_TIMEOUT = 10000;
    private final long SYNC_RESULT_TIMEOUT = 10000;

    @Autowired
    private ChainClient chainClient;

    public String putState(String key, String field, String value) {
        return putState(key, field, value, null);
    }

    @SneakyThrows
    public String putState(String key, String field, String value, String txId) {
        ResultOuterClass.TxResponse txRes;

        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        params.put("field", field.getBytes());
        params.put("value", value.getBytes());

        txRes = chainClient.invokeContract(CONTRACT_NAME, "put_state", txId, params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);
        return txRes.getTxId();
    }

    @SneakyThrows
    public String getState(String key, String field) {
        ResultOuterClass.TxResponse txRes;

        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        params.put("field", field.getBytes());

        txRes = chainClient.invokeContract(CONTRACT_NAME, "get_state", null, params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);

        return txRes.getContractResult().getResult().toStringUtf8();
    }

    @SneakyThrows
    public String getState(String txId) {
        ChainmakerTransaction.TransactionInfoWithRWSet transactionInfoWithRWSet;
        transactionInfoWithRWSet = chainClient.getTxWithRWSetByTxId(txId, RPC_CALL_TIMEOUT);
        return transactionInfoWithRWSet.getRwSet().getTxWrites(0).getValue().toStringUtf8();
    }

    @SneakyThrows
    public String getHistory(String key, String field) {
        ResultOuterClass.TxResponse txRes;

        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        params.put("field", field.getBytes());
        txRes = chainClient.invokeContract(CONTRACT_NAME, "get_key_history", null,
                params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);

        return txRes.getContractResult().getResult().toStringUtf8();
    }

}
