package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.usedtransactions.models.SellPostingConcernInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UsedTransactionProvider {
    private final PostingInfoRepository postingInfoRepository;
    private final PostConcernRepository postConcernsRepository;

    /**
     * 중고거래 글 조회
     * @param postingNo
     * @return SellPostingInfo
     * @throws BaseException
     * @comment postingNo로 중고거래 글 조회
     */
    public SellPostingInfo retrievePostingByPostingNo(Integer postingNo) throws BaseException {
        SellPostingInfo sellPostingInfo;

        try {
            sellPostingInfo = postingInfoRepository.findById(postingNo)
                    .orElseThrow(() -> new BaseException(EMPTY_POSTING));
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_POSTING);
        }

        return sellPostingInfo;
    }

    /**
     * 유효한 중고거래 글 조회
     * @param sellerUserNo
     * @return List<SellPostingInfo>
     * @throws BaseException
     * @Comment sellerUserNo로 중고거래 글 조회
     */
    public List<SellPostingInfo> retrievePostingBySellerUserNoAndStatuY(UserInfo sellerUserNo) throws BaseException {
        List<SellPostingInfo> sellPostingInfoList;

        try {
            sellPostingInfoList = postingInfoRepository.findBySellerUserNoAndStatus(sellerUserNo, "Y");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_POSTING);
        }

        return sellPostingInfoList;
    }

    /**
     * 관심 등록 조회
     * @param sellPostingInfo
     * @return List<SellPostingConsernInfo>
     * @throws BaseException
     * @comment postingNo로 관심 등록 조회
     */
    public List<SellPostingConcernInfo> retrievePostingConcernByPostingNo(SellPostingInfo sellPostingInfo) throws BaseException {
        List<SellPostingConcernInfo> sellPostingConsernInfoList;

        try {
            sellPostingConsernInfoList = postConcernsRepository.findByPostingNo(sellPostingInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CONSERN);
        }

        return sellPostingConsernInfoList;
    }

    /**
     * 관심 등록 조회
     * @param concernUserNo
     * @return SellPostingConsernInfo
     * @throws BaseException
     */
    public SellPostingConcernInfo retrievePostingConcernByPostingNo(UserInfo concernUserNo, SellPostingInfo sellPostingInfo) throws BaseException {
        List<SellPostingConcernInfo> existsConsernInfoList;
        try {
            existsConsernInfoList = postConcernsRepository.findByConcernUserNoAndPostingNo(concernUserNo, sellPostingInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CONSERN);
        }

        SellPostingConcernInfo sellPostingConsernInfo;
        if (existsConsernInfoList != null && existsConsernInfoList.size() > 0) {
            sellPostingConsernInfo = existsConsernInfoList.get(0);
        } else {
            throw new BaseException(EMPTY_POSTING);
        }

        return sellPostingConsernInfo;
    }

}
