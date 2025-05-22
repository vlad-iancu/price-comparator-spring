package com.example.price_comparator.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "discount")
public class Discount {

    @EmbeddedId
    private ProductStoreId id;

    @Column(name = "percentage", nullable = false)
    @NonNull
    private Double discountPercentage;

    @Column(name = "from_date", nullable = false)
    @NonNull
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    @NonNull
    private LocalDate toDate;
    // other fields...
}
