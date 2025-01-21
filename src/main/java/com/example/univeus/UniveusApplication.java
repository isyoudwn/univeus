package com.example.univeus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@SpringBootApplication
public class UniveusApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniveusApplication.class, args);
	}

}
