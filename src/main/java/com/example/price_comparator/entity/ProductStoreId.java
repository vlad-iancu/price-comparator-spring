package com.example.price_comparator.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductStoreId implements Serializable {

    @Column(name = "product_id")
    private Long product;

    @Column(name = "store_id")
    private Long store;

    public ProductStoreId() {}

    public ProductStoreId(Long product, Long store) {
        this.product = product;
        this.store = store;
    }

    // equals and hashCode based on product and store
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductStoreId)) return false;
        ProductStoreId that = (ProductStoreId) o;
        return Objects.equals(product, that.product) && Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store);
    }
}
