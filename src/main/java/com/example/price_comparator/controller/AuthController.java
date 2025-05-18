package com.example.price_comparator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.price_comparator.dto.ApiResponse;
import com.example.price_comparator.dto.RegisterRequest;
import com.example.price_comparator.dto.RegisterResponse;
import com.example.price_comparator.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest entity) {
        //System.out.println("Registering user: " + entity.getUsername());
        var user = userService.register(entity.getUsername(), entity.getPassword());
        //System.out.println("User registered successfully: " + entity.getUsername());
        var response = new RegisterResponse(user.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(response));
    }
    
}
