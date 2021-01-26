package com.rp2.shine.src.usedstore;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.usedstore.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usedstore")
public class UsedStoreController {
    private final UsedStoreService usedStoreService;

    /**
     * 중고거래 글 등록 API
     * [POST] /usedstore/:sellerUserNo
     * @PathVariable sellerUserNo
     * @RequestBody PostUsedStoreReq
     * @return BaseResponse<PostUsedStoreRes>
     */
    @PostMapping("/{sellerUserNo}")
    public BaseResponse<PostUsedStoreRes> postUsedStore(@PathVariable Integer sellerUserNo, PostUsedStoreReq parameters) {
        // 1. Body Parameter Validation
        if(sellerUserNo == null) {
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

        // 2. Post UserInfo
        try {
            PostUsedStoreRes postUsedStoreRes = usedStoreService.createUsedStore(sellerUserNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_USEDSTORE, postUsedStoreRes);
        } catch (BaseException exception) {
            exception.printStackTrace();    // 에러 이유 추척
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원 정보 수정 API
     * [PATCH] /usedstore/:sellerUserNo/:sellPostingNo
     * @PathVariable userNo
     * @RequestBody PatchUserReq
     * @return BaseResponse<PatchUserRes>
     */
    @PatchMapping("/{sellerUserNo}/{sellPostingNo}")
    public BaseResponse<PatchUsedStoreRes> patchUsedStrore(@PathVariable Integer sellerUserNo, @PathVariable Integer sellPostingNo, PatchUsedStoreReq parameters) {

        if (sellerUserNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (sellPostingNo == null) {
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
            PatchUsedStoreRes patchUsedStoreRes = usedStoreService.updateUsedStoreInfo(sellerUserNo, sellPostingNo, parameters);
            return new BaseResponse<>(SUCCESS_PATCH_USEDSTORE, patchUsedStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 삭제 API
     * [DELETE] /usedstore/:sellerUserNo/:sellPostingNo
     * @PathVariable sellerUserNo, sellPostingNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/{sellerUserNo}/{sellPostingNo}")
    public BaseResponse<Void> deleteUsedStore(@PathVariable Integer sellerUserNo, @PathVariable Integer sellPostingNo) {
        if (sellPostingNo == null || sellPostingNo <= 0) {
            return new BaseResponse<>(EMPTY_SELLERUSERNO);
        }
        if (sellerUserNo == null || sellerUserNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            usedStoreService.deleteUsedStore(sellPostingNo);
            return new BaseResponse<>(SUCCESS_DELETE_USEDSTORE);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 글 관심 등록 API
     * [POST] /usedstore/concerns/:postringNo/:userNo
     * @PathVariable postringNo, userNo
     * @return BaseResponse<PostConcernRes>
     */
    @PostMapping("/concerns/{postingNo}/{userNo}")
    public BaseResponse<PostConcernRes> postConcern(@PathVariable Integer postingNo, @PathVariable Integer userNo) {
        // 1. Body Parameter Validation
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTNO);
        }
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        // 2. Post UserInfo
        try {
            PostConcernRes postConcernRes = usedStoreService.createConcern(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_POST_CONCERN, postConcernRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고거래 관심 삭제 API
     * [DELETE] /usedstore/concerns/:postringNo/:userNo
     * @PathVariable postringNo, userNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/concerns/{postingNo}/{userNo}")
    public BaseResponse<Void> deleteConcern(@PathVariable Integer postingNo, @PathVariable Integer userNo) {
        // 1. Body Parameter Validation
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTNO);
        }
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            usedStoreService.deleteConcern(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_DELETE_CONCERN);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
