package com.a.platform.system;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@ComponentScan("com.a.platform")
public class SvcSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvcSystemApplication.class, args);
    }
}
