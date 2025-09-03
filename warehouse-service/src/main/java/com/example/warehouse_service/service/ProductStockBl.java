package com.example.warehouse_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.warehouse_service.entity.Product;
import com.example.warehouse_service.repository.ProductRepository;

@Service
public class ProductStockBl {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product;
    }
    
    /**
     * Updates the stock information of a product
     * @param product The product with updated stock information
     * @return The updated Product entity
     * @throws IllegalArgumentException if product is null or has invalid data
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Product updateProductStock(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        
        // Verify the product exists in the database
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " not found");
        }
        
        // Validate stock quantity is not negative
        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        
        // Save and return the updated product
        return productRepository.save(product);
    }
    public Product increaseStock(Integer productId, int quantity) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id " + productId);
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        product.setStockQuantity(product.getStockQuantity() + quantity);
        return productRepository.save(product);
    }
}
