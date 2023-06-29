package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 获取文章详细信息功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAndUDto {
    private Integer userId;
    private String userAvatar;
    private String userName;
    private int commentId;
    private String comment;
    private LocalDateTime createTime;
}
