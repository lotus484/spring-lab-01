
package com.example.transactionlab;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    // Normal transfer
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account to = repository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        from.debit(amount);
        to.credit(amount);

        repository.save(from);
        repository.save(to);
    }

    // Method for testing rollback
    @Transactional
    public void transferWithFailure(Long fromId, Long toId, BigDecimal amount) {
        Account from = repository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account to = repository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        from.debit(amount);
        repository.saveAndFlush(from);

        throw new RuntimeException("Simulated failure");
    }
}
