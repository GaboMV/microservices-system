package com.example.orchestrator_service.dto;

public class JournalResponse {
    private Integer id;
    private String status;

    private String balanceType;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBalanceType() { 
        return balanceType; 
    }
    public void setBalanceType(String balanceType) { 
        this.balanceType = balanceType; 
    }
}