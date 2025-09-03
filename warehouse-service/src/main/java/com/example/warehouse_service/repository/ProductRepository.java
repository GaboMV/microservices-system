package com.example.warehouse_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.warehouse_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findBySku(String sku);
}