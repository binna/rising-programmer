package com.rp2.shine.src.review;

import com.rp2.shine.config.BaseException;
import com.rp2.shine.src.review.models.ReviewInfo;
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
    public List<ReviewInfo> retrieveReviewALL(Integer userNo) throws BaseException {
        List<ReviewInfo> reviewInfoList;

        try {
            reviewInfoList = reviewRepository.findByWriterAndStatusOrderByCreateDateDesc(userNo, "Y");
        } catch (Exception ignored) {
            throw new BaseException(FAILED_TO_GET_REVIEW);
        }

        return reviewInfoList;
    }
}
