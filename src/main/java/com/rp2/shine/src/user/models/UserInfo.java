// name : 필드와 매핑할 테이블의 컬럼 이름
// insertable : 엔티티 저장시 이 필드도 같이 저장한다. false로 설정하면 데이터베이스에 저장하지 않는다. 읽기 전용일때 사용한다.
// updatable : 위와 동일한 하지만 수정일때 해당 된다.
// nullable(DDL) : null 값 허용 여부를 설정한다. false일 경우 DDL생성시 not null 제약조건이 된다.
// unique(DDL) : 한 컬럼에 간단히 유니크 제약 조건을 걸 때 사용한다. 만약 두개 이상 걸고 싶다면 클래스 레벨에서 @Table.uniqueConstraints를 사용해야 한다.
// columnDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 줄 수 있다.
// length : 문자 길이 제약 조건이다. String만 해당된다.
// precision, scale(DDL) : BigDecimal 타입에서 사용한다.(BigInteger 가능) precision은 소수점을 포함한 전체 자리수이고, scale은 소수점 자릿수이다. double랑 float타입에는 적용 되지 않는다.
package com.rp2.shine.src.user.models;

import com.rp2.shine.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@ToString(exclude = {"sellPostingInfo"})
@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@EqualsAndHashCode(callSuper = false)
@Data                   // from lombok
@Entity                 // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "User")   // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class UserInfo extends BaseEntity {
    @Id                 // PK를 의미하는 어노테이션
    @Column(name = "userNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 기본키 생성을 데이터베이스에 위임 즉, id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    private Integer userNo;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "phoneNumber", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "profilePath", length = 100)
    private String profilePath;
    @Column(name = "profileName", length = 100)
    private String profileName;

    @Column(name = "withdrawalReason", length = 1)
    private String withdrawalReason;

    @Column(name = "email", length = 50)
    private String email;

    public UserInfo(String nickname, String phoneNumber, String profilePath, String profileName) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.profilePath = profilePath;
        this.profileName = profileName;
    }
}