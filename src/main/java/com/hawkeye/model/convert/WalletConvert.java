package com.hawkeye.model.convert;

import com.hawkeye.model.dto.TransactionStatisticsItemDTO;
import com.hawkeye.model.entity.Transaction;
import com.hawkeye.model.entity.Wallet;
import com.hawkeye.model.vo.TransactionStatisticsItemVO;
import com.hawkeye.model.vo.TransactionVO;
import com.hawkeye.model.vo.WalletVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface WalletConvert {
    WalletConvert INSTANCE = Mappers.getMapper(WalletConvert.class);

    WalletVO toWalletVO(Wallet wallet);

    TransactionVO toTransactionVO(Transaction transaction);

    List<TransactionVO> toTransactionVOList(List<Transaction> transactionList);

    TransactionStatisticsItemVO toTransactionStatisticsItemVO(TransactionStatisticsItemDTO transactionStatisticsItemDTO);

    Map<String, TransactionStatisticsItemVO> toMapTransactionStatisticsItemVO(Map<String, TransactionStatisticsItemDTO> transactionStatisticsItemDTOMap);
}
