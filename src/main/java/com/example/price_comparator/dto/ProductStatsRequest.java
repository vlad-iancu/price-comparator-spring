package com.example.price_comparator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatsRequest {
    private Long productId;
    private Long storeId;
}
