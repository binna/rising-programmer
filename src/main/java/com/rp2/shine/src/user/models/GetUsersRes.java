package com.rp2.shine.src.user.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final String nickname;
    private final String phoneNumber;
    private final String profilePath;
    private final String profileName;
}
