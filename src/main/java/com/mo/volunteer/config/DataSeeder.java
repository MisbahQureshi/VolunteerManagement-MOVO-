package com.mo.volunteer.config;

import com.mo.volunteer.entity.User;
import com.mo.volunteer.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String adminUsername = "admin";

            if (!userRepository.existsByUsername(adminUsername)) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPasswordHash(encoder.encode("admin12345")); // change this
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }
        };
    }
}
