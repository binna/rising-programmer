package com.rp2.shine.src.common;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
@Setter
@ToString
public class PhotoReq {
    private String filePath;
    private String fileName;
}