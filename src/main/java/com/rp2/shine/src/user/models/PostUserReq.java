package com.rp2.shine.src.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserReq {
    private String nickname;
    private String phoneNumber;
    private String profilePath;
    private String profileName;
}