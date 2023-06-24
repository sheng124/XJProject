package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNameDto {
    private Integer userId;
    private String username;
    private String newUserName;
}
