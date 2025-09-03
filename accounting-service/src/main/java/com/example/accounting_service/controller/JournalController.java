package com.example.accounting_service.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounting_service.dto.JournalRequest;
import com.example.accounting_service.entity.Journal;
import com.example.accounting_service.repository.JournalRepository;
import com.example.accounting_service.service.RegisterJournal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/journals")
public class JournalController {
  private final JournalRepository journalRepository;
    private final RegisterJournal registerJournal;
    private static final Logger logger = LoggerFactory.getLogger(JournalController.class);

    @Autowired
    public JournalController(RegisterJournal registerJournal, JournalRepository journalRepository) {
        this.registerJournal = registerJournal;
        this.journalRepository = journalRepository;
    }

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody JournalRequest dto) {
        logger.info("POST request to create journal with data: {}", dto);
        try {
            logger.info("Journal created successfully with id: {}", dto.getAccountCode());
            Journal journal = registerJournal.registerJournal(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(journal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
             logger.error("Unexpected error while creating journal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Journal service is running!");
    }
     @GetMapping("/all")
    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }
}