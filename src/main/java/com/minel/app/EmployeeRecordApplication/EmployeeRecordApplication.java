package com.minel.app.EmployeeRecordApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeRecordApplication.class, args);
	}

}
