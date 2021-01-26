package com.rp2.shine.src.user.models;

import lombok.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final Integer userNo;
    private final String nickname;
    private final String phoneNumber;
    private final String profilePath;
    private final String profileName;
    private final LocalDate createDate;
    private final LocalDate updateDate;
    private final String status;
}
