package com.example.price_comparator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.price_comparator.dto.RegisterRequest;
import com.example.price_comparator.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest entity) {
        System.out.println("Registering user: " + entity.getUsername());
        userService.register(entity.getUsername(), entity.getPassword());
        System.out.println("User registered successfully: " + entity.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }
    
}
