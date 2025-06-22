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
		User user1 = new User();
		user1.setUsername("mel");
		userRepository.save(user1);

		User user2 = new User();
		user2.setUsername("ana");
		userRepository.save(user2);

		User user3 = new User();
		user3.setUsername("joao");
		userRepository.save(user3);

		User user4 = new User();
		user4.setUsername("bia");
		userRepository.save(user4);
	}

}
