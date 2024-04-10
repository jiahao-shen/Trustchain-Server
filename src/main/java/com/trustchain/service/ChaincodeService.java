package com.trustchain.service;

import com.alibaba.fastjson.JSONObject;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChaincodeService {
    @Autowired
    private ChainClient chainClient;

    private static final Map<String, byte[]> SAVE_PARAMS = new HashMap<String, byte[]>() {{
        put("file_name", "name007".getBytes());
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
        put("time", "6543234".getBytes());
    }};
    private static final Map<String, byte[]> QUERY_PARAMS = new HashMap<String, byte[]>() {{
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
    }};

    //合约名得核实
    private static final String CONTRACT_NAME = "fact";
    public ResultOuterClass.TxResponse invokeContractSave(String fileName, String fileHash, String time) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {

            //contract name
            String INVOKE_CONTRACT_METHOD_SAVE = "save";
            Map<String, byte[]> saveParams = new HashMap<>();
            saveParams.put("file_name", fileName.getBytes());
            saveParams.put("file_hash", fileHash.getBytes());
            saveParams.put("time", time.getBytes());
            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD_SAVE,
                    null, saveParams,10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseInfo;
    }

    //save the info to chain
    public ResultOuterClass.ContractResult invokeContractUpload(String key, String field, JSONObject jsonObject){
        ResultOuterClass.TxResponse responseInfo = null;
        String json = JSONObject.toJSONString(jsonObject);
        try{
            String INVOKE_CONTRACT_METHOD = "put_state";
            Map<String, byte[]> params = new HashMap<>();
            params.put("key", key.getBytes());
            params.put("field",field.getBytes());
            params.put("value", json.getBytes());
            responseInfo = chainClient.invokeContract("testOrg1", INVOKE_CONTRACT_METHOD, null, params, 10000, 10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseInfo.getContractResult();
    }

    //get the history list
    public ResultOuterClass.ContractResult getKeyHistory(String key, String field){
        ResultOuterClass.TxResponse responseInfo = null;
        ResultOuterClass.ContractResult contractResult = null;
        try{
            String INVOKE_CONTRACT_METHOD = "get_key_history";
            Map<String, byte[]> params = new HashMap<>();
            params.put("key", key.getBytes());
            params.put("field",field.getBytes());
            responseInfo = chainClient.invokeContract("testOrg1", INVOKE_CONTRACT_METHOD, null,
                    params, 10000, 10000);
            contractResult = responseInfo.getContractResult();
        }catch (Exception e){
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
                        null, params,10000, 10000);
                System.out.println(responseInfo);
                contractRes = responseInfo.getContractResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contractRes;
    }


    public ResultOuterClass.TxResponse invokeContractFind(String fileHash) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            String INVOKE_CONTRACT_METHOD_FIND = "findByFileHash";
            Map<String, byte[]> queryParams = new HashMap<>();
            queryParams.put("file_hash", fileHash.getBytes());
            responseInfo = chainClient.invokeContract(CONTRACT_NAME, INVOKE_CONTRACT_METHOD_FIND,
                    null, queryParams,10000, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseInfo;
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

    public ContractOuterClass.Contract[] getContractList() {
        ContractOuterClass.Contract[] contractConfig = null;
        try {
            contractConfig = chainClient.getContractList(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractConfig;
    }

    public ContractOuterClass.Contract getContractByName(String name) {
        ContractOuterClass.Contract contractConfig = null;
        try {
            contractConfig = chainClient.getContractInfo("fact", 20000);
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
                    null, params,10000, 10000);
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
                    null, params,10000, 10000);
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
                    null, params,10000, 10000);
            contractRes = responseInfo.getContractResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contractRes;
    }
}
