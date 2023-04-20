package com.configure.kafka.producer.consumer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
   //http://localhost:8094/api/v1/kafka/publish?message=hello world
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
