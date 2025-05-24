package com.example.price_comparator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.price_comparator.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be defined here if needed
    // Example: Find all products with a specific name
    List<Product> findByProductName(String productName);
    
    // Example: Find all products by category
    List<Product> findByProductCategory(String productCategory);

    // Find by productName and brand
    Optional<Product> findByProductNameAndBrand(String productName, String brand);


}
