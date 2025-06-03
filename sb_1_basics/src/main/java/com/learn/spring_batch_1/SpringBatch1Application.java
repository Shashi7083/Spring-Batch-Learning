package com.learn.spring_batch_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.learn*"})
@SpringBootApplication
public class SpringBatch1Application {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBatch1Application.class, args);
		System.out.println("Test Application ----");
	}

}
