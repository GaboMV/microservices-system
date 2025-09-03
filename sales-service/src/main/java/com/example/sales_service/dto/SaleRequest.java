package com.example.sales_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class SaleRequest {

    private Long id;

    @NotBlank(message = "Sale number is required")
    private String saleNumber;

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0")
    private BigDecimal unitPrice;

    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", message = "Discount percentage must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Discount percentage must be between 0 and 100")
    private BigDecimal discountPercentage;

    private BigDecimal discountAmount;

    private BigDecimal finalAmount;

    private LocalDate saleDate;

    private Integer customerId;

    private String customerName;

    private String salesperson;

    private String paymentMethod;

    @Pattern(regexp = "pending|paid|partial|cancelled", message = "Payment status must be one of: pending, paid, partial, cancelled")
    private String paymentStatus;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Default constructor
    public SaleRequest() {}

    // Constructor with all fields
    public SaleRequest(Long id, String saleNumber, Integer productId, Integer quantity, BigDecimal unitPrice,
                       BigDecimal totalAmount, BigDecimal discountPercentage, BigDecimal discountAmount,
                       BigDecimal finalAmount, LocalDate saleDate, Integer customerId, String customerName,
                       String salesperson, String paymentMethod, String paymentStatus, String notes,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.saleNumber = saleNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.saleDate = saleDate;
        this.customerId = customerId;
        this.customerName = customerName;
        this.salesperson = salesperson;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor with required fields
    public SaleRequest(String saleNumber, Integer productId, Integer quantity, BigDecimal unitPrice, BigDecimal totalAmount) {
        this.saleNumber = saleNumber;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(String salesperson) {
        this.salesperson = salesperson;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SaleRequest{" +
                "id=" + id +
                ", saleNumber='" + saleNumber + '\'' +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", finalAmount=" + finalAmount +
                ", saleDate=" + saleDate +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", salesperson='" + salesperson + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}