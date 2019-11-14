package com.a.platform.buyer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@EnableDubbo
@SpringBootApplication
@ComponentScan("com.a.platform")
@ServletComponentScan
public class ApiBuyerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBuyerApplication.class, args);
	}
}
