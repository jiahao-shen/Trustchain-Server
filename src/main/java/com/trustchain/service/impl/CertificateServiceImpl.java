package com.trustchain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.relation.RelationManager;
import com.trustchain.mapper.CertificateMapper;
import com.trustchain.model.entity.Certificate;
import com.trustchain.service.CertificateService;
import com.trustchain.service.ChainService;
import org.chainmaker.pb.common.ResultOuterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private ChainService ChainService;


    @Override
    public boolean generate(Certificate certificate) {
//        int count = certificateMapper.insert(certificate);
//        if(count < 0){
//            return false;
//        }
//        ResultOuterClass.ContractResult contractResult = null;
//        String field = "certificate";
//        JSONObject jsonObject = JSONObject.from(certificate);
//        try{
//            contractResult = ChainService.invokeContractUpload(certificate.getId(), field, jsonObject);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }

    @Override
    public List<Certificate> getCertificateHistory(String certificateId) {
//
//        String field = "certificate";
//        ResultOuterClass.ContractResult contractResult = null;
//        List<Certificate> certificates = new ArrayList<>();
//        try{
//            contractResult = ChainService.getKeyHistory(certificateId, field);
//            byte[] data = contractResult.toByteArray();
//            String res = new String(data);
//            String[] temp1 = res.split("\\[");
//            if(temp1.length < 2){
//                certificates.add(certificateMapper.selectOneById(certificateId));
//            }else{
//                String[] temp2 = temp1[1].split("\\]");
//                String jsonMess = temp2[0];
//                String jsonStr = "["+jsonMess+"]";
//                JSONArray jsonArray = JSONArray.parseArray(jsonStr);
//                for(int i=0; i < jsonArray.size(); i++){
//                    JSONObject tmp = jsonArray.getJSONObject(i);
//                    if(tmp.containsKey("value")){
//                        certificates.add(JSON.parseObject(tmp.getString("value"), Certificate.class));
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        return certificates;
        return null;
    }

    @Override
    public Certificate informationDetail(String certificateId, String version) {
//
//        if(version.equals("@latest")){
//
//        }
//        String res = ChainService.getTxByTxId(version).getRwSet().getTxWrites(0).getValue().toStringUtf8();
//        Certificate certificate = JSON.parseObject(res, Certificate.class);
//        return certificate;
        return null;
    }

    @Override
    public Certificate informationUpdate(Certificate certificate) {
//        ResultOuterClass.ContractResult contractResult = null;
//        String field = "certificate";
//        JSONObject jsonObject = JSONObject.from(certificate);
//        try{
//            contractResult = ChainService.invokeContractUpload(certificate.getId(), field, jsonObject);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//        certificateMapper.update(certificate);
//        RelationManager.setMaxDepth(1);
//        return certificateMapper.selectOneWithRelationsById(certificate.getId());
        return null;
    }

}
