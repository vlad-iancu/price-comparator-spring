package com.example.price_comparator.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.price_comparator.entity.User;
import com.example.price_comparator.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
    
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        
        return userRepository.save(user);
    }
}
