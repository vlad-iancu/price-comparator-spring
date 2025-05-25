package com.example.price_comparator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.price_comparator.entity.Product;
import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.ProductStoreId;
import com.example.price_comparator.entity.Store;

public interface ProductStoreRepository extends JpaRepository<ProductStore, ProductStoreId> {
    // Add custom queries if needed
    Optional<ProductStore> findByProductAndStore(Product product, Store store);

    // @Query("""
    //             SELECT ps FROM ProductStore ps
    //             WHERE ps.removal = false
    //               AND ps.store = :store
    //               AND ps.id.updatedAt = (
    //                 SELECT MAX(ps2.id.updatedAt)
    //                 FROM ProductStore ps2
    //                 WHERE ps2.product = ps.product
    //                   AND ps2.store = ps.store
    //               )
    //         """)
    // List<ProductStore> findLatestActiveProductStores(Store store);
}
