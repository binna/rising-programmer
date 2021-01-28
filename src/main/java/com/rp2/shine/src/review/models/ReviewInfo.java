package com.rp2.shine.src.review.models;

import com.rp2.shine.src.usedtransactions.models.SellPostingInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "Review")
public class ReviewInfo {
    @Id
    @Column(name = "reviewNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewNo;

    @ManyToOne
    @JoinColumn(name = "postingNo")
    private SellPostingInfo sellPostingInfo;

    @Column(name = "writer", nullable = false)
    private Integer writer;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "division", nullable = false, length = 1)
    private String division;

    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    @Column(name = "fileName", length = 100)
    private String fileName;
    @Column(name = "filePath", length = 100)
    private String filePath;

    @CreationTimestamp
    @Column(name = "createDate", nullable = false, updatable = false)
    private Date createDate;

    public ReviewInfo(String content, SellPostingInfo sellPostingInfo, String division, String fileName, String filePath) {
        this.content = content;
        this.sellPostingInfo = sellPostingInfo;
        this.division = division;
        this.fileName = fileName;
        this.filePath = filePath;

        this.writer = sellPostingInfo.getSellerUserNo().getUserNo();
    }
}
