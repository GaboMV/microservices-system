package com.example.orchestrator_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orchestrator_service.dto.SaleRequest;
import com.example.orchestrator_service.service.SaleSagaService;

@RestController
@RequestMapping("/saga")
public class SagaController {

    private final SaleSagaService sagaService;

    public SagaController(SaleSagaService sagaService) {
        this.sagaService = sagaService;
    }

    @PostMapping("/sale")
    public ResponseEntity<String> createSale(@RequestBody SaleRequest request) {
        try {
            sagaService.processSale(request);
            return ResponseEntity.ok("Venta procesada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error en la saga: " + e.getMessage());
        }
    }
}