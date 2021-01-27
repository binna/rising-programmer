package com.rp2.shine.src.usedtransactions.models;

import com.rp2.shine.src.common.PhotoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class PatchUsedTransactionRes {
    private final Integer postingNo;
    private final Integer sellerUserNo;
    private final String title;
    private final String content;
    private final String category;
    private final Date createDate;
    private final Date updateDate;
    private final String status;
    private final Integer price;
    private final List<PhotoRes> postingPhoto;
}
