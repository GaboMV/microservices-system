package com.example.warehouse_service.api;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/warehouse")
public class WarehouseApi {
@GetMapping
public Map<String, Object> getStatus() {
    return Map.of(
        "service", "warehouse-service",
        "status", "running",
        "timestamp", LocalDateTime.now()
    );
    
    }}
