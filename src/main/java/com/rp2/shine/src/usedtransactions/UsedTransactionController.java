package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.review.ReviewService;
import com.rp2.shine.src.review.models.PostReviewReq;
import com.rp2.shine.src.review.models.PostReviewRes;
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
    private final ReviewService reviewService;

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
     * [PATCH] /used-transactions/:postingNo/sales-completed
     * @PathVariable postingNo
     * @RequestBody buyerNo
     * @return BaseResponse<PatchUsedTransactionRes>
     */
    @ResponseBody
    @PatchMapping("/{postingNo}/sales-completed")
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
     * [DELETE] /used-transactions/:postingNo
     * @PathVariable postingNo
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @DeleteMapping("/{postingNo}/{userNo}")
    public BaseResponse<Void> deleteUsedTransaction(@PathVariable Integer postingNo) {
        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_SELLERUSERNO);
        }

        try {
            usedTransactionsService.deleteUsedTransaction(postingNo);
            return new BaseResponse<>(SUCCESS_DELETE_POSTING);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // ===================================================================================
    /**
     * 중고거래 글 관심 등록, 삭제 API
     * [POST] /used-transactions/:postingNo/concern
     * @PathVariable postingNo
     * @return BaseResponse<PostConcernRes>
     */
    @ResponseBody
    @PostMapping("/{postingNo}/concern")
    public BaseResponse<PostConcernRes> postConcern(@PathVariable Integer postingNo) {
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTINGNO);
        }

        try {
            PostConcernRes postConcernRes = usedTransactionsService.concern(postingNo);
            if(postConcernRes != null) {
                return new BaseResponse<>(SUCCESS_POST_CONCERN, postConcernRes);
            } else {
                return new BaseResponse<>(SUCCESS_DELETE_CONCERN);
            }
        } catch (BaseException exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // ===================================================================================
    /**
     * 판매자 후기 등록 API
     * [POST] /used-transactions/:postingNo/reviews/seller
     * @PathVariable userNo, postingNo
     * @RequestBody PostReviewReq
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/{postingNo}/reviews/seller")
    public BaseResponse<PostReviewRes> postSellerReview(@PathVariable Integer postingNo, @RequestBody(required = false) PostReviewReq parameters) {
        if(parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_CONTENT);
        }
        if (parameters.getTakeManner() == null) {
            return new BaseResponse<>(EMPTY_MANNERSCORE);
        }

        try {
            PostReviewRes postReviewRes = reviewService.createSellerReviewInfo(postingNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_REVIEW, postReviewRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 구매자 후기 등록 API
     * [POST] /used-transactions/:postingNo/reviews/buyer
     * @PathVariable userNo, postingNo
     * @RequestBody PostReviewReq
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/{postingNo}/reviews/buyer")
    public BaseResponse<PostReviewRes> postBuyerReview(@PathVariable Integer postingNo, @RequestBody(required = false) PostReviewReq parameters) {
        if(parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (parameters.getTakeManner() == null) {
            return new BaseResponse<>(EMPTY_MANNERSCORE);
        }

        try {
            PostReviewRes postReviewRes = reviewService.createBuyerReviewInfo(postingNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_REVIEW, postReviewRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 판매자 후기 삭제 API
     * [DELETE] /used-transactions/:postingNo/reviews/seller
     * @PathVariable postingNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{postingNo}/reviews/seller")
    public BaseResponse<Void> deleteSellerReview(@PathVariable Integer postingNo) {
        try {
            reviewService.deleteSellerReview(postingNo);
            return new BaseResponse<>(SUCCESS_DELETE_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 구매자 후기 삭제 API
     * [DELETE] /buyer/:userNo/delete/:postingNo
     * @PathVariable userNo, postingNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{postingNo}/reviews/buyer")
    public BaseResponse<Void> deleteBuyerReview(@PathVariable Integer postingNo) {
        try {
            reviewService.deleteBuyerReview(postingNo);
            return new BaseResponse<>(SUCCESS_DELETE_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}