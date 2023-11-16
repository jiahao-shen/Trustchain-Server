package com.trustchain.service;

import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChaincodeService {
    @Autowired
    private ChainClient chainClient;
    /*
    private static final Map<String, byte[]> SAVE_PARAMS = new HashMap<String, byte[]>() {{
        put("file_name", "name007".getBytes());
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
        put("time", "6543234".getBytes());
    }};
    private static final Map<String, byte[]> QUERY_PARAMS = new HashMap<String, byte[]>() {{
        put("file_hash", "ab3456df5799b87c77e7f88".getBytes());
    }};
    */
    private static final String CONTRACT_NAME = "fact";
    public ResultOuterClass.TxResponse invokeContractSave(String fileName, String fileHash, String time) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
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
}
