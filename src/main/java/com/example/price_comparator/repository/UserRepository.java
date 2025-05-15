package com.example.price_comparator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.price_comparator.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This interface will automatically provide CRUD operations for the User entity
    // You can add custom query methods here if needed
    Optional<User> findByUsername(String username);
}
