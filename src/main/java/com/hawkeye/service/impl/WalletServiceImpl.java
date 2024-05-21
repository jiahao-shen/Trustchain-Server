package com.hawkeye.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.hawkeye.mapper.*;
import com.hawkeye.model.dto.TransactionStatisticsItemDTO;
import com.hawkeye.model.entity.Transaction;
import com.hawkeye.model.entity.User;
import com.hawkeye.model.entity.Wallet;
import com.hawkeye.model.enums.DateRange;
import com.hawkeye.model.enums.TransactionChannel;
import com.hawkeye.model.enums.TransactionMethod;
import com.hawkeye.model.enums.BalanceType;
import com.hawkeye.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hawkeye.model.entity.table.TransactionTableDef.TRANSACTION;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    private static final Logger logger = LogManager.getLogger(WalletService.class);

    @Override
    @Transactional
    public Wallet create(String userId) {
        User user = userMapper.selectOneWithRelationsById(userId);

        // 创建新钱包
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        walletMapper.insert(wallet);

        // 更新用户
        user.setWalletId(wallet.getId());
        userMapper.update(user, true);

        return wallet;
    }

    @Override
    public Wallet detail(String userId) {
        RelationManager.setMaxDepth(1);
        User user = userMapper.selectOneWithRelationsById(userId);

        if (user.getWallet() == null) {
            logger.info("no wallet: " + user.getWalletId());
            return create(userId);
        } else {
            logger.info("hava wallet");
            logger.info(user.getWallet());
            return user.getWallet();
        }
    }

    @Override
    @Transactional
    public void topUp(String userId, Double amount, TransactionChannel channel) {
        String txId;
        switch (channel) {
            case ALIPAY: {
                txId = topUpByAlipay();
                break;
            }
            case WECHAT: {
                txId = topUpByWechat();
                break;
            }
            default: {
                return;
            }
        }
        RelationManager.setMaxDepth(1);
        User user = userMapper.selectOneWithRelationsById(userId);
        Wallet wallet = user.getWallet();

        // 更新钱包余额
        wallet.setBalance(wallet.getBalance() + amount);
        walletMapper.update(wallet, true);

        // 新建交易
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setTxId(txId);
        tx.setAmount(amount);
        tx.setBalance(wallet.getBalance());
        tx.setBalanceType(BalanceType.INCOME);
        tx.setMethod(TransactionMethod.TOPUP);
        tx.setChannel(channel);

        // TODO: 上链

        transactionMapper.insert(tx);
    }

    private String topUpByAlipay() {
        // TODO: 支付宝充值
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String topUpByWechat() {
        // TODO: 微信充值
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    @Transactional
    public void withdraw(String userId, Double amount, TransactionChannel channel) {
        String txId;
        switch (channel) {
            case ALIPAY: {
                txId = withdrawByAlipay();
                break;
            }
            case WECHAT: {
                txId = withdrawByWechat();
                break;
            }
            default: {
                return;
            }
        }
        RelationManager.setMaxDepth(1);
        User user = userMapper.selectOneWithRelationsById(userId);
        Wallet wallet = user.getWallet();

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("您的余额不足");
        }

        // 更新钱包余额
        wallet.setBalance(wallet.getBalance() - amount);
        walletMapper.update(wallet, true);

        // 新建交易
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setTxId(UUID.randomUUID().toString().replaceAll("-", ""));
        tx.setAmount(amount);
        tx.setBalance(wallet.getBalance());
        tx.setBalanceType(BalanceType.EXPENSE);
        tx.setMethod(TransactionMethod.WITHDRAW);
        tx.setChannel(channel);

        // TODO: 上链

        transactionMapper.insert(tx);
    }

    private String withdrawByAlipay() {
        // TODO: 支付宝提现
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String withdrawByWechat() {
        // TODO: 微信提现
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    @Transactional
    public void sale(String userId, Double amount, TransactionChannel channel, String txId) {
        RelationManager.setMaxDepth(1);
        User user = userMapper.selectOneWithRelationsById(userId);
        Wallet wallet = user.getWallet();

        // 更新钱包余额
        wallet.setBalance(wallet.getBalance() + amount);
        walletMapper.update(wallet, true);

        // 新建交易
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setTxId(txId);
        tx.setAmount(amount);
        tx.setBalance(wallet.getBalance());
        tx.setBalanceType(BalanceType.INCOME);
        tx.setMethod(TransactionMethod.SALE);
        tx.setChannel(channel);

        // TODO: 上链

        transactionMapper.insert(tx);
    }

    @Override
    @Transactional
    public void purchase(String userId, Double amount, TransactionChannel channel, String txId) {
        RelationManager.setMaxDepth(1);
        User user = userMapper.selectOneWithRelationsById(userId);
        Wallet wallet = user.getWallet();

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("您的余额不足");
        }

        // 更新钱包余额
        wallet.setBalance(wallet.getBalance() - amount);
        walletMapper.update(wallet, true);

        // 新建交易
        Transaction tx = new Transaction();
        tx.setWalletId(wallet.getId());
        tx.setTxId(txId);
        tx.setAmount(amount);
        tx.setBalance(wallet.getBalance());
        tx.setBalanceType(BalanceType.EXPENSE);
        tx.setMethod(TransactionMethod.PURCHASE);
        tx.setChannel(channel);

        // TODO: 上链

        transactionMapper.insert(tx);
    }

    @Override
    public Page<Transaction> transactionList(String userId, Integer pageNumber, Integer pageSize, Map<String, List<String>> filter, Map<String, String> sort) {
        RelationManager.setMaxDepth(0);
        User user = userMapper.selectOneWithRelationsById(userId);

        QueryWrapper query = QueryWrapper.create()
                .select(TRANSACTION.ID,
                        TRANSACTION.BALANCE_TYPE,
                        TRANSACTION.METHOD,
                        TRANSACTION.CHANNEL,
                        TRANSACTION.BALANCE,
                        TRANSACTION.AMOUNT,
                        TRANSACTION.TIME
                )
                .from(TRANSACTION)
                .where(TRANSACTION.WALLET_ID.eq(user.getWalletId()));

        filter.forEach((key, value) -> {
            switch (key) {
                case "balanceType": {
                    query.where(TRANSACTION.BALANCE_TYPE.in(value.stream().map(BalanceType::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "method": {
                    query.where(TRANSACTION.METHOD.in(value.stream().map(TransactionMethod::valueOf).collect(Collectors.toList())));
                    break;
                }
                case "channel": {
                    query.where(TRANSACTION.CHANNEL.in(value.stream().map(TransactionChannel::valueOf).collect(Collectors.toList())));
                    break;
                }
            }
        });

        if (sort.isEmpty()) {
            query.orderBy(TRANSACTION.TIME, false);
        } else {
            sort.forEach((key, value) -> {
                switch (key) {
                    case "time": {
                        query.orderBy(TRANSACTION.TIME, "ascending".equals(value));
                    }
                }
            });
        }

        return transactionMapper.paginate(pageNumber, pageSize, query);
    }

    @Override
    public Transaction transactionDetail(String id) {
        return null;
    }

    @Override
    public Map<String, TransactionStatisticsItemDTO> transactionStatistics(String userId, DateRange range) {
        RelationManager.setMaxDepth(0);
        User user = userMapper.selectOneWithRelationsById(userId);

        QueryWrapper query = QueryWrapper.create()
                .select(TRANSACTION.AMOUNT,
                        TRANSACTION.BALANCE,
                        TRANSACTION.BALANCE_TYPE,
                        TRANSACTION.TIME)
                .from(TRANSACTION)
                .where(TRANSACTION.WALLET_ID.eq(user.getWalletId()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, TransactionStatisticsItemDTO> statistics = new TreeMap();
        switch (range) {
            case LAST_WEEK: {
                sdf = new SimpleDateFormat("MM-dd");
                for (int i = 0; i <= 7; i++) {
                    statistics.put(sdf.format(calendar.getTime()), new TransactionStatisticsItemDTO(0.0, 0.0, 0.0));
                    calendar.add(Calendar.DATE, -1);
                }
                break;
            }
            case LAST_MONTH: {
                sdf = new SimpleDateFormat("MM-dd");
                for (int i = 0; i <= 30; i++) {
                    statistics.put(sdf.format(calendar.getTime()), new TransactionStatisticsItemDTO(0.0, 0.0, 0.0));
                    calendar.add(Calendar.DATE, -1);
                }
                break;
            }
            case LAST_YEAR: {
                sdf = new SimpleDateFormat("yyyy-MM");
                for (int i = 0; i <= 12; i++) {
                    statistics.put(sdf.format(calendar.getTime()), new TransactionStatisticsItemDTO(0.0, 0.0, 0.0));
                    calendar.add(Calendar.MONTH, -1);
                }
                break;
            }
        }
        query.where(TRANSACTION.TIME.gt(calendar.getTime()));

        List<Transaction> transactions = transactionMapper.selectListByQuery(query);
        for (Transaction tx : transactions) {
            String key = sdf.format(tx.getTime());
            TransactionStatisticsItemDTO item = statistics.get(key);
            if (tx.getBalanceType() == BalanceType.INCOME) {
                item.setIncome(item.getIncome() + tx.getAmount());
            } else {
                item.setExpense(item.getExpense() + tx.getAmount());
            }
        }

        return statistics;
    }
}
