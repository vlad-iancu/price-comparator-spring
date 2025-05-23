package com.example.price_comparator.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "product_store")
public class ProductStore {
    @EmbeddedId
    private ProductStoreId id;

    @ManyToOne
    @MapsId("product")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("store")
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "package_quantity", nullable = false)
    @NonNull
    private Double packageQuantity;

    @NonNull
    private Double price;

    @jakarta.persistence.OneToOne
    @jakarta.persistence.JoinColumns({
        @jakarta.persistence.JoinColumn(name = "store_id", referencedColumnName = "store_id"),
        @jakarta.persistence.JoinColumn(name = "product_id", referencedColumnName = "product_id")
    })
    @NotFound(action = NotFoundAction.IGNORE)
    private Discount discount;
}
