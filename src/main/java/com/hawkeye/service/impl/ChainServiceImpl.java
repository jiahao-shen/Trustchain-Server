package com.hawkeye.service.impl;

import com.hawkeye.service.ChainService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ChainServiceImpl implements ChainService {
    private final String CONTRACT_NAME = "testOrg1";
    private final long RPC_CALL_TIMEOUT = 10000;
    private final long SYNC_RESULT_TIMEOUT = 10000;

//    @Autowired
//    private ChainClient chainClient;

    public String putState(String key, String field, String value) {
//        return putState(key, field, value, null);
        return null;
    }

    @SneakyThrows
    public String putState(String key, String field, String value, String txId) {
//        ResultOuterClass.TxResponse txRes;
//
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        params.put("field", field.getBytes());
//        params.put("value", value.getBytes());
//
//        txRes = chainClient.invokeContract(CONTRACT_NAME, "put_state", txId, params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);
//        return txRes.getTxId();
        return null;
    }

    @SneakyThrows
    public String getState(String key, String field) {
//        ResultOuterClass.TxResponse txRes;
//
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        params.put("field", field.getBytes());
//
//        txRes = chainClient.invokeContract(CONTRACT_NAME, "get_state", null, params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);
//
//        return txRes.getContractResult().getResult().toStringUtf8();
        return null;
    }

    @SneakyThrows
    public String getState(String txId) {
//        ChainmakerTransaction.TransactionInfoWithRWSet transactionInfoWithRWSet;
//        transactionInfoWithRWSet = chainClient.getTxWithRWSetByTxId(txId, RPC_CALL_TIMEOUT);
//        return transactionInfoWithRWSet.getRwSet().getTxWrites(0).getValue().toStringUtf8();
        return null;
    }

    @SneakyThrows
    public String getHistory(String key, String field) {
//        ResultOuterClass.TxResponse txRes;
//
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        params.put("field", field.getBytes());
//        txRes = chainClient.invokeContract(CONTRACT_NAME, "get_key_history", null,
//                params, RPC_CALL_TIMEOUT, SYNC_RESULT_TIMEOUT);
//
//        return txRes.getContractResult().getResult().toStringUtf8();
        return null;
    }


//    public ResultOuterClass.ContractResult putState(String key, String field, JSONObject jsonObject, String txId) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        String json = JSONObject.toJSONString(jsonObject);
//        try {
//            String INVOKE_CONTRACT_METHOD = "put_state";
//            Map<String, byte[]> params = new HashMap<>();
//            params.put("key", key.getBytes());
//            params.put("field", field.getBytes());
//            params.put("value", json.getBytes());
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD, txId, params, 10000, 10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return responseInfo.getContractResult();
//    }
//
//    //get the history list
//    public ResultOuterClass.ContractResult getKeyHistory(String key, String field) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        ResultOuterClass.ContractResult contractResult = null;
//        try {
//            String INVOKE_CONTRACT_METHOD = "get_key_history";
//            Map<String, byte[]> params = new HashMap<>();
//            params.put("key", key.getBytes());
//            params.put("field", field.getBytes());
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD, null,
//                    params, 10000, 10000);
//            contractResult = responseInfo.getContractResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contractResult;
//    }
//
//    //get the special version
//    public ChainmakerTransaction.TransactionInfoWithRWSet getTxByTxId(String TxId) {
//        ChainmakerTransaction.TransactionInfoWithRWSet transactionInfoWithRWSet = null;
//        try {
//            transactionInfoWithRWSet = chainClient.getTxWithRWSetByTxId(TxId, 20000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return transactionInfoWithRWSet;
//    }
//
//    //get the current version(the newest one)
//    public String getNewVersion(String key, String field) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        ResultOuterClass.ContractResult contractRes = null;
//        String ret = null;
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        params.put("field", field.getBytes());
//        try {
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, "get_state",
//                    null, params, 10000, 10000);
////            System.out.println(responseInfo);
//            contractRes = responseInfo.getContractResult();
//            ret = contractRes.getResult().toStringUtf8();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ret;
//    }
//
//    public ChainConfigOuterClass.ChainConfig getChainConfig() {
//        ChainConfigOuterClass.ChainConfig chainConfig = null;
//        try {
//            chainConfig = chainClient.getChainConfig(20000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return chainConfig;
//    }
//
//    public ContractOuterClass.Contract getContractByName(String name) {
//        ContractOuterClass.Contract contractConfig = null;
//        try {
//            contractConfig = chainClient.getContractInfo(name, 20000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contractConfig;
//    }
//
//    public ResultOuterClass.ContractResult putState(String key, String value) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        ResultOuterClass.ContractResult contractRes = null;
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        params.put("value", value.getBytes());
//        try {
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, "put_state",
//                    null, params, 10000, 10000);
//            System.out.println(responseInfo);
//            contractRes = responseInfo.getContractResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contractRes;
//    }
//
//    public ResultOuterClass.ContractResult getState(String key) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        ResultOuterClass.ContractResult contractRes = null;
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        try {
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, "get_state",
//                    null, params, 10000, 10000);
//            System.out.println(responseInfo);
//            contractRes = responseInfo.getContractResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contractRes;
//    }
//
//    public ResultOuterClass.ContractResult getHistoryByKey(String key) {
//        ResultOuterClass.TxResponse responseInfo = null;
//        ResultOuterClass.ContractResult contractRes = null;
//        Map<String, byte[]> params = new HashMap<>();
//        params.put("key", key.getBytes());
//        try {
//            responseInfo = chainClient.invokeContract(CONTRACT_NAME, "get_key_history",
//                    null, params, 10000, 10000);
//            contractRes = responseInfo.getContractResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contractRes;
//    }
}
