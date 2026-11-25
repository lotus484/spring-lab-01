# Lab 12 — Automated Testing

## 1. Project Information

- Project: spring-lab-01
- Course: Spring Boot
- Branch: lab12
- Java version: 21
- Build tool: Maven

## 2. Unit Tests — FineService

Created unit tests for FineService.

The tests cover:
- Fine calculation for five overdue days.
- Zero fine when there are no overdue days.
- Maximum fine limit.
- Exception when overdue days are negative.

Result: 4 tests passed.

## 3. Web Layer Tests — BookController

Created BookControllerTest using @WebMvcTest and MockMvc.

The tests cover:
- Retrieving the list of books.
- Adding a book.
- Deleting a book.
- Sending a POST request as an authenticated user.

Result: 4 tests passed.

## 4. Repository Tests — BookRepository

Created BookRepositoryTest using @DataJpaTest and an H2 test database.

The tests cover:
- Saving and finding a book.
- Deleting a book.
- Counting books.

Result: 3 tests passed.

## 5. PostgreSQL Integration Test

Created BookPostgresIntegrationTest using:
- @SpringBootTest
- Testcontainers
- PostgreSQL 16
- PostgreSQLContainer

The integration test checks saving and retrieving a book.

Status: Test source code compiles successfully. Runtime execution has not been verified because Docker is not running.

## 6. Code Coverage — JaCoCo

Configured JaCoCo Maven Plugin version 0.8.14.

Generated coverage report:

target/site/jacoco/index.html

Total instruction coverage: 73%.

## 7. Build Verification

Command:

mvn clean verify

Result: BUILD SUCCESS.

Test results:
- FineServiceTest: 4 tests passed.
- BookControllerTest: 4 tests passed.
- BookRepositoryTest: 3 tests passed.
- SpringLab01ApplicationTests: 1 test passed.

Total: 12 tests passed.

## 8. Evidence

Screenshots of test results and the JaCoCo coverage report should be attached to the lab submission.
