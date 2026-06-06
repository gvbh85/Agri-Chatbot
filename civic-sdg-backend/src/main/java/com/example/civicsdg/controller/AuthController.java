package com.example.civicsdg.controller;

import com.example.civicsdg.model.User;
import com.example.civicsdg.repository.UserRepository;
import com.example.civicsdg.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email already exists"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Registered successfully")
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Optional<User> optionalUser =
                userRepository.findByEmail(user.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        User dbUser = optionalUser.get();

        if (!passwordEncoder.matches(
                user.getPassword(),
                dbUser.getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        String token = jwtUtil.generateToken(
                dbUser.getEmail(),
                dbUser.getRole()
        );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", dbUser.getRole(),
                        "email", dbUser.getEmail()
                )
        );
    }

    // ================= CREATE ADMIN =================
    // Call once: http://localhost:8080/api/auth/create-admin
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin() {

        String adminEmail = "vungalalli@gmail.com";

        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Admin already exists"));
        }

        User admin = new User();
        admin.setName("Admin");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("12345"));
        admin.setRole("ADMIN");

        userRepository.save(admin);

        return ResponseEntity.ok(
                Map.of("message", "Admin created successfully")
        );
    }
}
