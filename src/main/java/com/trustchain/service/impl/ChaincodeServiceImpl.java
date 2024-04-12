package com.trustchain.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.service.ChaincodeService;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChaincodeServiceImpl implements ChaincodeService {

    @Autowired
    private ChainClient chainClient;

    //save the info to chain
    public ResultOuterClass.ContractResult invokeContractUpload(String key, String field, JSONObject jsonObject) {
        ResultOuterClass.TxResponse responseInfo = null;
        String json = JSONObject.toJSONString(jsonObject);
        try {
            String INVOKE_CONTRACT_METHOD = "put_state";
            Map<String, byte[]> params = new HashMap<>();
            params.put("key", key.getBytes());
            params.put("field", field.getBytes());
            params.put("value", json.getBytes());
            responseInfo = chainClient.invokeContract("testOrg1", INVOKE_CONTRACT_METHOD, null, params, 10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseInfo.getContractResult();
    }

    //get the history list
    public ResultOuterClass.ContractResult getKeyHistory(String key, String field) {
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractResult = null;
        try {
            String INVOKE_CONTRACT_METHOD = "get_key_history";
            Map<String, byte[]> params = new HashMap<>();
            params.put("key", key.getBytes());
            params.put("field", field.getBytes());
            responseInfo = chainClient.invokeContract("testOrg1", INVOKE_CONTRACT_METHOD, null,
                    params, 10000, 10000);
            contractResult = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractResult;
    }

    //get the special version
    public ChainmakerTransaction.TransactionInfo getTxByTxId(String TxId) {
        ChainmakerTransaction.TransactionInfo transactionInfo = null;
        ResultOuterClass.ContractResult contractResult = null;
        try {
            transactionInfo = chainClient.getTxByTxId(TxId, 20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionInfo;
    }

    //get the current version(the newest one)
    public ResultOuterClass.ContractResult getNewVersion(String key) {
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractRes = null;
        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        try {
            responseInfo = chainClient.invokeContract("testOrg1", "get_state",
                    null, params, 10000, 10000);
            System.out.println(responseInfo);
            contractRes = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractRes;
    }

    public ChainConfigOuterClass.ChainConfig getChainConfig() {
        ChainConfigOuterClass.ChainConfig chainConfig = null;
        try {
            chainConfig = chainClient.getChainConfig(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chainConfig;
    }

    public ContractOuterClass.Contract getContractByName(String name) {
        ContractOuterClass.Contract contractConfig = null;
        try {
            contractConfig = chainClient.getContractInfo(name, 20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractConfig;
    }

    public ResultOuterClass.ContractResult putState(String key, String value) {
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractRes = null;
        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        params.put("value", value.getBytes());
        try {
            responseInfo = chainClient.invokeContract("testOrg1", "put_state",
                    null, params, 10000, 10000);
            System.out.println(responseInfo);
            contractRes = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractRes;
    }

    public ResultOuterClass.ContractResult getState(String key) {
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractRes = null;
        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        try {
            responseInfo = chainClient.invokeContract("testOrg1", "get_state",
                    null, params, 10000, 10000);
            System.out.println(responseInfo);
            contractRes = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractRes;
    }

    public ResultOuterClass.ContractResult getHistoryByKey(String key) {
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractRes = null;
        Map<String, byte[]> params = new HashMap<>();
        params.put("key", key.getBytes());
        try {
            responseInfo = chainClient.invokeContract("testOrg1", "get_key_history",
                    null, params, 10000, 10000);
            contractRes = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractRes;
    }
}
