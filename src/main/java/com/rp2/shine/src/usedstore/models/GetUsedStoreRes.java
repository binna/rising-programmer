package com.rp2.shine.src.usedstore.models;

import com.rp2.shine.src.common.PhotoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUsedStoreRes {
    private final int postingNo;
    private final int sellerUserNo;
    private final String title;
    private final String content;
    private final String category;
    private final Integer price;
    private final Integer clickCnt;
    private final Integer postAgain;
    private LocalDate createDate;
    private LocalDate updateDate;
    private String status;
    private int concernUserNo;
    private final List<PhotoRes> photo;
}