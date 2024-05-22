package com.hawkeye.controller;

import com.alibaba.fastjson2.JSONObject;
import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.vo.BaseResponse;
import com.hawkeye.service.ChainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.chainmaker.sdk.utils.FileUtils;
import org.chainmaker.sdk.utils.SdkUtils;
import org.chainmaker.sdk.utils.UtilsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;

@RestController
@RequestMapping("/chain")
public class ChainController {
    @Autowired
    private ChainService chainService;
    @Autowired
    private ChainClient chainClient;

    private static final Logger logger = LogManager.getLogger(ChainController.class);

    @PostMapping("/putState")
    @ResponseBody
    public BaseResponse<String> putState(@RequestBody JSONObject request) {
        String key = request.getString("key");
        String field = request.getString("field");
        String value = request.getString("value");
        String txId;
        if (request.containsKey("txId")) {
            txId = request.getString("txId");
            txId = chainService.putState(key, field, value, txId);
        } else {
            txId = chainService.putState(key, field, value);
        }

        return new BaseResponse(StatusCode.SUCCESS, "", txId);
    }

    @PostMapping("/getState")
    @ResponseBody
    public BaseResponse<String> getState(@RequestBody JSONObject request) {
        String result;
        if (request.containsKey("key") && request.containsKey("field")) {
            String key = request.getString("key");
            String field = request.getString("field");
            result = chainService.getState(key, field);
        } else {
            String txId = request.getString("txId");
            result = chainService.getState(txId);
        }

        return new BaseResponse(StatusCode.SUCCESS, "", result);
    }

    @PostMapping("/getHistory")
    @ResponseBody
    public BaseResponse<String> getHistory(@RequestBody JSONObject request) {
        String key = request.getString("key");
        String field = request.getString("field");

        String result = chainService.getHistory(key, field);
        return new BaseResponse(StatusCode.SUCCESS, "", result);
    }

    @GetMapping("/loadContract/{contractName}")
    @ResponseBody
    public BaseResponse<String> loadContract(@PathVariable String contractName) throws UtilsException, ChainMakerCryptoSuiteException, ChainClientException {
        User user = new User("test", FileUtils.getFileBytes("/root/release/crypto-config/test/user/testAdmin/testAdmin.sign.key"),
                FileUtils.getFileBytes("/root/release/crypto-config/test/user/testAdmin/testAdmin.sign.crt"),
                FileUtils.getFileBytes("/root/release/crypto-config/test/user/testAdmin/testAdmin.tls.key"),
                FileUtils.getFileBytes("/root/release/crypto-config/test/user/testAdmin/testAdmin.tls.crt"), false);
        byte[] bytes = FileUtils.getFileBytes("/root/release/testOrg.7z");
        Request.Payload payload = chainClient.createContractCreatePayload(contractName, "1.0", bytes, ContractOuterClass.RuntimeType.DOCKER_GO, null);
        Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(payload, new User[]{user});
        ResultOuterClass.TxResponse responseInfo = chainClient.sendContractManageRequest(payload, endorsementEntries, 10000, 10000);
        return new BaseResponse(StatusCode.SUCCESS, "", responseInfo.getContractResult());
    }
}
