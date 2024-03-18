package com.trustchain.controller.chainmaker;

import com.trustchain.service.ChaincodeService;
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
    private ChaincodeService chaincodeService;
    @PostMapping("/chaincode/test/save/{fileName}")
    public ResponseEntity<Object> Save(@PathVariable String fileName, @RequestParam String fileHash, @RequestParam String time){
        ResultOuterClass.TxResponse responseInfo;
        try {
            responseInfo = chaincodeService.invokeContractSave(fileName,fileHash,time);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    @PostMapping("/chaincode/test/findByFileHash/{fileHash}")
    public ResponseEntity<Object> FindByFileHash(@PathVariable String fileHash){
        ResultOuterClass.TxResponse responseInfo;
        try {
            responseInfo = chaincodeService.invokeContractFind(fileHash);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    @PostMapping("/chaincode/test/getChainConfig")
    public ResponseEntity<Object> GetChainConfig(){
        ChainConfigOuterClass.ChainConfig responseInfo;
        try {
            responseInfo = chaincodeService.getChainConfig();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    //todo: conflict with fastJson version
    @PostMapping("/chaincode/test/getContractList")
    public ResponseEntity<Object> GetContractList(){
        ContractOuterClass.Contract[] responseInfo;
        try {
            responseInfo = chaincodeService.getContractList();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    @PostMapping("/chaincode/test/getContractByName/{contractName}")
    public ResponseEntity<Object> GetContractByName(@PathVariable String contractName){
        ContractOuterClass.Contract responseInfo;
        try {
            responseInfo = chaincodeService.getContractByName(contractName);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("chaincode error:  " + e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }
}
