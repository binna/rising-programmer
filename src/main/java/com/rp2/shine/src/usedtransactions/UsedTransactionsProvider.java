package com.rp2.shine.src.usedtransactions;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.usedtransactions.models.SellPostingConsernInfo;
import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UsedTransactionsProvider {
    private final PostingInfoRepository sellPostingInfoRepository;
    private final PostConcernRepository postConcernsRepository;

    /**
     * 판매글 조회
     * @param sellPostingNo
     * @return SellPostingInfo
     * @throws BaseException
     */
    public SellPostingInfo retrieveSellPostingInfoByPostingNo(Integer sellPostingNo) throws BaseException {
        SellPostingInfo sellPostingInfo;

        try {
            sellPostingInfo = sellPostingInfoRepository.findById(sellPostingNo)
                    .orElseThrow(() -> new BaseException(EMPTY_USEDSTORE));
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_USER);
        }

        return sellPostingInfo;
    }

    /**
     * 판매글 조회
     * @param sellerUserNo
     * @return List<SellPostingInfo>
     * @throws BaseException
     */
    public List<SellPostingInfo> retrieveSellPostingInfoBySellerUserNo(UserInfo sellerUserNo) throws BaseException {
        List<SellPostingInfo> sellPostingInfoList;

        try {
            sellPostingInfoList = sellPostingInfoRepository.findBySellerUserNoAndStatus(sellerUserNo, "Y");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_POSTING);
        }

        return sellPostingInfoList;
    }

    /**
     * 관심 등록 조회
     * @param concernUserNo
     * @return SellPostingConsernInfo
     * @throws BaseException
     */
    public SellPostingConsernInfo retrieveSellPostingConsernInfoByConcernUserNo(UserInfo concernUserNo, SellPostingInfo sellPostingInfo) throws BaseException {
        List<SellPostingConsernInfo> existsConsernInfoList;
        try {
            existsConsernInfoList = postConcernsRepository.findByConcernUserNoAndPostingNo(concernUserNo, sellPostingInfo);
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_CONSERN);
        }

        SellPostingConsernInfo sellPostingConsernInfo;
        if (existsConsernInfoList != null && existsConsernInfoList.size() > 0) {
            sellPostingConsernInfo = existsConsernInfoList.get(0);
        } else {
            throw new BaseException(EMPTY_USEDSTORE);
        }

        return sellPostingConsernInfo;
    }

}
