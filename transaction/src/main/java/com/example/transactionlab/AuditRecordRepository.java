package com.example.transactionlab;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRecordRepository
        extends JpaRepository<AuditRecord, Long> {
}
