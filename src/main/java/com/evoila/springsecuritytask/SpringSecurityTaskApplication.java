package com.evoila.springsecuritytask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityTaskApplication.class, args);

//	@Bean
//	CommandLineRunner commandLineRunner(UserRepository users, PasswordEncoder encoder) {
//		return args -> {
//			users.save(new User("user", "user@email.com", encoder.encode("user123")));
//		};
//	}
	}
}
