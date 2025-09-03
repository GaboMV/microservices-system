package com.example.accounting_service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.accounting_service.dto.JournalRequest;
import com.example.accounting_service.entity.Journal;
import com.example.accounting_service.repository.JournalRepository;

// Add this import

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Service
public class RegisterJournal {
    
    @Autowired
    private JournalRepository journalRepository;
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Journal registerJournal(JournalRequest journalDto) {
        // Validaciones básicas
        if (journalDto == null) {
            throw new IllegalArgumentException("JournalDto cannot be null");
        }
        
        if (journalDto.getAccountCode() == null || journalDto.getAccountCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Account code is required");
        }
        
        if (journalDto.getAccountName() == null || journalDto.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Account name is required");
        }
        
        if (journalDto.getDescription() == null || journalDto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        
        if (journalDto.getAmount() == null || journalDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        
        if (journalDto.getBalanceType() == null || 
            (!journalDto.getBalanceType().equals("D") && !journalDto.getBalanceType().equals("C"))) {
            throw new IllegalArgumentException("Balance type must be 'D' (Debit) or 'C' (Credit)");
        }
        
        if (journalDto.getCreatedBy() == null || journalDto.getCreatedBy().trim().isEmpty()) {
            throw new IllegalArgumentException("Created by is required");
        }
        
        // Crear nueva entrada de diario
        Journal journal = new Journal();
        
        // Generar número de entrada único
        String journalEntryNumber = generateJournalEntryNumber();
        journal.setJournalEntryNumber(journalEntryNumber);
        
        // Configurar campos básicos
        journal.setAccountCode(journalDto.getAccountCode().trim());
        journal.setAccountName(journalDto.getAccountName().trim());
        journal.setDescription(journalDto.getDescription().trim());
        journal.setCreatedBy(journalDto.getCreatedBy().trim());
        
        // Configurar fechas
        LocalDate transactionDate = journalDto.getTransactionDate() != null 
            ? journalDto.getTransactionDate() 
            : LocalDate.now();
        journal.setTransactionDate(transactionDate);
        journal.setPostingDate(LocalDate.now());
        
        // Calcular y asignar montos según el tipo de balance
        BigDecimal amount = journalDto.getAmount();
        if ("D".equals(journalDto.getBalanceType())) {
            journal.setDebitAmount(amount);
            journal.setCreditAmount(BigDecimal.ZERO);
            journal.setBalanceType(Journal.BalanceType.D);
        } else {
            journal.setDebitAmount(BigDecimal.ZERO);
            journal.setCreditAmount(amount);
            journal.setBalanceType(Journal.BalanceType.C);
        }
        
        // Configurar campos opcionales
        if (journalDto.getReferenceNumber() != null && !journalDto.getReferenceNumber().trim().isEmpty()) {
            journal.setReferenceNumber(journalDto.getReferenceNumber().trim());
        }
        
        if (journalDto.getDepartment() != null && !journalDto.getDepartment().trim().isEmpty()) {
            journal.setDepartment(journalDto.getDepartment().trim());
        }
        
        if (journalDto.getCostCenter() != null && !journalDto.getCostCenter().trim().isEmpty()) {
            journal.setCostCenter(journalDto.getCostCenter().trim());
        }
        
        if (journalDto.getNotes() != null && !journalDto.getNotes().trim().isEmpty()) {
            journal.setNotes(journalDto.getNotes().trim());
        }
        
        // Configurar valores por defecto
        journal.setCurrencyCode("USD");
        journal.setExchangeRate(BigDecimal.ONE);
        journal.setStatus(Journal.Status.draft);
        
        // Guardar en la base de datos
        return journalRepository.save(journal);
    }
    
    /**
     * Genera un número único para la entrada de diario
     * Formato: JE-YYYYMMDD-HHMMSS
     */
    private String generateJournalEntryNumber() {
        LocalDate now = LocalDate.now();
        long timestamp = System.currentTimeMillis() % 100000; // últimos 5 dígitos del timestamp
        return String.format("JE-%s-%05d", 
            now.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
            timestamp);
    }

}
