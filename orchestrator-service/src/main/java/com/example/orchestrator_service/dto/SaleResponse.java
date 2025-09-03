package com.example.orchestrator_service.dto;

public class SaleResponse {
    private Integer id;
    private String saleNumber;
    private String message;
   private Integer ProductId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSaleNumber() { return saleNumber; }
    public void setSaleNumber(String saleNumber) { this.saleNumber = saleNumber; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getProductId() { return ProductId; }
    public void setProductId(Integer productId) { ProductId = productId; }
}