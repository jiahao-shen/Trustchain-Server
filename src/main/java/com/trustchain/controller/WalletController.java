package com.trustchain.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.mybatisflex.core.paginate.Page;
import com.trustchain.model.convert.UserConvert;
import com.trustchain.model.convert.WalletConvert;
import com.trustchain.model.dto.TransactionStatisticsItemDTO;
import com.trustchain.model.entity.Transaction;
import com.trustchain.model.entity.User;
import com.trustchain.model.entity.Wallet;
import com.trustchain.model.enums.DateRange;
import com.trustchain.model.enums.StatusCode;
import com.trustchain.model.enums.TransactionChannel;
import com.trustchain.model.vo.BaseResponse;
import com.trustchain.model.vo.TransactionVO;
import com.trustchain.model.vo.WalletVO;
import com.trustchain.service.CaptchaService;
import com.trustchain.service.WalletService;
import com.trustchain.util.AuthUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private CaptchaService captchaService;

    private static final Logger logger = LogManager.getLogger(WalletController.class);

    @GetMapping("/detail")
    @ResponseBody
    public BaseResponse<WalletVO> detail() {
        User user = AuthUtil.getUser();

        Wallet wallet = walletService.detail(user.getId());

        return new BaseResponse(StatusCode.SUCCESS, "", WalletConvert.INSTANCE.toWalletVO(wallet));
    }


    @PostMapping("/topup")
    public BaseResponse<Boolean> topUp(@RequestBody JSONObject request) {
        Double amount = request.getDouble("amount");
        TransactionChannel channel = TransactionChannel.valueOf(request.getString("channel"));
        User user = AuthUtil.getUser();

        walletService.topUp(user.getId(), amount, channel);

        return new BaseResponse(StatusCode.SUCCESS, "", null);
    }

    @PostMapping("/withdraw")
    public BaseResponse<Boolean> withdraw(@RequestBody JSONObject request) {
        Double amount = request.getDouble("amount");
        TransactionChannel channel = TransactionChannel.valueOf(request.getString("channel"));
        String code = request.getString("code");
        User user = AuthUtil.getUser();

        // 判断验证码是否正确
        captchaService.verify(user.getEmail(), code);

        walletService.withdraw(user.getId(), amount, channel);

        return new BaseResponse(StatusCode.SUCCESS, "", null);
    }

    @PostMapping("/transaction/list")
    public BaseResponse<Page<TransactionVO>> transactionList(@RequestBody JSONObject request) {
        Integer pageNumebr = request.getInteger("pageNumber");
        Integer pageSize = request.getInteger("pageSize");
        Map<String, List<String>> filter = request.getObject("filter", new TypeReference<Map<String, List<String>>>() {
        });
        Map<String, String> sort = request.getObject("sort", new TypeReference<Map<String, String>>() {
        });

        User user = AuthUtil.getUser();

        Page<Transaction> transactions = walletService.transactionList(user.getId(), pageNumebr, pageSize, filter, sort);

        return new BaseResponse(StatusCode.SUCCESS, "",
                new Page(WalletConvert.INSTANCE.toTransactionVOList(transactions.getRecords()),
                        transactions.getPageNumber(), transactions.getPageSize(), transactions.getTotalRow()));
    }

    @PostMapping("/transaction/detail")
    public BaseResponse<TransactionVO> transactionDetail(@RequestBody JSONObject request) {
        // TODO:
        walletService.transactionDetail(null);
        return null;
    }

    @PostMapping("/transaction/statistics")
    public BaseResponse<?> transcationStatistics(@RequestBody JSONObject request) {
        DateRange range = DateRange.valueOf(request.getString("range"));
        User user = AuthUtil.getUser();

        Map<String, TransactionStatisticsItemDTO> statistics = walletService.transactionStatistics(user.getId(), range);
        logger.error(statistics);
        return new BaseResponse(StatusCode.SUCCESS, "",
                WalletConvert.INSTANCE.toMapTransactionStatisticsItemVO(statistics));
    }
}
