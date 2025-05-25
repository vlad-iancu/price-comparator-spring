package com.example.price_comparator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceAlertCreateRequest {
    @NotNull
    private Double targetPrice;

    @NotBlank
    private String productName;
}
