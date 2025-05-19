package com.example.price_comparator.dto;

import lombok.Getter;

@Getter
public class RegisterResponse  {
    private final Long id;
    private final String username;
    public RegisterResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
    
}
