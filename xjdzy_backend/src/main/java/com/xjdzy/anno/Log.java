package com.xjdzy.anno;

import java.lang.annotation.*;

@Target({ElementType.METHOD})        //针对方法生效
@Retention(RetentionPolicy.RUNTIME)  //运行时生效
public @interface Log {
}
