package com.example.price_comparator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.price_comparator.entity.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    // Custom query methods can be defined here if needed
    // Example: Find all shopping lists for a specific user
    List<ShoppingList> findAllByUserId(Long userId);
    
}
