package com.java.everis.msfixedterm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsFixedTermApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFixedTermApplication.class, args);
	}

}
