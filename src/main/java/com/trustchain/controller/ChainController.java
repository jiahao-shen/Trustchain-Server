package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.service.ChainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chain")
public class ChainController {
    @Autowired
    private ChainService chainService;

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
    public BaseResponse<String> getHistory(@RequestBody JSONObject request) {
        String key = request.getString("key");
        String field = request.getString("field");

        String result = chainService.getHistory(key, field);
        return new BaseResponse(StatusCode.SUCCESS, "", result);
    }
    //save the org information to the chain
//    @PostMapping("/chaincode/uploadOrg")
//    public ResponseEntity<Object> uploadOrgToChain(@RequestBody JSONObject request){
//        ResultOuterClass.ContractResult responseInfo = null;
//        String orgId = request.getString("orgId");
//        String field = "organization";
////        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
////        queryWrapper.eq(Organization::getId,orgId);
////        Organization organization = organizationMapper.selectOne(queryWrapper);
////        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(organization);
//        JSONObject jsonObject = null;
//        try{
//            responseInfo = ChainService.invokeContractUpload(orgId, field, jsonObject);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chain error");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
//    }
//
//    //get the history information of organization
//    @PostMapping("/chaincode/getOrgHistory")
//    public ResponseEntity<Object> checkOrgHistory(@RequestBody JSONObject request){
//        ResultOuterClass.ContractResult contractResult = null;
//        String orgId = request.getString("orgId");
//        JSONArray jsonArray = null;
//        try {
//            contractResult = ChainService.getKeyHistory(orgId,"organization");
//            byte[] data = contractResult.toByteArray();
//            String res = new String(data);
//            String[] temp1 = res.split("\\[");
//            String[] temp2 = temp1[1].split("\\]");
//            String jsonMess = temp2[0];
//            String jsonStr = "["+jsonMess+"]";
//            jsonArray = JSONArray.parseArray(jsonStr);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chain error");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(jsonArray);
//    }
//
//    @PostMapping("/chaincode/getSpecialOrg")
//    public ResponseEntity<Object> getSpecialOrg(@RequestBody JSONObject request){
//        String txId = request.getString("txId");
//        ChainmakerTransaction.TransactionInfoWithRWSet transactionInfo = null;
//        String writeSetInfo = null;
//        try{
//            transactionInfo = ChainService.getTxByTxId(txId);
//            writeSetInfo = transactionInfo.getRwSet().getTxWrites(0).getValue().toStringUtf8();
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chain error");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(writeSetInfo);
//    }
//
//    @PostMapping("/chaincode/getNewestOrg")
//    public ResponseEntity<Object> getNewestOrg(@RequestBody JSONObject request){
//            String res = null;
//            String orgId = request.getString("orgId");
//            String field = "organization";
//        try{
//            res = ChainService.getNewVersion(orgId, field);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chain error");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//    }
//
//    @PostMapping("/chaincode/test/getChainConfig")
//    public ResponseEntity<Object> GetChainConfig(){
//        ChainConfigOuterClass.ChainConfig responseInfo;
//        try {
//            responseInfo = ChainService.getChainConfig();
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
//    }
//
//    @PostMapping("/chaincode/test/getContractByName/{contractName}")
//    public ResponseEntity<Object> GetContractByName(@PathVariable String contractName){
//        ContractOuterClass.Contract responseInfo;
//        try {
//            responseInfo = ChainService.getContractByName(contractName);
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
//    }
//
//    @PostMapping("/chaincode/test/getTransactionById/{txid}")
//    public ResponseEntity<Object> GetTransactionById(@PathVariable String txid){
//        ChainmakerTransaction.TransactionInfoWithRWSet transactionInfo;
//        String writeSetInfo = null;
//        try {
//            transactionInfo = ChainService.getTxByTxId(txid);
//            writeSetInfo = transactionInfo.getRwSet().getTxWrites(0).getValue().toStringUtf8();
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(writeSetInfo);
//    }
//
//    @GetMapping("/chaincode/test/getState/{key}")
//    public ResponseEntity<Object> GetState(@PathVariable String key){
//        ResultOuterClass.ContractResult state=null;
//        try {
//            state = ChainService.getState(key);
//            System.out.println(state.toString());
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(state.toString());
//    }
//
//    @PostMapping("/chaincode/test/putState/{key}")
//    public ResponseEntity<Object> PutState(@PathVariable String key, @RequestParam String value){
//        ResultOuterClass.ContractResult state;
//        try {
//            state = ChainService.putState(key, value);
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(state.toString());
//    }
//
//    @GetMapping("/chaincode/test/getHistoryByKey/{key}")
//    public ResponseEntity<Object> GetHistoryByKey(@PathVariable String key){
//        ResultOuterClass.ContractResult state;
//        try {
//            state = ChainService.getHistoryByKey(key);
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(state.toString());
//    }
}
