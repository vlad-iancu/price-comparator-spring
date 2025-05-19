package com.example.price_comparator.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.price_comparator.entity.User;
import com.example.price_comparator.exception.types.IncorrectPasswordException;
import com.example.price_comparator.exception.types.UserAlreadyExistsException;
import com.example.price_comparator.exception.types.UserNotFoundException;
import com.example.price_comparator.repository.UserRepository;
import com.example.price_comparator.security.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException(username);
        }
    
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);
        
        return userRepository.save(user);
    }

    public String loginUser(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException(username);
        }
        User user = userRepository.findByUsername(username).get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException(username);
        }
        return JWTUtils.generateToken(user.getId());
    }

}
