package com.trustchain.model.convert;

import com.trustchain.model.dto.TransactionStatisticsItemDTO;
import com.trustchain.model.entity.Transaction;
import com.trustchain.model.entity.Wallet;
import com.trustchain.model.vo.TransactionStatisticsItemVO;
import com.trustchain.model.vo.TransactionVO;
import com.trustchain.model.vo.WalletVO;
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
