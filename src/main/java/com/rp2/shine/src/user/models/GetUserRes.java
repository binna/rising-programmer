package com.rp2.shine.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final Integer userNo;
    private final String nickname;
    private final String phoneNumber;
}