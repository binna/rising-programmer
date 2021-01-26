package com.rp2.shine.src.usedstore.models;

import com.rp2.shine.config.BaseEntity;
import com.rp2.shine.src.user.models.UsersInfo;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data                   // from lombok
@Entity                 // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "SellPosting")   // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class SellPostingInfo extends BaseEntity {

    /**
     * 판매글 번호
     */
    @Id                 // PK를 의미하는 어노테이션
    @Column(name = "postingNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 기본키 생성을 데이터베이스에 위임 즉, id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    private Integer postingNo;

    /**
     * 판매자
     */
    //@Column(name = "sellerUserNo", nullable = false)
    //private int sellerUserNo;
    @OneToOne
    @JoinColumn(name = "sellerUserNo", referencedColumnName = "userNo")
    private UsersInfo sellerUserNo;

    /**
     * 제목
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 내용
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 카테고리
     */
    @Column(name = "category", length = 15, nullable = false)
    private String category;

    /**
     * 조회
     */
    @Column(name = "clickCnt")
    private int clickCnt;

    /**
     * 끌올
     */
    @Column(name = "postAgain")
    private int postAgain;

    @Column(name="price", nullable = false)
    private Integer price;

    /**
     * 구매자
     */
    //@Column(name = "buyerUserNo")
    //private int buyerUserNo;
    @OneToOne
    @JoinColumn(name = "buyerUserNo", referencedColumnName = "userNo")
    private UsersInfo buyerUserNo;

    /**
     * SellPostingPhoto 테이블과 연결
     */
    @OneToMany(mappedBy = "sellPostingInfo", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SellPostingPhotoInfo> sellPostingPhotoInfoList;

    public void setSellPostingPhotoInfo(List<SellPostingPhotoInfo> sellPostingPhotoInfoList) {
        this.sellPostingPhotoInfoList = sellPostingPhotoInfoList;

        for (SellPostingPhotoInfo sellPostingPhotoInfo : sellPostingPhotoInfoList) {
            sellPostingPhotoInfo.setSellPostingInfo(this);
        }
    }

    public SellPostingInfo(UsersInfo sellerUserNo, String title, String content, String category, Integer price) {
        this.sellerUserNo = sellerUserNo;
        this.title = title;
        this.content = content;
        this.category = category;
        this.price = price;
    }
}
