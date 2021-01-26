package com.rp2.shine.src.usedstore.models;

import com.rp2.shine.src.common.PhotoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PatchUsedStoreRes {
    private final int postingNo;
    private final int sellerUserNo;
    private final String title;
    private final String content;
    private final String category;
    private final LocalDate createDate;
    private final LocalDate updateDate;
    private final String status;
    private final Integer price;
    private List<PhotoRes> sellPostingPhoto;
}
