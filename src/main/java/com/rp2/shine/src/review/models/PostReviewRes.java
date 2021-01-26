package com.rp2.shine.src.review.models;

import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostReviewRes {
    private final Integer reviewNo;
    private final String content;
    private final String status;
    private final SellPostingInfo posting;
}