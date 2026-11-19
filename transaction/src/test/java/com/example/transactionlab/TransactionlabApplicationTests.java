package com.example.transactionlab;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionlabApplicationTests {

	@Autowired
	private AccountRepository repository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuditRecordRepository auditRepository;

	@Autowired
	private TransactionPropagationService propagationService;

	@Test
	void testSuccessfulTransfer() {
		repository.deleteAll();

		Account alice = repository.save(new Account("Alice", new BigDecimal("1000.00")));
		Account bob = repository.save(new Account("Bob", new BigDecimal("500.00")));

		accountService.transfer(alice.getId(), bob.getId(), new BigDecimal("100.00"));

		assertEquals(new BigDecimal("900.00"), repository.findById(alice.getId()).orElseThrow().getBalance());
		assertEquals(new BigDecimal("600.00"), repository.findById(bob.getId()).orElseThrow().getBalance());
	}

	@Test
	void testInsufficientBalance() {
		repository.deleteAll();

		Account alice = repository.save(new Account("Alice", new BigDecimal("50.00")));
		Account bob = repository.save(new Account("Bob", new BigDecimal("500.00")));

		assertThrows(IllegalStateException.class, () ->
				accountService.transfer(alice.getId(), bob.getId(), new BigDecimal("100.00")));

		assertEquals(new BigDecimal("50.00"), repository.findById(alice.getId()).orElseThrow().getBalance());
		assertEquals(new BigDecimal("500.00"), repository.findById(bob.getId()).orElseThrow().getBalance());
	}

	@Test
	void testTransferRollback() {
		repository.deleteAll();

		Account alice = repository.save(new Account("Alice", new BigDecimal("1000.00")));
		Account bob = repository.save(new Account("Bob", new BigDecimal("500.00")));

		assertThrows(RuntimeException.class, () ->
				accountService.transferWithFailure(alice.getId(), bob.getId(), new BigDecimal("100.00")));

		assertEquals(new BigDecimal("1000.00"), repository.findById(alice.getId()).orElseThrow().getBalance());
		assertEquals(new BigDecimal("500.00"), repository.findById(bob.getId()).orElseThrow().getBalance());
	}

	@Test
	void testRequiredRollback() {
		auditRepository.deleteAll();

		assertThrows(RuntimeException.class, () ->
				propagationService.testRequired());

		assertEquals(0, auditRepository.count());
	}

	@Test
	void testRequiresNewCommit() {
		auditRepository.deleteAll();

		assertThrows(RuntimeException.class, () ->
				propagationService.testRequiresNew());

		assertEquals(1, auditRepository.count());
	}
}