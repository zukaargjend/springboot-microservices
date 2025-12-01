package com.project.fileprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FileprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileprocessorApplication.class, args);
	}

}
