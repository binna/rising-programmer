package com.rp2.shine.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final Integer userNo;
    private final String nickname;
    private final String phoneNumber;
    private final String profilePath;
    private final String profileName;
    private final Date createDate;
    private final Date updateDate;
    private final String status;
}