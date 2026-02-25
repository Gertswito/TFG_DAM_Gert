package com.gert.tfgdam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TfgdamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TfgdamApplication.class, args);
	}

}
