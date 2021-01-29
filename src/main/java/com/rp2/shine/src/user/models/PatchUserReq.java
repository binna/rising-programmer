package com.rp2.shine.src.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PatchUserReq {
    private String nickname;
    private String phoneNumber;
    private String email;
    private String profilePath;
    private String profileName;
}