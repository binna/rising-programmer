package com.rp2.shine.src.usedtransactions.models;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "SellPostingPhoto")
@ToString(exclude = {"sellPostingInfo"})
public class SellPostingPhotoInfo {
    @Id
    @Column(name = "photoNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoNo;

    // 양방향 매핑에서 꼭 알아야 하는 것들 : 도우미 클래스, 도우미 메서드, 도움메서드
    @ManyToOne
    @JoinColumn(name = "postingNo")
    private SellPostingInfo sellPostingInfo;

    @Column(name = "filePath", nullable = false, length = 100)
    private String filePath;

    @Column(name = "fileName", nullable = false, length = 100)
    private String fileName;

    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    public SellPostingPhotoInfo(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }
}