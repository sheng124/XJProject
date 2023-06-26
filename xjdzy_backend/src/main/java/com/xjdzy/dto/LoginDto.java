package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String token;
    private Integer userId;
}
