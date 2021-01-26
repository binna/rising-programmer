package com.rp2.shine.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private final Integer userNo;
    private final String nickname;
    private final String profileName;
    private final String profilePath;
    private final String email;
    private final String phoneNumber;

}