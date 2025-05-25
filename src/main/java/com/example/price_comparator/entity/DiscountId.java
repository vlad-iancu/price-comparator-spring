package com.example.price_comparator.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DiscountId implements Serializable {

    @Column(name = "product_id")
    private Long product;

    @Column(name = "store_id")
    private Long store;

    @Column(name = "from_date")
    private LocalDate fromDate;

    public DiscountId() {}

    public DiscountId(Long product, Long store) {
        this.product = product;
        this.store = store;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getStore() {
        return store;
    }

    public void setStore(Long store) {
        this.store = store;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    // equals and hashCode based on product and store
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountId)) return false;
        DiscountId that = (DiscountId) o;
        return Objects.equals(product, that.product) && Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, store);
    }
}