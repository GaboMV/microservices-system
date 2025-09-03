package com.example.orchestrator_service.dto;

public class SaleResponse {
    private Integer id;
    private String saleNumber;
    private String message;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String saleNumber) { this.saleNumber = saleNumber; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}