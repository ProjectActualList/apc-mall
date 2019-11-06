package com.a.platform.base;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@ComponentScan("com.a.platform")
public class SvcBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvcBaseApplication.class, args);
    }
}
