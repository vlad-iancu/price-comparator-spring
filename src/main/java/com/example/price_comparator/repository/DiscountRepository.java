package com.example.price_comparator.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.price_comparator.entity.Discount;
import com.example.price_comparator.entity.ProductStoreId;

public interface DiscountRepository extends JpaRepository<Discount, ProductStoreId> {

    // Custom query methods can be defined here if needed
    // Example: Find all discounts for a specific product
    List<Discount> findByProductId(ProductStoreId productId);
    
    // Example: Find all discounts for a specific store
    List<Discount> findByStoreId(Long storeId);
    
    // Example: Find all discounts within a specific date range
    List<Discount> findByFromDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Example: Find all discounts by percentage
    List<Discount> findByDiscountPercentage(Double discountPercentage);
    
}
