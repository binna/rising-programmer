package com.rp2.shine.src.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
public class PostLoginReq {
    private String phoneNumber;
}
