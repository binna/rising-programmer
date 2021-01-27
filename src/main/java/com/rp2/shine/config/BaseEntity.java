package com.rp2.shine.config;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)      // DB insert/update 시에 자동으로 하게끔 해준다 -> 얘랑 짝궁으로 application에도 해줘야 한다.
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(name = "createDate", nullable = false, updatable = false)
    private Date createDate;

    @CreationTimestamp
    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "status", length = 1)
    private String status = "Y";
}