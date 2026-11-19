package com.example.transactionlab;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditRecordRepository repository;

    public AuditService(AuditRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void writeRequired() {
        repository.save(new AuditRecord("REQUIRED audit"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeRequiresNew() {
        repository.save(new AuditRecord("REQUIRES_NEW audit"));
    }
}