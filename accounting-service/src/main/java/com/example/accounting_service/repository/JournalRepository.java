package com.example.accounting_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.accounting_service.entity.Journal;



@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    // Puedes agregar métodos personalizados aquí si los necesitas
}

