package com.example.price_comparator.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private DiscountId id;

    @ManyToOne
    @MapsId("product")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @MapsId("store")
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "percentage", nullable = false)
    @NonNull
    private Double discountPercentage;

    @Column(name = "to_date", nullable = false)
    @NonNull
    private LocalDate toDate;
    // other fields...
}
