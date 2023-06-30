package com.xjdzy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查看是否点赞、收藏所需的数据传送对象
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LACStatusDTO {
    private boolean like;
    private boolean collection;
}
