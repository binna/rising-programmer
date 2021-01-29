package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.GetReviewRes;
import com.rp2.shine.src.review.models.ReviewInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReviewProvider {
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;

    /**
     * 회원별 후기 검색 -> user 노출용
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
     * 회원별 후기 검색 -> review 노출용
     * @param userNo
     * @return List<ReviewInfo>
     * @throws BaseException
     */
    @Transactional
    public List<GetReviewRes> retrieveReviewByUserNoReturnGetReviewRes(Integer userNo) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findByWriterAndStatusOrderByCreateDateDesc(userNo, "Y");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList.stream().map(reviewInfo -> {
            return new GetReviewRes(reviewInfo.getWriter(),
                    reviewInfo.getFileName(), reviewInfo.getFilePath(),
                    reviewInfo.getContent(),
                    reviewInfo.getCreateDate());
        }).collect(Collectors.toList());
    }

    /**
     * 회원별 판매 후기 검색 -> review 노출용
     * @param userNo
     * @return List<ReviewInfo>
     * @throws BaseException
     */
    @Transactional
    public List<GetReviewRes> retrieveSellerReviewByUserNoReturnGetReviewRes(Integer userNo) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findByWriterAndStatusAndDivisionOrderByCreateDateDesc(userNo, "Y", "S");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList.stream().map(reviewInfo -> {
            return new GetReviewRes(reviewInfo.getWriter(),
                    reviewInfo.getFileName(), reviewInfo.getFilePath(),
                    reviewInfo.getContent(),
                    reviewInfo.getCreateDate());
        }).collect(Collectors.toList());
    }

    /**
     * 회원별 구매 후기 검색 -> review 노출용
     * @param userNo
     * @return List<ReviewInfo>
     * @throws BaseException
     */
    @Transactional
    public List<GetReviewRes> retrieveBuyerReviewByUserNoReturnGetReviewRes(Integer userNo) throws BaseException {
        if(jwtService.getUserNo() != userNo) {
            throw new BaseException(INVALID_JWT);
        }

        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findByWriterAndStatusAndDivisionOrderByCreateDateDesc(userNo, "Y", "B");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList.stream().map(reviewInfo -> {
            return new GetReviewRes(reviewInfo.getWriter(),
                    reviewInfo.getFileName(), reviewInfo.getFilePath(),
                    reviewInfo.getContent(),
                    reviewInfo.getCreateDate());
        }).collect(Collectors.toList());
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
