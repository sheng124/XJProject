package com.xjdzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLogInfo {
    @TableId(type = IdType.AUTO)
    private Integer logId;                 //ID

    private int userId;                //操作人员ID
    private LocalDateTime operateTime; //操作时间
    private String className;          //操作类名
    private String methodName;         //操作方法名
    private String methodParams;       //操作方法参数
    private String returnValue;        //操作返回值
    private Long costTime;             //操作执行耗时
}
