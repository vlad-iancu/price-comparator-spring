package com.example.price_comparator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.price_comparator.entity.Product;
import com.example.price_comparator.entity.ProductStore;
import com.example.price_comparator.entity.ProductStoreId;
import com.example.price_comparator.entity.Store;

public interface ProductStoreRepository extends JpaRepository<ProductStore, ProductStoreId> {
    // Add custom queries if needed
    Optional<ProductStore> findByProductAndStore(Product product, Store store);

    @Query("""
        SELECT ps FROM ProductStore ps
        WHERE ps.product.id = :productId
          AND ps.id.updatedAt = (
            SELECT MAX(ps2.id.updatedAt)
            FROM ProductStore ps2
            WHERE ps2.product.id = :productId
              AND ps2.store = ps.store
          )
    """)
    List<ProductStore> findLatestProductStoresByProductId(Long productId);

    @Query("""
        SELECT ps FROM ProductStore ps
        WHERE ps.id.product = :productId AND ps.id.store = :storeId ORDER BY ps.id.updatedAt ASC
    """)
    List<ProductStore> findByProductIdOrderedByUpdatedAt(Long productId, Long storeId);


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
