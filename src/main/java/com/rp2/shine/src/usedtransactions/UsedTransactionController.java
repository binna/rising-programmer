package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.usedtransactions.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/used-transactions")
public class UsedTransactionController {
    private final UsedTransactionService usedTransactionsService;

    /**
     * 중고거래 글 등록 API
     * [POST] /used-transactions
     * @RequestBody PostUsedTransactionsReq
     * @return BaseResponse<PostUsedTransactionsRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUsedTransactionsRes> postUsedTransactions(@RequestBody(required = false) PostUsedTransactionsReq parameters) {
        if (parameters.getTitle() == null || parameters.getTitle().length() == 0) {
            return new BaseResponse<>(EMPTY_TITLE);
        }
        if (parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_CONTENT);
        }
        if (parameters.getCategory() == null || parameters.getCategory().length() == 0) {
            return new BaseResponse<>(EMPTY_CATEGORY);
        }
        if (parameters.getPrice() == null) {
            return new BaseResponse<>(EMPTY_PRICE);
        }

        try {
            PostUsedTransactionsRes postUsedStoreRes = usedTransactionsService.createUsedTransactions(parameters);
            return new BaseResponse<>(SUCCESS_POST_POSTING, postUsedStoreRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 수정 API
     * [PATCH] /used-transactions/:postingNo
     * @PathVariable postingNo
     * @RequestBody PatchUsedTransactionReq
     * @return BaseResponse<PatchUsedTransactionRes>
     */
    @ResponseBody
    @PatchMapping("/{postingNo}")
    public BaseResponse<PatchUsedTransactionRes> patchUsedTransaction(@PathVariable Integer postingNo, @RequestBody(required = false) PatchUsedTransactionReq parameters) {
        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (parameters.getTitle() == null || parameters.getTitle().length() == 0) {
            return new BaseResponse<>(EMPTY_TITLE);
        }
        if (parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_CONTENT);
        }
        if (parameters.getCategory() == null || parameters.getCategory().length() == 0) {
            return new BaseResponse<>(EMPTY_CATEGORY);
        }
        if (parameters.getPrice() == null) {
            return new BaseResponse<>(EMPTY_PRICE);
        }

        try {
            PatchUsedTransactionRes patchUsedStoreRes = usedTransactionsService.updateUsedTransaction(postingNo, parameters);
            return new BaseResponse<>(SUCCESS_PATCH_POSTING, patchUsedStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 판매완료 API
     * [PATCH] /used-transactions/:postingNo
     * @PathVariable postingNo
     * @RequestBody buyerNo
     * @return BaseResponse<PatchUsedTransactionRes>
     */
    @ResponseBody
    @PatchMapping("/{postingNo}")
    public BaseResponse<Void> patchUsedTransactionSalesCompleted(@PathVariable Integer postingNo, @RequestBody(required = false) HashMap<String, Integer> parameter) {
        Integer buyerNo = parameter.get("buyerNo");

        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTINGNO);
        }
        if (buyerNo == null) {
            return new BaseResponse<>(EMPTY_BUYERUSERNO);
        }

        try {
            usedTransactionsService.patchUsedTransactionSalesCompleted(postingNo, buyerNo);
            return new BaseResponse<>(SUCCESS_SALES_COMPLETED);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 삭제 API
     * [DELETE] /usedtransactions/:postingNo/:userNo
     * @PathVariable postingNo, userNo
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @DeleteMapping("/{postingNo}/{userNo}")
    public BaseResponse<Void> deleteUsedTransaction(@PathVariable Integer postingNo, @PathVariable Integer userNo) {
        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_SELLERUSERNO);
        }
        if (userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            usedTransactionsService.deleteUsedTransaction(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_DELETE_POSTING);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 관심 등록, 삭제 API
     * [POST] /used-transactions/:postingNo/concern
     * @PathVariable postingNo
     * @return BaseResponse<PostConcernRes>
     */
    @ResponseBody
    @PostMapping("/{postingNo}/concerns")
    public BaseResponse<PostConcernRes> postConcern(@PathVariable Integer postingNo) {
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTINGNO);
        }

        try {
            PostConcernRes postConcernRes = usedTransactionsService.concern(postingNo);
            return new BaseResponse<>(SUCCESS_POST_CONCERN, postConcernRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            return new BaseResponse<>(exception.getStatus());
        }
    }
}