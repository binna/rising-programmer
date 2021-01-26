package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.BuyerReviewInfo;
import com.rp2.shine.src.review.models.SellerReviewInfo;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import com.rp2.shine.src.usedstore.models.SellPostingPhotoInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rp2.shine.config.BaseResponseStatus.EMPTY_USEDSTORE;
import static com.rp2.shine.config.BaseResponseStatus.FAILED_TO_GET_USER;

@RequiredArgsConstructor
@Service
public class ReviewProvider {
    private final SellerReviewRepository sellerReviewRepository;
    private final BuyerReviewRepository buyerReviewRepository;

    /**
     * 판매자 후기 조회
     *
     * @param sellPostingNo
     * @return SellPostingInfo
     * @throws BaseException
     */
    public SellerReviewInfo retrieveSellerReviewByPostingInfoAndSeller(SellPostingInfo sellPostingInfo, Integer seller) throws BaseException {
        SellerReviewInfo sellerReviewInfo;

        try {
            sellerReviewInfo = sellerReviewRepository.findBySellPostingInfoAndSeller(sellPostingInfo, seller).get(0);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        return sellerReviewInfo;
    }

    /**
     * 구매자 후기 조회
     *
     * @param sellPostingNo
     * @return SellPostingInfo
     * @throws BaseException
     */
    public BuyerReviewInfo retrieveBuyerReviewByPostingInfoAndBuyer(SellPostingInfo sellPostingInfo, Integer buyer) throws BaseException {
        BuyerReviewInfo buyerReviewInfo;

        try {
            buyerReviewInfo = buyerReviewRepository.findBySellPostingInfoAndAndBuyer(sellPostingInfo, buyer).get(0);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }
        return buyerReviewInfo;
    }
}
