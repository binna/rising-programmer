package com.rp2.shine.src.user.models;

import com.rp2.shine.src.review.models.GetReviewRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetDetailRes {
    // UserInfo==============================
    private final Integer userNo;
    private final String nickname;
    private final String phoneNumber;
    private final String profilePath;
    private final String profileName;
    private final Date createDate;
    private final Date updateDate;
    private final String status;

    // 판매상품================================
    private final Integer sellPostingCnt;
    
    // 받은 매너 평가 ==========================
    private final Double temperature;
    private final HashMap<String, Integer> mannerScore;

    // 받은 거래 후기 ==========================
    private final List<GetReviewRes> review;
}
