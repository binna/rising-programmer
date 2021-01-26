package com.rp2.shine.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)      // DB insert/update 시에 자동으로 하게끔 해준다 -> 얘랑 짝궁으로 application에도 해줘야 한다.
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "createDate", nullable = false, updatable = false)
    private LocalDate createDate;

    @LastModifiedDate
    @Column(name = "updateDate")
    private LocalDate updateDate;

    @Column(name = "status", length = 1)
    private String status = "Y";
}