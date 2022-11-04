package com.evoila.springsecuritytask;


import com.evoila.springsecuritytask.model.User;
import com.evoila.springsecuritytask.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class SpringSecurityTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityTaskApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
			return args -> {
				if(users.count() == 0)
					users.save(new User("user", "user@email.com", encoder.encode("user123")));
			};
	}
}
