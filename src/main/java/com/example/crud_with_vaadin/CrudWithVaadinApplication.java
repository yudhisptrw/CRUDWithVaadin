package com.example.crud_with_vaadin;

import com.example.crud_with_vaadin.backend.entities.AppUser;
import com.example.crud_with_vaadin.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrudWithVaadinApplication {

	private static final Logger log = LoggerFactory.getLogger(CrudWithVaadinApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CrudWithVaadinApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository repository) {
		return (args) -> {
			repository.save(new AppUser("John", "Mayer", "California, CA", "Dev"));
			repository.save(new AppUser("Herman", "Li", "Massachusetts, MA", "Dev"));
			repository.save(new AppUser("Judika", "Situmorang", "Jakarta, Indonesia", "Dev"));

			// fetch all user
			log.info("User found with findAll():");
			log.info("-------------------------------");
			for (AppUser appUser : repository.findAll()) {
				log.info(appUser.toString());
			}
			log.info("");

		};

	}
}
