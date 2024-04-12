package com.trustchain.service;

import com.alibaba.fastjson2.JSONObject;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.config.ChainConfigOuterClass;

public interface ChaincodeService {
    /**
     * @param key 存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @param jsonObject 存入区块链账本的值
     * @return 是否调用合约成功
     */
    ResultOuterClass.ContractResult invokeContractUpload(String key, String field, JSONObject jsonObject);

    /**
     * @param key 存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @return 关于键和字段的账本历史记录
     */
    ResultOuterClass.ContractResult getKeyHistory(String key, String field);

    /**
     * @param TxId 交易号
     * @return 具体的交易信息
     */
    ChainmakerTransaction.TransactionInfo getTxByTxId(String TxId);

    /**
     * @param key 存入区块链账本的键
     * @return 关于键最新一次存入的值
     */
    ResultOuterClass.ContractResult getNewVersion(String key);

    /**
     * @return 区块链的信息
     */
    ChainConfigOuterClass.ChainConfig getChainConfig();

    /**
     * @param name 合约名
     * @return 具体的合约信息
     */
    ContractOuterClass.Contract getContractByName(String name);

    /**
     * @param key 存入区块链账本的键
     * @param value 存入区块链账本的值
     * @return 是否调用合约成功
     */
    ResultOuterClass.ContractResult putState(String key, String value);

    /**
     * @param key 存入区块链账本的键
     * @return 关于键最新一次存入的值
     */
    ResultOuterClass.ContractResult getState(String key);

    /**
     * @param key 存入区块链账本的键
     * @return 关于键的账本历史记录
     */
    ResultOuterClass.ContractResult getHistoryByKey(String key);
}
