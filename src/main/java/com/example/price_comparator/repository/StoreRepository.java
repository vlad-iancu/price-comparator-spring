package com.example.price_comparator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // Custom query methods can be defined here if needed
    // Example: Find all stores with a specific name
    @Query("SELECT s FROM Store s WHERE s.name = :name")
    Optional<Store> findFirstByName(String name);
    @Query("SELECT ps FROM ProductStore ps WHERE ps.store.id = :storeId")
    List<ProductStore> findByStoreId(Long storeId);
}
