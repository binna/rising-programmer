package com.rp2.shine.src.user.models;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class PostLoginReq {
    private String phoneNumber;
}