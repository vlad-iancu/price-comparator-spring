package com.example.price_comparator.entity;


import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    @NonNull
    private String productName;

    @Column(name = "package_unit", nullable = false)
    @NonNull
    private String packageUnit;

    @Column(name = "product_category", nullable = false)
    @NonNull
    private String productCategory;

    @Column(name = "brand", nullable = false)
    @NonNull
    private String brand;

    @OneToMany(mappedBy = "product")
    private Set<ProductStore> productStores;
    // @ManyToMany(mappedBy = "products")
    // private Set<ShoppingList> shoppingLists;

    // Getters and setters
}

