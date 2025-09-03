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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @PostMapping("/direct")
public ResponseEntity<Sale> createSaleDirect(@Valid @RequestBody SaleRequest saleRequest) {
    logger.info("Creando venta DIRECTA (solo insert) para: {}", saleRequest);

    try {
        // Crear la venta directamente sin llamar a warehouse ni accounting
        Sale sale = new Sale();
        sale.setProductId(saleRequest.getProductId());
        sale.setQuantity(saleRequest.getQuantity());
        sale.setUnitPrice(saleRequest.getUnitPrice());
        sale.setTotalAmount(saleRequest.getTotalAmount());
        sale.setDiscountAmount(saleRequest.getDiscountAmount() != null ? saleRequest.getDiscountAmount() : BigDecimal.ZERO);
        sale.setFinalAmount(saleRequest.getFinalAmount() != null ? saleRequest.getFinalAmount() : sale.getTotalAmount().subtract(sale.getDiscountAmount()));
        sale.setSaleNumber(saleRequest.getSaleNumber() != null ? saleRequest.getSaleNumber()
                : "SALE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setSaleDate(saleRequest.getSaleDate() != null ? saleRequest.getSaleDate() : LocalDate.now());
        sale.setCustomerId(saleRequest.getCustomerId());
        sale.setCustomerName(saleRequest.getCustomerName());
        sale.setSalesperson(saleRequest.getSalesperson());
        sale.setPaymentMethod(saleRequest.getPaymentMethod() != null ? saleRequest.getPaymentMethod() : "cash");
        sale.setPaymentStatus(saleRequest.getPaymentStatus() != null ? saleRequest.getPaymentStatus() : "pending");
        sale.setNotes(saleRequest.getNotes());
        sale.setCreatedAt(LocalDateTime.now());
        

        Sale savedSale = saleService.saveDirectSale(sale);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSale);
    } catch (Exception e) {
        logger.error("Error al crear venta directa", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
  @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteSaleById(@PathVariable Long id) {
        Sale sale = saleService.findById(id);

        if (sale == null) {
            logger.warn("Venta no encontrada con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Venta no encontrada con id: " + id);
        }

        saleService.deleteSale(sale);
        logger.info("Venta eliminada exitosamente: {}", id);
        return ResponseEntity.ok("Venta eliminada con id: " + id);
    }
}
   
