package com.hawkeye.service;

public interface ChainService {

    /**
     * @param key   存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @param value 存入区块链账本的值
     * @return 是否调用合约成功
     */
    String putState(String key, String field, String value);

    /**
     * @param key   存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @param value 存入区块链账本的值
     * @param txId  写入区块链的交易号
     * @return 是否调用合约成功
     */
    String putState(String key, String field, String value, String txId);

    /**
     * @param key   存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @return 是否调用合约成功
     */
    String getState(String key, String field);

    /**
     * @param txId 区块链的交易号
     * @return 是否调用合约成功
     */
    String getState(String txId);

    /**
     * @param key   存入区块链账本的键
     * @param field 存入区块链账本的字段
     * @return 是否调用合约成功
     */
    String getHistory(String key, String field);
}
