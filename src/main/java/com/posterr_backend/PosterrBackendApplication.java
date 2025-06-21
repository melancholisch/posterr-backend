package com.posterr_backend;

import com.posterr_backend.models.User;
import com.posterr_backend.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PosterrBackendApplication {

	@Autowired
	private UserRepository userRepository;


	public static void main(String[] args) {
		SpringApplication.run(PosterrBackendApplication.class, args);
	}

	@PostConstruct
	public void init() {
		User user = new User();
		user.setUsername("mel");
		userRepository.save(user);
	}

}
