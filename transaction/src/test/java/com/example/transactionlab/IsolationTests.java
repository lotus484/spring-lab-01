package com.example.transactionlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IsolationTests {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private BigDecimal[] readTwice(int isolationLevel, Long accountId) {

        TransactionTemplate outer =
                new TransactionTemplate(transactionManager);

        outer.setIsolationLevel(isolationLevel);

        TransactionTemplate inner =
                new TransactionTemplate(transactionManager);

        inner.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        return outer.execute(status -> {

            BigDecimal firstRead = jdbc.queryForObject(
                    "SELECT balance FROM accounts WHERE id = ?",
                    BigDecimal.class, accountId);

            inner.execute(innerStatus -> {
                jdbc.update(
                        "UPDATE accounts SET balance = balance + 100 WHERE id = ?",
                        accountId);
                return null;
            });

            BigDecimal secondRead = jdbc.queryForObject(
                    "SELECT balance FROM accounts WHERE id = ?",
                    BigDecimal.class, accountId);

            return new BigDecimal[]{firstRead, secondRead};
        });
    }

    @Test
    void compareIsolationLevels() {

        repository.deleteAll();

        Account alice = repository.save(
                new Account("Alice", new BigDecimal("1000.00")));

        BigDecimal[] readCommitted = readTwice(
                TransactionDefinition.ISOLATION_READ_COMMITTED,
                alice.getId());

        jdbc.update(
                "UPDATE accounts SET balance = 1000.00 WHERE id = ?",
                alice.getId());

        BigDecimal[] repeatableRead = readTwice(
                TransactionDefinition.ISOLATION_REPEATABLE_READ,
                alice.getId());

        System.out.println("READ_COMMITTED:");
        System.out.println("First read: " + readCommitted[0]);
        System.out.println("Second read: " + readCommitted[1]);

        System.out.println("REPEATABLE_READ:");
        System.out.println("First read: " + repeatableRead[0]);
        System.out.println("Second read: " + repeatableRead[1]);

        assertEquals(new BigDecimal("1000.00"), readCommitted[0]);
        assertEquals(new BigDecimal("1100.00"), readCommitted[1]);

        assertEquals(new BigDecimal("1000.00"), repeatableRead[0]);
        assertEquals(new BigDecimal("1000.00"), repeatableRead[1]);
    }
}