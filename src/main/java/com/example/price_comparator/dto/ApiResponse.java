package com.example.price_comparator.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final T data;

    public ApiResponse(T data) {
        this.data = data;
    }
}
