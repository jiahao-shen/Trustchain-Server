package com.trustchain.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.service.ChainService;
import com.trustchain.service.OrganizationService;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ChainCodeController {
    @Autowired
    private ChainService ChainService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMapper organizationMapper;

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
