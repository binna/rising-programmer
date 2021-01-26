package com.rp2.shine.src.usedstore;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.usedstore.models.SellPostingConsernInfo;
import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import com.rp2.shine.src.user.models.UsersInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rp2.shine.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class UsedStoreProvider {
    private final PostingInfoRepository sellPostingInfoRepository;
    private final PostConcernsRepository postConcernsRepository;

    /**
     * 포스팅 조회
     *
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
     * 관심 등록 조회
     *
     * @param concernUserNo
     * @return SellPostingConsernInfo
     * @throws BaseException
     */
    public SellPostingConsernInfo retrieveSellPostingConsernInfoByConcernUserNo(UsersInfo concernUserNo, SellPostingInfo sellPostingInfo) throws BaseException {
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
