package com.example.price_comparator.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.price_comparator.entity.Discount;
import com.example.price_comparator.entity.DiscountId;

public interface DiscountRepository extends JpaRepository<Discount, DiscountId> {

    // Custom query methods can be defined here if needed
    // Example: Find all discounts for a specific product
    List<Discount> findByIdProduct(Long productId);
    
    // Example: Find all discounts for a specific store
    List<Discount> findByIdStore(Long storeId);
    
    // Optional<Discount> findFirstByIdProductAndIdStoreAndFromDateLessThanEqualAndToDateGreaterThanEqual(
    //     Long productId, Long storeId, LocalDate today1, LocalDate today2
    // );

    @Query(
        "SELECT d FROM Discount d WHERE d.id.product = :productId AND d.id.store = :storeId " +
        "AND d.toDate >= :today AND d.id.fromDate <= :today"
    )
    Optional<Discount> findActiveDiscount(Long productId, Long storeId, LocalDate today);
    // Example: Find all discounts within a specific date range
    // List<Discount> findByFromDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Example: Find all discounts by percentage
    // List<Discount> findByDiscountPercentage(Double discountPercentage);
    
}
