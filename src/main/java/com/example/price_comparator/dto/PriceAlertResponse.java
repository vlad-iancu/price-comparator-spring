package com.example.price_comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceAlertResponse {
    private Long id;
    private String productName;
    private Double targetPrice;
    public static PriceAlertResponse fromEntity(com.example.price_comparator.entity.PriceAlert entity) {
        return new PriceAlertResponse(
            entity.getId(),
            entity.getProductName(),
            entity.getTargetPrice()
        );
    }
}
