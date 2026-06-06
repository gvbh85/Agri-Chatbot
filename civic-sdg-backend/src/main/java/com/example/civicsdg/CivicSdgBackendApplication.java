package com.example.civicsdg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.civicsdg.model.User;
import com.example.civicsdg.repository.UserRepository;

@SpringBootApplication
public class CivicSdgBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CivicSdgBackendApplication.class, args);
    }

    // Only create admin user on startup
    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("vungalalli@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("vungalalli@gmail.com");
                admin.setPassword(passwordEncoder.encode("12345")); // use existing passwordEncoder bean
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Admin user created successfully!");
            } else {
                System.out.println("ℹ️ Admin user already exists.");
            }
        };
    }
}
