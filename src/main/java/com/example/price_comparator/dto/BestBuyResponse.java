package com.example.price_comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BestBuyResponse {
    private String productName;
    private String storeName;
    private String brand;
    private Double price;
}
