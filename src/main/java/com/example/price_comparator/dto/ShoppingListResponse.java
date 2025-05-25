package com.example.price_comparator.dto;

import java.util.List;

import com.example.price_comparator.entity.ShoppingList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingListResponse {
    private Long id;
    private String name;
    private List<ProductResponse> products;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ProductResponse {
        private Long id;
        private String name;
    }

    public static ShoppingListResponse fromEntity(ShoppingList shoppingList) {
        List<ProductResponse> productResponses = shoppingList.getProducts().stream()
            .map(product -> new ProductResponse(product.getId(), product.getProductName()))
            .toList();
        return new ShoppingListResponse(shoppingList.getId(), shoppingList.getName(), productResponses);
    }
}
