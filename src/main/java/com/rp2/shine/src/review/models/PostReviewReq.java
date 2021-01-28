package com.rp2.shine.src.review.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostReviewReq {
    private String content;
    private Integer takeManner;
    private String filePath;
    private String fileName;
}