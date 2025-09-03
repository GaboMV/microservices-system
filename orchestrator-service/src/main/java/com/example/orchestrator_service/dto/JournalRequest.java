

package com.example.orchestrator_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;




public class JournalRequest {

    private String accountCode;
    private String accountName;
    private String description;
    private BigDecimal amount;
    private String balanceType; // "D" for Debit, "C" for Credit
    private LocalDate transactionDate;
    private String referenceNumber;
    private String createdBy;
    private String department;
    private String costCenter;
    private String notes;

    public JournalRequest() {}

    // Constructor para inicializar rápidamente
    public JournalRequest(String accountCode, String accountName, String description,
                          BigDecimal amount, String balanceType, String createdBy,
                          String department, String costCenter, String notes,
                          String referenceNumber, LocalDate transactionDate) {
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.description = description;
        this.amount = amount;
        this.balanceType = balanceType;
        this.createdBy = createdBy;
        this.department = department;
        this.costCenter = costCenter;
        this.notes = notes;
        this.referenceNumber = referenceNumber;
        this.transactionDate = transactionDate;
    }

    // Getters y Setters
    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getBalanceType() { return balanceType; }
    public void setBalanceType(String balanceType) { this.balanceType = balanceType; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCostCenter() { return costCenter; }
    public void setCostCenter(String costCenter) { this.costCenter = costCenter; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "JournalRequest{" +
                "accountCode='" + accountCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", balanceType='" + balanceType + '\'' +
                ", transactionDate=" + transactionDate +
                ", createdBy='" + createdBy + '\'' +
                ", department='" + department + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", notes='" + notes + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}