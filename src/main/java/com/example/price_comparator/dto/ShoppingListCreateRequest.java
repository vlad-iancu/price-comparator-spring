package com.example.price_comparator.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListCreateRequest {
    @NotEmpty
    private List<Long> productIds;

    @NotBlank
    private String name; // Optional name for the shopping list
}
