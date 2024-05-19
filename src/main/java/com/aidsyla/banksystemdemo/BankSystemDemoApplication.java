package com.aidsyla.banksystemdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aidsyla.banksystemdemo")
public class BankSystemDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemDemoApplication.class, args);
	}

}
