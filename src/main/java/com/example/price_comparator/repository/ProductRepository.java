package com.example.price_comparator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.price_comparator.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be defined here if needed
    // Example: Find all products with a specific name
    List<Product> findByName(String name);
    
    // Example: Find all products within a specific price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // Example: Find all products by category
    List<Product> findByCategory(String category);
    
}
