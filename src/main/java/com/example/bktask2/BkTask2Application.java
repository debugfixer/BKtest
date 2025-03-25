package com.example.bktask2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.bktask2") // Укажите базовый пакет вашего приложения
public class BkTask2Application {
	public static void main(String[] args) {
		SpringApplication.run(BkTask2Application.class, args);
	}

}
