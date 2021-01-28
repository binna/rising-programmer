package com.rp2.shine.src.usedtransactions.models;

import com.rp2.shine.src.common.PhotoReq;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PatchUsedTransactionReq {
    private String title;
    private String content;
    private String category;
    private Integer price;
    private List<PhotoReq> postingPhoto;
}
