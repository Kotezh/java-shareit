package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("ru.practicum.shareit")
@EnableJpaRepositories("ru.practicum.shareit")
@EntityScan("ru.practicum.shareit")
public class ShareItApp {

	public static void main(String[] args) {
		try {
			SpringApplication.run(ShareItApp.class, args);
		} catch (Exception e) {
			e.printStackTrace(); // Выведет все скрытые исключения
			throw e;
		}
	}

}
