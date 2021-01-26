package com.rp2.shine.src.user.models;

import com.rp2.shine.src.user.models.UsersInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "MannerScore")
public class MannerScoreInfo {
    @Id
    @Column(name = "mannerNo", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mannerNo;

    @OneToOne
    @JoinColumn(name = "userNo", referencedColumnName = "userNo")
    private UsersInfo userNo;

    @Column(name = "takeManner", nullable = false)
    private Integer takeManner;

    public MannerScoreInfo(UsersInfo usersInfo, Integer takeManner) {
        this.userNo = usersInfo;
        this.takeManner = takeManner;
    }
}
