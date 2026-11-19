package com.example.transactionlab;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OptimisticLockingTests {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testOptimisticLocking() {

        repository.deleteAll();

        Account saved = repository.save(
                new Account("Alice", new BigDecimal("1000.00")));

        EntityManager em1 = entityManagerFactory.createEntityManager();
        EntityManager em2 = entityManagerFactory.createEntityManager();

        EntityTransaction tx1 = em1.getTransaction();
        EntityTransaction tx2 = em2.getTransaction();

        try {
            tx1.begin();
            tx2.begin();

            Account first = em1.find(Account.class, saved.getId());
            Account second = em2.find(Account.class, saved.getId());

            first.credit(new BigDecimal("100.00"));
            tx1.commit();

            second.credit(new BigDecimal("200.00"));

            assertThrows(RuntimeException.class, tx2::commit);

            if (tx2.isActive()) {
                tx2.rollback();
            }

            Account result = repository.findById(saved.getId())
                    .orElseThrow();

            assertEquals(new BigDecimal("1100.00"), result.getBalance());

            System.out.println("Optimistic locking conflict detected!");
            System.out.println("Final balance: " + result.getBalance());

        } finally {
            if (tx1.isActive()) tx1.rollback();
            if (tx2.isActive()) tx2.rollback();

            em1.close();
            em2.close();
        }
    }
}