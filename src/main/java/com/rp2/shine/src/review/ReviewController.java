package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.review.models.GetReviewRes;
import com.rp2.shine.src.review.models.PostReviewReq;
import com.rp2.shine.src.review.models.PostReviewRes;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.user.models.GetUsersRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewProvider reviewProvider;

    /**
     * 회원별 후기 전체 조회 API
     * [GET] /reviews/all/:userNo
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("/all/{userNo}")
    public BaseResponse<List<GetReviewRes>> getReview(@PathVariable Integer userNo) {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveReviewByUserNoReturnGetReviewRes(userNo);
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원별 판매 후기 조회 API
     * [GET] /reviews/seller/:userNo
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("/seller/{userNo}")
    public BaseResponse<List<GetReviewRes>> getSellerReview(@PathVariable Integer userNo) {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveSellerReviewByUserNoReturnGetReviewRes(userNo);
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원별 구매 후기 조회 API
     * [GET] /reviews/buyer/:userNo
     * @return BaseResponse<List<GetUsersRes>>
     */
    @GetMapping("/buyer/{userNo}")
    public BaseResponse<List<GetReviewRes>> getBuyerReview(@PathVariable Integer userNo) {
        try {
            List<GetReviewRes> getReviewResList = reviewProvider.retrieveBuyerReviewByUserNoReturnGetReviewRes(userNo);
            return new BaseResponse<>(SUCCESS_GET_REVIEW, getReviewResList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 판매자 후기 등록 API
     * [POST] /reviews/seller/:userNo/post/:postingNo
     * @PathVariable userNo, postingNo
     * @RequestBody PostReviewReq
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/seller/{userNo}/post/{postingNo}")
    public BaseResponse<PostReviewRes> postSellerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo, @RequestBody(required = false) PostReviewReq parameters) {
        if(parameters.getContent() == null || parameters.getContent().length() == 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }
        if (parameters.getTakeManner() == null) {
            return new BaseResponse<>(EMPTY_MANNERSCORE);
        }

        try {
            PostReviewRes postReviewRes = reviewService.createSellerReviewInfo(userNo, postingNo, parameters);
            return new BaseResponse<>(SUCCESS_POST_REVIEW, postReviewRes);
        } catch (BaseException exception) {
            //exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

     /** 구매자 후기 등록 API
     * [POST] /reviews/buyer/:userNo/post/:postingNo
     * @PathVariable userNo, postingNo
     * @RequestBody PostReviewReq
     * @return BaseResponse<PostReviewRes>
     */
     @ResponseBody
     @PostMapping("/buyer/{userNo}/post/{postingNo}")
     public BaseResponse<PostReviewRes> postBuyerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo, @RequestBody(required = false) PostReviewReq parameters) {
         if(parameters.getContent() == null || parameters.getContent().length() == 0) {
             return new BaseResponse<>(EMPTY_USERNO);
         }
         if (parameters.getTakeManner() == null) {
             return new BaseResponse<>(EMPTY_MANNERSCORE);
         }

         try {
             PostReviewRes postReviewRes = reviewService.createBuyerReviewInfo(userNo, postingNo, parameters);
             return new BaseResponse<>(SUCCESS_POST_REVIEW, postReviewRes);
         } catch (BaseException exception) {
             //exception.printStackTrace();
             return new BaseResponse<>(exception.getStatus());
         }
     }

    /**
     * 판매자 후기 삭제 API
     * [DELETE] /seller/:userNo/delete/:postingNo
     * @PathVariable userNo, postingNo
     * @return BaseResponse<Void>
     */
    @DeleteMapping("/seller/{userNo}/delete/{postingNo}")
    public BaseResponse<Void> deleteSellerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            reviewService.deleteSellerReview(userNo, postingNo);
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
    @DeleteMapping("/buyer/{userNo}/delete/{postingNo}")
    public BaseResponse<Void> deleteBuyerReview(@PathVariable Integer userNo, @PathVariable Integer postingNo) {
        if (userNo == null || userNo <= 0) {
            return new BaseResponse<>(EMPTY_USERNO);
        }

        try {
            reviewService.deleteBuyerReview(userNo, postingNo);
            return new BaseResponse<>(SUCCESS_DELETE_REVIEW);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
