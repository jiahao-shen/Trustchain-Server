package com.hawkeye.controller;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.mybatisflex.core.paginate.Page;
import com.hawkeye.model.convert.WalletConvert;
import com.hawkeye.model.dto.TransactionStatisticsItemDTO;
import com.hawkeye.model.entity.Transaction;
import com.hawkeye.model.entity.User;
import com.hawkeye.model.entity.Wallet;
import com.hawkeye.model.enums.DateRange;
import com.hawkeye.model.enums.StatusCode;
import com.hawkeye.model.enums.TransactionChannel;
import com.hawkeye.model.vo.BaseResponse;
import com.hawkeye.model.vo.TransactionVO;
import com.hawkeye.model.vo.WalletVO;
import com.hawkeye.service.CaptchaService;
import com.hawkeye.service.WalletService;
import com.hawkeye.util.AuthUtil;
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
        Integer pageNumber = request.getInteger("pageNumber");
        Integer pageSize = request.getInteger("pageSize");
        Map<String, List<String>> filter = request.getObject("filter", new TypeReference<Map<String, List<String>>>() {
        });
        Map<String, String> sort = request.getObject("sort", new TypeReference<Map<String, String>>() {
        });

        User user = AuthUtil.getUser();

        Page<Transaction> transactions = walletService.transactionList(user.getId(), pageNumber, pageSize, filter, sort);

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
