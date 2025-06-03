package com.learn.sb_2_chunk_proecssing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.learn.*"})
@SpringBootApplication
public class Sb2ChunkProecssingApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sb2ChunkProecssingApplication.class, args);
	}

}
