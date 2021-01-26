package com.rp2.shine.src.usedstore.models;

import com.rp2.shine.config.BaseEntity;
import com.rp2.shine.src.user.models.UsersInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data                   // from lombok
@Entity                 // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "SellPostingConcern")   // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class SellPostingConsernInfo {

    /**
     * 관심번호
     */
    @Id                 // PK를 의미하는 어노테이션
    @Column(name = "concernNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concernNo;

    /**
     * 누른사람
     */
    @OneToOne
    @JoinColumn(name = "concernUserNo", referencedColumnName = "userNo")
    private UsersInfo concernUserNo;

    /**
     * 관심번호
     */
    @OneToOne
    @JoinColumn(name = "postingNo", referencedColumnName = "postingNo")
    private SellPostingInfo postingNo;

    /**
     * 상태
     */
    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    public SellPostingConsernInfo(UsersInfo concernUserNo, SellPostingInfo postingNo) {
        this.concernUserNo = concernUserNo;
        this.postingNo = postingNo;
    }
}