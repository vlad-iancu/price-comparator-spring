package com.example.price_comparator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.price_comparator.dto.ApiResponse;
import com.example.price_comparator.dto.LoginRequest;
import com.example.price_comparator.dto.LoginResponse;
import com.example.price_comparator.dto.RegisterRequest;
import com.example.price_comparator.dto.RegisterResponse;
import com.example.price_comparator.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest entity) {
        //System.out.println("Registering user: " + entity.getUsername());
        var user = userService.register(entity.getUsername(), entity.getPassword());
        //System.out.println("User registered successfully: " + entity.getUsername());
        var response = new RegisterResponse(user.getId(), user.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest entity) {
        //System.out.println("Logging in user: " + entity.getUsername());
        var jwt = userService.loginUser(entity.getUsername(), entity.getPassword());
        System.out.println("User logged in successfully: " + jwt);
        var response = new LoginResponse(jwt);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }
}
