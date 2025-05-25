package com.example.price_comparator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListDeleteRequest {
    @NotNull
    private Long shoppingListId;
}
