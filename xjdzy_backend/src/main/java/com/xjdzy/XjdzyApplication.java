package com.xjdzy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com/xjdzy/mapper")
public class XjdzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(XjdzyApplication.class, args);
    }

}
