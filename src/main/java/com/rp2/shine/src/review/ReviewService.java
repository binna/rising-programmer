package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.*;
import com.rp2.shine.src.usedstore.UsedStoreProvider;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import com.rp2.shine.src.user.MannerScoreRepository;
import com.rp2.shine.src.user.UserInfoProvider;
import com.rp2.shine.src.user.models.MannerScoreInfo;
import com.rp2.shine.src.user.models.UsersInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final SellerReviewRepository sellerReviewRepository;
    private final BuyerReviewRepository buyerReviewRepository;
    private final MannerScoreRepository mannerScoreRepository;
    private final UserInfoProvider userInfoProvider;
    private final UsedStoreProvider usedStoreProvider;
    private final ReviewProvider reviewProvider;

    /**
     * 판매자 후기 등록
     * @param parameters
     * @return PostSellerReviewRes
     * @throws BaseException
     */
    @Transactional
    public PostReviewRes createSellerReviewInfo(Integer userNo, Integer postingNo, PostReviewReq parameters) throws BaseException {
        
        // TODO jwt 인증
        
        // TODO 중복 검사

        UsersInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(postingNo);

        MannerScoreInfo mannerScoreInfo = new MannerScoreInfo(usersInfo, parameters.getTakeManner());
        SellerReviewInfo sellerReviewInfo = new SellerReviewInfo(parameters.getContent(), sellPostingInfo);

        try {
            mannerScoreRepository.save(mannerScoreInfo);
            sellerReviewInfo = sellerReviewRepository.save(sellerReviewInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }

        // 4. UserInfoLoginRes로 변환하여 return
        return new PostReviewRes(sellerReviewInfo.getReviewNo(), sellerReviewInfo.getContent(),
                sellerReviewInfo.getStatus(), sellerReviewInfo.getSellPostingInfo());
    }

    /**
     * 구매자 후기 등록
     * @param parameters
     * @return PostSellerReviewRes
     * @throws BaseException
     */
    @Transactional
    public PostReviewRes createBuyerReviewInfo(Integer userNo, Integer postingNo, PostReviewReq parameters) throws BaseException {

        // TODO jwt 인증

        // TODO 중복 검사

        UsersInfo usersInfo = userInfoProvider.retrieveUserInfoByUserNO(userNo);
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(postingNo);

        MannerScoreInfo mannerScoreInfo = new MannerScoreInfo(usersInfo, parameters.getTakeManner());
        BuyerReviewInfo buyerReviewInfo = new BuyerReviewInfo(parameters.getContent(), sellPostingInfo);

        try {
            mannerScoreRepository.save(mannerScoreInfo);
            buyerReviewInfo = buyerReviewRepository.save(buyerReviewInfo);
        } catch (Exception exception) {
            //exception.printStackTrace();    // 에러 이유 추척
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }

        // 4. UserInfoLoginRes로 변환하여 return
        return new PostReviewRes(buyerReviewInfo.getReviewNo(), buyerReviewInfo.getContent(),
                buyerReviewInfo.getStatus(), buyerReviewInfo.getSellPostingInfo());
    }

    /**
     * 판매자 후기 삭제
     * @param potingNo, userNo
     * @throws BaseException
     */
    @Transactional
    public void deleteSellerReview(Integer potingNo, Integer userNo) throws BaseException {
        // TODO JWT 인증

        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(potingNo);
        SellerReviewInfo sellerReviewInfo = reviewProvider.retrieveSellerReviewByPostingInfoAndSeller(sellPostingInfo, userNo);
        try {
            sellerReviewRepository.delete(sellerReviewInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_REVIEW);
        }
    }

    /**
     * 구매자 후기 삭제
     * @param potingNo, userNo
     * @throws BaseException
     */
    @Transactional
    public void deleteBuyerReview(Integer potingNo, Integer userNo) throws BaseException {
        // TODO JWT 인증

        // 1. 존재하는 UserInfo가 있는지 확인 후 저장
        SellPostingInfo sellPostingInfo = usedStoreProvider.retrieveSellPostingInfoByPostingNo(potingNo);
        BuyerReviewInfo buyerReviewInfo = reviewProvider.retrieveBuyerReviewByPostingInfoAndBuyer(sellPostingInfo, userNo);

        System.out.println();
        try {
            buyerReviewRepository.delete(buyerReviewInfo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_REVIEW);
        }
    }
}
