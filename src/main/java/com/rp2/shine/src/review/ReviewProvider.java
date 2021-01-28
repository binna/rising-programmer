package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReviewProvider {
    private final ReviewRepository reviewRepository;

    /**
     * 회원별 후기 검색
     * @param userNo
     * @return List<ReviewInfo>
     * @throws BaseException
     */
    @Transactional
    public List<ReviewInfo> retrieveReviewByUserNo(Integer userNo) throws BaseException {
        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findByWriterAndStatusOrderByCreateDateDesc(userNo, "Y");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList;
    }

    /**
     * 중고거래별 후기 조회
     * @param sellPostingInfo
     * @return List<ReviewInfo>
     * @throws BaseException
     * @comment SellPostingInfo로 후기 조회
     */
    @Transactional
    public List<ReviewInfo> retrieveReviewByPostingNo(SellPostingInfo sellPostingInfo) throws BaseException {
        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findBySellPostingInfo(sellPostingInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList;
    }

    /**
     * 중고거래별 판매 후기 조회
     * @param sellPostingInfo
     * @return List<ReviewInfo>
     * @throws BaseException
     * @comment SellPostingInfo로 후기 조회
     */
    @Transactional
    public List<ReviewInfo> retrieveSellerReviewByPostingNoAndDivisionS(SellPostingInfo sellPostingInfo) throws BaseException {
        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findBySellPostingInfoAndDivision(sellPostingInfo, "S");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList;
    }

    /**
     * 중고거래별 구매 후기 조회
     * @param sellPostingInfo
     * @return List<ReviewInfo>
     * @throws BaseException
     * @comment SellPostingInfo로 후기 조회
     */
    @Transactional
    public List<ReviewInfo> retrieveBuyerReviewByPostingNoAndDivisionB(SellPostingInfo sellPostingInfo) throws BaseException {
        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findBySellPostingInfoAndDivision(sellPostingInfo, "B");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList;
    }

}
