package com.rp2.shine.src.usedtransactions.models;

import com.rp2.shine.src.common.PhotoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUsedTransactionsRes {
    private final int postingNo;
    private final int sellerUserNo;
    private final String title;
    private final String content;
    private final String category;
    private final Integer price;
    private final Integer clickCnt;
    private final Integer postAgain;
    private final Date createDate;
    private final Date updateDate;
    private final String status;
    private final Integer concernUserNo;
    private final List<PhotoRes> postingPhoto;
}