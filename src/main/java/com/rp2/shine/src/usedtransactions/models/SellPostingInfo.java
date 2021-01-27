package com.rp2.shine.src.usedtransactions.models;

import com.rp2.shine.config.BaseEntity;
import com.rp2.shine.src.user.models.UserInfo;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "SellPosting")
public class SellPostingInfo extends BaseEntity {
    @Id
    @Column(name = "postingNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postingNo;

    @OneToOne
    @JoinColumn(name = "sellerUserNo", referencedColumnName = "userNo")
    private UserInfo sellerUserNo;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "category", length = 15, nullable = false)
    private String category;

    @Column(name = "clickCnt")
    private int clickCnt;

    @Column(name = "postAgain")
    private int postAgain;

    @Column(name="price", nullable = false)
    private Integer price;

    @OneToOne
    @JoinColumn(name = "buyerUserNo", referencedColumnName = "userNo")
    private UserInfo buyerUserNo;

    @OneToMany(mappedBy = "sellPostingInfo", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SellPostingPhotoInfo> sellPostingPhotoInfoList;

    public void setSellPostingPhotoInfo(List<SellPostingPhotoInfo> sellPostingPhotoInfoList) {
        this.sellPostingPhotoInfoList = sellPostingPhotoInfoList;

        for (SellPostingPhotoInfo sellPostingPhotoInfo : sellPostingPhotoInfoList) {
            sellPostingPhotoInfo.setSellPostingInfo(this);
        }
    }

    public SellPostingInfo(UserInfo sellerUserNo, String title, String content, String category, Integer price) {
        this.sellerUserNo = sellerUserNo;
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
    }
}