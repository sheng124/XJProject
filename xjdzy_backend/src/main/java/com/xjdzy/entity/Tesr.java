package com.xjdzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tesr {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private int ainfo;
    private int binfo;
}
