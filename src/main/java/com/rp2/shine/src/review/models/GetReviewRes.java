package com.rp2.shine.src.review.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GetReviewRes {
    private final Integer userNo;
    private final String fileName;
    private final String filePath;
    private final String Content;
    private final Date createDate;
}
