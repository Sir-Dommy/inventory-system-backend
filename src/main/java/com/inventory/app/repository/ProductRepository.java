package com.inventory.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.app.entity.Products;

public interface ProductRepository extends JpaRepository<Products, Long> {
    // You can define custom queries or methods here if needed
}
