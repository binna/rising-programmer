package com.rp2.shine.src.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class PostReviewRes {
    private final String division;
    private final Integer reviewNo;
    private final Integer writer;
    private final String content;
    private final String status;
    private final Date createDate;
    private final String fileName;
    private final String filePath;
    private final Integer takeManner;
    private final Integer mannerTargetUserNo;
}