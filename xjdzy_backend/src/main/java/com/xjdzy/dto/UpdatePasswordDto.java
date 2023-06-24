package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
        private Integer userId;
        private String password;
        private String newPassword;

}
