package com.example.transactionlab;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionPropagationService {

    private final AuditService auditService;

    public TransactionPropagationService(AuditService auditService) {
        this.auditService = auditService;
    }

    @Transactional
    public void testRequired() {
        auditService.writeRequired();
        throw new RuntimeException("Outer transaction failed");
    }

    @Transactional
    public void testRequiresNew() {
        auditService.writeRequiresNew();
        throw new RuntimeException("Outer transaction failed");
    }
}