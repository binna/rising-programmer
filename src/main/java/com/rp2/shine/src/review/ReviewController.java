package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.config.BaseResponse;
import com.rp2.shine.src.review.models.PostReviewReq;
import com.rp2.shine.src.review.models.PostReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

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
}
