package com.example.sales_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.sales_service.dto.JournalRequest;
import com.example.sales_service.dto.ProductRequest;
import com.example.sales_service.dto.SaleRequest;
import com.example.sales_service.entity.Sale;
import com.example.sales_service.repository.SaleRepository;

import jakarta.transaction.Transactional;
@Service
public class SaleService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SaleService.class);

    private final SaleRepository saleRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SaleService(SaleRepository saleRepository, RestTemplate restTemplate) {
        this.saleRepository = saleRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public Sale createAndSaveSale(SaleRequest saleRequest) {
        Integer productId = saleRequest.getProductId();
        logger.debug("Consultando stock para producto id: {}", productId);

        // 1️⃣ Consultar producto en warehouse
        ProductRequest productFromWarehouse = restTemplate.getForObject(
                "http://warehouse/api/products/" + productId,
                ProductRequest.class
        );

        if (productFromWarehouse == null) {
            throw new IllegalArgumentException("Producto no encontrado en warehouse");
        }

        if (productFromWarehouse.getStockQuantity() < saleRequest.getQuantity()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + productFromWarehouse.getName());
        }

        // 2️⃣ Actualizar stock
        int newStock = productFromWarehouse.getStockQuantity() - saleRequest.getQuantity();
        logger.debug("Actualizando stock para producto id {} a {}", productId, newStock);

        String warehousePutUrl = "http://warehouse/api/products/" + productId + "/stock";
        Map<String, Integer> stockUpdate = Map.of("stockQuantity", newStock);
        restTemplate.put(warehousePutUrl, stockUpdate);

        // 3️⃣ Mapear SaleRequest a Sale
        Sale sale = new Sale();
        sale.setProductId(productId);
        sale.setQuantity(saleRequest.getQuantity());
        sale.setUnitPrice(saleRequest.getUnitPrice() != null ? saleRequest.getUnitPrice() : productFromWarehouse.getPrice());
        sale.setTotalAmount(sale.getTotalAmount() != null ? saleRequest.getTotalAmount()
                : sale.getUnitPrice().multiply(BigDecimal.valueOf(saleRequest.getQuantity())));

        // Descuento
        if (saleRequest.getDiscountPercentage() != null && saleRequest.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = sale.getTotalAmount()
                    .multiply(saleRequest.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100));
            sale.setDiscountAmount(discountAmount);
            sale.setFinalAmount(sale.getTotalAmount().subtract(discountAmount));
        } else {
            sale.setDiscountAmount(BigDecimal.ZERO);
            sale.setFinalAmount(sale.getTotalAmount());
        }

        // Campos opcionales
        sale.setSaleNumber(saleRequest.getSaleNumber() != null ? saleRequest.getSaleNumber()
                : "SALE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setSaleDate(saleRequest.getSaleDate() != null ? saleRequest.getSaleDate() : LocalDate.now());
        sale.setPaymentMethod(saleRequest.getPaymentMethod() != null ? saleRequest.getPaymentMethod() : "cash");
        sale.setPaymentStatus(saleRequest.getPaymentStatus() != null ? saleRequest.getPaymentStatus() : "pending");
        sale.setCustomerId(saleRequest.getCustomerId());
        sale.setCustomerName(saleRequest.getCustomerName());
        sale.setSalesperson(saleRequest.getSalesperson());
        sale.setNotes(saleRequest.getNotes());
        sale.setCreatedAt(LocalDateTime.now());
        

        // Guardar venta
        Sale savedSale = saleRepository.save(sale);
        logger.info("Venta creada exitosamente: {}", savedSale.getSaleNumber());

        // 4️⃣ Registrar asiento contable
        String accountingUrl = "http://accounting/api/journals";
        JournalRequest journalDto = new JournalRequest(
                "1010",
                productFromWarehouse.getName(),
                "Venta de " + productFromWarehouse.getName(),
                savedSale.getFinalAmount(),
                "D",
                "sales-system",
                "Sales",
                "CC01",
                "Venta: " + savedSale.getSaleNumber(),
                "REF" + savedSale.getSaleNumber().substring(5),
                savedSale.getSaleDate()
        );

        try {
            restTemplate.postForObject(accountingUrl, journalDto, Void.class);
            logger.info("Asiento contable registrado exitosamente para venta: {}", savedSale.getSaleNumber());
        } catch (Exception e) {
            logger.warn("Error al registrar asiento contable, pero la venta se creó: {}", e.getMessage());
        }

        return savedSale;
    }
public Sale saveDirectSale(Sale sale) {
    return saleRepository.save(sale);
}
@Transactional
    public void deleteSale(Sale sale) {
        saleRepository.delete(sale);
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }
    public Sale findBySaleNumber(String saleNumber) {
        return saleRepository.findBySaleNumber(saleNumber).orElse(null);
    }
}