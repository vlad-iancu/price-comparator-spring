package com.example.price_comparator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.price_comparator.entity.PriceAlert;
import com.example.price_comparator.entity.User;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {

    // Custom query methods can be defined here if needed

    // Example: Find all price alerts for a specific user
    List<PriceAlert> findAllByUser(User user);

    List<PriceAlert> findAllByUserId(Long userId);

    // Check if a price alert with a given name and userId exists
    boolean existsByProductNameAndUserId(String name, Long userId);

}
