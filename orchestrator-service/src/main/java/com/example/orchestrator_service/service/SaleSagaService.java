package com.example.orchestrator_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.orchestrator_service.dto.JournalRequest;
import com.example.orchestrator_service.dto.JournalResponse;
import com.example.orchestrator_service.dto.ProductRequest;
import com.example.orchestrator_service.dto.SaleRequest;
import com.example.orchestrator_service.dto.SaleResponse;




@Service
public class SaleSagaService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SaleSagaService.class);

    private final RestTemplate restTemplate;

    public SaleSagaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void processSale(SaleRequest saleRequest) {
        Integer productId = saleRequest.getProductId();
        logger.info("Iniciando saga para producto id {}", productId);

        boolean stockDecreased = false;
        boolean saleCreated = false;
        Integer saleId = null;     
            Integer quantity = null;
        String  journalCode = null; 
JournalRequest journalDto = null;   
        try {
            // 1️ Consultar producto en warehouse
            ProductRequest product = restTemplate.getForObject(
                    "http://warehouse/api/products/" + productId,
                    ProductRequest.class
            );

            if (product == null) {
                throw new IllegalArgumentException("Producto no encontrado en warehouse");
            }

            if (product.getStockQuantity() < saleRequest.getQuantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
            }

           
           int newStock = product.getStockQuantity() - saleRequest.getQuantity();
            String warehousePutUrl = "http://warehouse/api/products/" + productId + "/stock";
            Map<String, Integer> stockUpdate = Map.of("stockQuantity", newStock);

            restTemplate.put(warehousePutUrl, stockUpdate);
            stockDecreased = true;
            logger.info("Stock actualizado en warehouse. Nuevo stock: {}", newStock);

            // 3️ Registrar venta en sales-service
            

ResponseEntity<SaleResponse> saleResponse = restTemplate.postForEntity(
        "http://sales/api/sales/direct",
        saleRequest,
        SaleResponse.class   
);

quantity = saleRequest.getQuantity();

if (!saleResponse.getStatusCode().is2xxSuccessful() || saleResponse.getBody() == null) {
    rollbackStock(saleRequest.getProductId(), quantity,stockDecreased);
    throw new RuntimeException("Error creando venta en sales-service");
   
}

 saleId = saleResponse.getBody().getId(); 


            saleCreated = true;
            logger.info("Venta registrada en sales-service: {}", saleResponse.getBody());

           
             journalDto = new JournalRequest(
                    "1010", product.getName(), "Venta de " + product.getName(),
                    saleRequest.getUnitPrice().multiply(BigDecimal.valueOf(saleRequest.getQuantity())),
                    "D", "sales-system", "Sales", "CC01",
                    "Venta: " + saleRequest.getSaleNumber(),
                    "REF" + saleRequest.getSaleNumber(),
                    saleRequest.getSaleDate() != null ? saleRequest.getSaleDate() : LocalDate.now()
            );

 ResponseEntity<JournalResponse> journalResponse = restTemplate.postForEntity(
                    "http://accounting/api/journals",
                    journalDto,
                    JournalResponse.class
            );
             journalCode = journalResponse.getBody().getBalanceType();
             logger.info("**El tipo de balance Es:", journalCode);
 if (!journalResponse.getStatusCode().is2xxSuccessful() || journalResponse.getBody() == null) {
                throw new RuntimeException("Error registrando asiento contable");
            }
            

            logger.info("Asiento contable registrado en accounting");

            logger.info("Saga completada exitosamente");

        } catch (Exception e) {
            logger.error("Error en la saga: {}. Ejecutando rollback...", e.getMessage());
           saleCreated = true;
ProductRequest sale = restTemplate.getForObject(
                    "http://warehouse/api/products/" + productId,
                    ProductRequest.class
            );
  rollback(productId, saleId,saleRequest, stockDecreased, saleCreated, journalDto, journalCode);
            throw new RuntimeException("Saga fallida y rollback aplicado: " + e.getMessage());
            
        }
    }

    private void rollback(Integer productId, Integer saleId, SaleRequest request, boolean stockDecreased, boolean saleCreated, JournalRequest journalRequest, String journalCode) {
        // 1️ Restaurar stock si se descontó
        if (stockDecreased) {
           try {
            restTemplate.put(
                
                    "http://warehouse/api/products/" + productId + "/stock/increase",
                    
                    Map.of("quantity", request.getQuantity())
            );
                logger.info("Rollback stock aplicado: stock restaurado");
            } catch (Exception ex) {
                logger.warn("Error al restaurar stock en warehouse: {}", ex.getMessage());
            }
        }

        // 2️ Eliminar venta si se creó
        if (saleCreated) {
            try {
                restTemplate.delete("http://sales/api/sales/id/" + saleId);
                logger.info("Rollback venta aplicado: venta eliminada");
            } catch (Exception ex) {
                logger.warn("Error al eliminar venta en sales-service: {}", ex.getMessage());
            }
        }

        // 3 Registrar asiento contable compensatorio
        try {
           
            journalRequest.setBalanceType(journalCode.equals("D") ? "C" : "D"); // Invertir tipo de balance
           ResponseEntity<JournalResponse> journalResponse = restTemplate.postForEntity(
                    "http://accounting/api/journals",
                    journalRequest,
                    JournalResponse.class
            );
             logger.info("Rollback asiento contable aplicado: asiento compensatorio registrado, tipo de codigo: "+journalRequest.getBalanceType());
        } catch (Exception ex) {
            logger.warn(" Error al registrar asiento compensatorio: {}", ex.getMessage());
        }
    }



    private void rollbackStock(Integer productId,Integer quantity, boolean stockDecreased) {
        // 1️ Restaurar stock si se descontó
        if (stockDecreased) {
           try {
            restTemplate.put(
                
                    "http://warehouse/api/products/" + productId + "/stock/increase",
                    
                    Map.of("quantity", quantity)
            );
                logger.info("******Rollback stock aplicado: stock restaurado");
            } catch (Exception ex) {
                logger.warn("Error al restaurar stock en warehouse: {}", ex.getMessage());
            }
        }

        
    }
}



















