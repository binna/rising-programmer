package com.rp2.shine.src.usedstore.models;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data                   // from lombok
@Entity                 // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "SellPostingPhoto")   // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
@ToString(exclude = {"sellPostingInfo"})
public class SellPostingPhotoInfo {

    /**
     * 사진 번호
     */
    @Id                 // PK를 의미하는 어노테이션
    @Column(name = "photoNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 기본키 생성을 데이터베이스에 위임 즉, id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    private Integer photoNo;

    // 양방향 매핑에서 꼭 알아야 하는 것들 : 도우미 클래스, 도우미 메서드, 도움메서드
    /**
     * 판매글 번호
     */
    //@Column(name = "postingNo", nullable = false)
    //private int postingNo;
    @ManyToOne
    @JoinColumn(name = "postingNo")
    private SellPostingInfo sellPostingInfo;

    /**
     * 파일위치
     */
    @Column(name = "filePath", nullable = false, length = 100)
    private String filePath;

    /**
     * 사진이름
     */
    @Column(name = "fileName", nullable = false, length = 100)
    private String fileName;

    /**
     * 상태
     */
    @Column(name = "status", nullable = false, length = 1)
    private String status = "Y";

    public SellPostingPhotoInfo(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }
}