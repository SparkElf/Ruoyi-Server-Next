package com.ruoyi;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private int age;
    private boolean young;

    // getter、setter、toString、equals、hashCode
}