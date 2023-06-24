package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取用户关注数、粉丝数、获赞与收藏数功能所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoFoLiCoNumDto {
    private int following;
    private int followers;
    private int likesNum;
    private int collectionNum;
}
