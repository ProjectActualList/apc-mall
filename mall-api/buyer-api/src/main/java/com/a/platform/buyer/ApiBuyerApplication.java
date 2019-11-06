package com.a.platform.buyer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

@EnableDubbo
@SpringBootApplication
public class ApiBuyerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBuyerApplication.class, args);
	}
}
