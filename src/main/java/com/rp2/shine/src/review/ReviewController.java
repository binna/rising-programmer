package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.review.models.GetReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewProvider reviewProvider;

    /**
     * 회원별 후기 전체 조회 API
     * [GET] /reviews
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetReviewRes>> getReview() {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveReviewByUserNoReturnGetReviewRes();
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원별 판매 후기 조회 API
     * [GET] /reviews/seller
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("/seller")
    public BaseResponse<List<GetReviewRes>> getSellerReview() {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveSellerReviewByUserNoReturnGetReviewRes();
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원별 구매 후기 조회 API
     * [GET] /reviews/buyer
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("/buyer")
    public BaseResponse<List<GetReviewRes>> getBuyerReview() {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveBuyerReviewByUserNoReturnGetReviewRes();
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}