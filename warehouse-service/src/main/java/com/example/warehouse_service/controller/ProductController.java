package com.example.warehouse_service.controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.warehouse_service.entity.Product;
import com.example.warehouse_service.service.ProductStockBl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductStockBl productStockBl;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
                logger.info("GET request received for product with id: {}", id);

        Product product = productStockBl.getProductById(id);
        if (product == null) {
            logger.warn("Product with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
         logger.info("Product found: {}", product);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
          logger.info("PUT request to update stock for product id: {}, body: {}", id, body);
        Product product = productStockBl.getProductById(id);
        if (product == null) {
            logger.warn("Product with id {} not found when trying to update stock", id);
            return ResponseEntity.notFound().build();
            }

        Integer newStock = body.get("stockQuantity");
        logger.debug("New stock value received: {}", newStock);
        product.setStockQuantity(newStock);
        Product updated = productStockBl.updateProductStock(product);
               logger.info("Product stock updated successfully for id {}. New stock: {}", id, updated.getStockQuantity());

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
logger.info("Test endpoint called");       
        return ResponseEntity.ok("Warehouse service is running!");
    }
    

    @PutMapping("/{id}/stock/increase")
public ResponseEntity<Product> increaseStock(
        @PathVariable Integer id,
        @RequestBody Map<String, Integer> body) {
    int quantity = body.get("quantity");
    Product updated = productStockBl.increaseStock(id, quantity);
    return ResponseEntity.ok(updated);
}
}