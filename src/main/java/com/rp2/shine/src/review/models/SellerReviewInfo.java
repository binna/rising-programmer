package com.rp2.shine.src.review.models;

import com.rp2.shine.src.usedstore.models.SellPostingInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "SellerReview")
public class SellerReviewInfo {
    @Id
    @Column(name = "reviewNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewNo;

    @ManyToOne
    @JoinColumn(name = "postingNo")
    private SellPostingInfo sellPostingInfo;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "seller", nullable = false)
    private Integer seller;

    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    public SellerReviewInfo(String content, SellPostingInfo sellPostingInfo) {
        this.content = content;
        this.sellPostingInfo = sellPostingInfo;
        this.seller = sellPostingInfo.getSellerUserNo().getUserNo();
    }
}
