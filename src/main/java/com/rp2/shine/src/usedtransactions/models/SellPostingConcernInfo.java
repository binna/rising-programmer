package com.rp2.shine.src.usedtransactions.models;

import com.rp2.shine.src.user.models.UserInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "SellPostingConcern")
public class SellPostingConcernInfo {
    @Id
    @Column(name = "concernNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concernNo;

    @OneToOne
    @JoinColumn(name = "concernUserNo", referencedColumnName = "userNo")
    private UserInfo concernUserNo;

    @OneToOne
    @JoinColumn(name = "postingNo", referencedColumnName = "postingNo")
    private SellPostingInfo postingNo;

    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    public SellPostingConcernInfo(UserInfo concernUserNo, SellPostingInfo postingNo) {
        this.concernUserNo = concernUserNo;
        this.postingNo = postingNo;
    }
}