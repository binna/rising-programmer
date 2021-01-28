package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.usedtransactions.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usedtransactions")
public class UsedTransactionController {
    private final UsedTransactionService usedTransactionsService;

    /**
     * 중고거래 글 등록 API
     * [POST] /usedtransactions/:userNo
     * @PathVariable userNo
     * @RequestBody PostUsedTransactionsReq
     * @return BaseResponse<PostUsedTransactionsRes>
     */
    @ResponseBody
    @PostMapping("/{userNo}")
    public BaseResponse<PostUsedTransactionsRes> postUsedTransactions(@PathVariable Integer userNo, @RequestBody(required = false) PostUsedTransactionsReq parameters) {
        if(userNo == null) {
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
            PostUsedTransactionsRes postUsedStoreRes = usedTransactionsService.createUsedTransactions(userNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_POSTING, postUsedStoreRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /usedtransactions/:postingNo/:userNo
     * @PathVariable postingNo, userNo
     * @RequestBody PatchUsedTransactionReq
     * @return BaseResponse<PatchUsedTransactionRes>
     */
    @ResponseBody
    @PatchMapping("/{postingNo}/{userNo}")
    public BaseResponse<PatchUsedTransactionRes> patchUsedTransaction(@PathVariable Integer postingNo, @PathVariable Integer userNo, @RequestBody(required = false) PatchUsedTransactionReq parameters) {
        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (userNo == null) {
            return new BaseResponse<>(EMPTY_SELLERUSERNO);
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
            PatchUsedTransactionRes patchUsedStoreRes = usedTransactionsService.updateUsedTransaction(postingNo, userNo, parameters);
            return new BaseResponse<>(SUCCESS_PATCH_POSTING, patchUsedStoreRes);
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
     * 중고거래 글 관심 등록 API
     * [POST] /usedtransactions/concerns/:postingNo/:userNo
     * @PathVariable postingNo, userNo
     * @return BaseResponse<PostConcernRes>
     */
    @ResponseBody
    @PostMapping("/concerns/{postingNo}/{userNo}")
    public BaseResponse<PostConcernRes> postConcern(@PathVariable Integer postingNo, @PathVariable Integer userNo) {
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTINGNO);
        }
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            PostConcernRes postConcernRes = usedTransactionsService.createConcern(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_POST_CONCERN, postConcernRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 관심 삭제 API
     * [DELETE] /usedtransactions/concerns/:postingNo/:userNo
     * @PathVariable postringNo, userNo
     * @return BaseResponse<Void>
     */
    @ResponseBody
    @DeleteMapping("/concerns/{postingNo}/{userNo}")
    public BaseResponse<Void> deleteConcern(@PathVariable Integer postingNo, @PathVariable Integer userNo) {
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTINGNO);
        }
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            usedTransactionsService.deleteConcern(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_DELETE_CONCERN);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
