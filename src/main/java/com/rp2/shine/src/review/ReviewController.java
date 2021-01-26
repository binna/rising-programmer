package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.review.models.PostReviewReq;
import com.rp2.shine.src.review.models.PostReviewRes;
import com.rp2.shine.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtService jwtService;

    /**
     * 판매자 후기 등록 API
     * [POST] /review/seller/:userNo/:postingNo
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @PostMapping("/seller/{userNo}/{postingNo}")
    public BaseResponse<PostReviewRes> postSellerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo, PostReviewReq parameters) {
        if (userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (postingNo == null) {
           return new BaseResponse<>(EMPTY_USEDSTORE);
        }
        if (parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_CONTENT);
        }
        if(parameters.getTakeManner() == null) {
            return new BaseResponse<>(EMPTY_MANNERSCORE);
        }

        try {
            PostReviewRes postUserRes = reviewService.createSellerReviewInfo(userNo, postingNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_REVIEW, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 구매자 후기 등록 API
     * [POST] /review/buyer/:userNo/:postingNo
     * @RequestBody PostUserReq
     * @return BaseResponse<PostUserRes>
     */
    @PostMapping("/buyer/{userNo}/{postingNo}")
    public BaseResponse<PostReviewRes> postBuyerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo, PostReviewReq parameters) {
        if (userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (postingNo == null) {
            return new BaseResponse<>(EMPTY_USEDSTORE);
        }
        if (parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_CONTENT);
        }
        if(parameters.getTakeManner() == null) {
            return new BaseResponse<>(EMPTY_MANNERSCORE);
        }

        try {
            PostReviewRes postUserRes = reviewService.createBuyerReviewInfo(userNo, postingNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_REVIEW, postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 판매자 후기 삭제 API
     * [DELETE] /review/seller/:userNo/:postingNo
     * @PathVariable postringNo, userNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/seller/{userNo}/{postingNo}")
    public BaseResponse<Void> deleteSellerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo) {
        // 1. Body Parameter Validation
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTNO);
        }

        try {
            reviewService.deleteSellerReview(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_DELETE_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 구매자 후기 삭제 API
     * [DELETE] /review/seller/:userNo/:postingNo
     * @PathVariable postringNo, userNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/buyer/{userNo}/{postingNo}")
    public BaseResponse<Void> deleteBuyerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo) {
        // 1. Body Parameter Validation
        if(userNo == null) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if(postingNo == null) {
            return new BaseResponse<>(EMPTY_POSTNO);
        }

        try {
            reviewService.deleteBuyerReview(postingNo, userNo);
            return new BaseResponse<>(SUCCESS_DELETE_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}