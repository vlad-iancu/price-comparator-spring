package com.example.price_comparator.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatsResponse {
    private Long productId;
    private String productName;
    private List<DataPoint> dataPoints;

    @Getter
    @Setter
    public class DataPoint {
        private LocalDate date;
        private Double price;
    }
}
