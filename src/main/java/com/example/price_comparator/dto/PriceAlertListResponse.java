package com.example.price_comparator.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceAlertListResponse {
    private List<PriceAlertResponse> alerts;
}
