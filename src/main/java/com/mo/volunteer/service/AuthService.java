package com.mo.volunteer.service;

import com.mo.volunteer.dto.RegisterRequest;
import com.mo.volunteer.entity.User;
import com.mo.volunteer.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest req){
        if(userRepository.existsByUsername(req.getUsername())){
            throw new IllegalArgumentException("Username already exists.");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
    }
}
