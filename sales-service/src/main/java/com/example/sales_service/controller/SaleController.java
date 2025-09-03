package com.example.sales_service.controller;

import com.example.sales_service.dto.ProductRequest;
import com.example.sales_service.dto.SaleRequest;
import com.example.sales_service.entity.Sale;
import com.example.sales_service.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        logger.info("Recibido SaleRequest: {}", saleRequest);

        try {
            Sale createdSale = saleService.createAndSaveSale(saleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de negocio al crear venta: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Error inesperado al crear venta", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
     @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        // Implementar si es necesario
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
   
