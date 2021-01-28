package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.PostReviewReq;
import com.rp2.shine.src.review.models.PostReviewRes;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.usedtransactions.UsedTransactionProvider;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.src.user.MannerScoreRepository;
import com.rp2.shine.src.user.models.MannerScoreInfo;
import com.rp2.shine.src.user.models.PostUserReq;
import com.rp2.shine.src.user.models.PostUserRes;
import com.rp2.shine.src.user.models.UserInfo;
import com.rp2.shine.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MannerScoreRepository mannerScoreRepository;
    private final UsedTransactionProvider usedTransactionProvider;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    /**
     * 판매 후기 작성
     * @param postingNo, userNo, parameters
     * @return PostReviewRes
     * @throws BaseException
     */
    @Transactional
    public PostReviewRes createSellerReviewInfo(Integer userNo, Integer postingNo, PostReviewReq parameters) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        ReviewInfo reviewInfo = new ReviewInfo(parameters.getContent(), sellPostingInfo, "S", parameters.getFileName(),parameters.getFilePath());
        MannerScoreInfo mannerScoreInfo = new MannerScoreInfo(sellPostingInfo.getSellerUserNo(), parameters.getTakeManner());

        // 판매자와 로그인 일치여부 확인
        if(!userNo.equals(sellPostingInfo.getSellerUserNo().getUserNo())) {
            throw new BaseException(DO_NOT_MATCH_USERNO);
        }

        // 판매완료 상태인지 확인
        if(!sellPostingInfo.getStatus().equals("B")) {
            throw new BaseException(DO_NOT_SALES_COMPLETED);
        }
        
        // 이미 존재하는 후기
        if(!reviewProvider.retrieveSellerReviewByPostingNoAndDivisionS(sellPostingInfo).isEmpty()) {
            throw new BaseException(ALREADY_REVIEW);
        }

        try {
            mannerScoreInfo = mannerScoreRepository.save(mannerScoreInfo);
            reviewInfo = reviewRepository.save(reviewInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }

        return new PostReviewRes(reviewInfo.getDivision(), reviewInfo.getReviewNo(), reviewInfo.getWriter(),
                reviewInfo.getContent(), reviewInfo.getStatus(), reviewInfo.getCreateDate(),
                reviewInfo.getFilePath(), reviewInfo.getFilePath(), mannerScoreInfo.getTakeManner(), mannerScoreInfo.getUserNo().getUserNo());
    }

    /**
     * 구매 후기 작성
     * @param postingNo, userNo, parameters
     * @return PostReviewRes
     * @throws BaseException
     */
    @Transactional
    public PostReviewRes createBuyerReviewInfo(Integer userNo, Integer postingNo, PostReviewReq parameters) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        SellPostingInfo sellPostingInfo = usedTransactionProvider.retrievePostingByPostingNo(postingNo);
        ReviewInfo reviewInfo = new ReviewInfo(parameters.getContent(), sellPostingInfo, "B", parameters.getFileName(),parameters.getFilePath());
        MannerScoreInfo mannerScoreInfo = new MannerScoreInfo(sellPostingInfo.getSellerUserNo(), parameters.getTakeManner());

        // 구매자와 로그인 일치여부 확인
        if(!userNo.equals(sellPostingInfo.getBuyerUserNo().getUserNo())) {
            throw new BaseException(DO_NOT_MATCH_USERNO);
        }

        // 판매완료 상태인지 확인
        if(!sellPostingInfo.getStatus().equals("B")) {
            throw new BaseException(DO_NOT_SALES_COMPLETED);
        }

        // 이미 존재하는 후기
        if(!reviewProvider.retrieveBuyerReviewByPostingNoAndDivisionB(sellPostingInfo).isEmpty()) {
            throw new BaseException(ALREADY_REVIEW);
        }

        try {
            mannerScoreInfo = mannerScoreRepository.save(mannerScoreInfo);
            reviewInfo = reviewRepository.save(reviewInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_USER);
        }

        return new PostReviewRes(reviewInfo.getDivision(), reviewInfo.getReviewNo(), reviewInfo.getWriter(),
                reviewInfo.getContent(), reviewInfo.getStatus(), reviewInfo.getCreateDate(),
                reviewInfo.getFilePath(), reviewInfo.getFilePath(), mannerScoreInfo.getTakeManner(), mannerScoreInfo.getUserNo().getUserNo());
    }
}
