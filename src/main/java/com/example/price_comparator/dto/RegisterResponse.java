package com.example.price_comparator.dto;

import lombok.Getter;

@Getter
public class RegisterResponse  {
    private final String username;
    public RegisterResponse(String username) {
        this.username = username;
    }
    
}
