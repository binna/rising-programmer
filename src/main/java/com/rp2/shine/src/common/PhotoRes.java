package com.rp2.shine.src.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhotoRes {
    private Integer photoNo;
    private String filePath;
    private String fileName;
    private String status;
}