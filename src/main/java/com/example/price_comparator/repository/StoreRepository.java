package com.example.price_comparator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // Custom query methods can be defined here if needed
    // Example: Find all stores with a specific name
    List<Store> findByName(String name);
    @Query("SELECT ps FROM ProductStore ps WHERE ps.store.id = :storeId")
    List<ProductStore> findProductStoresByStoreId(Long storeId);
}
