package kz.iitu.springlab.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FineServiceTest {

    private final FineService fineService = new FineService();

    @Test
    void shouldCalculateFineForFiveDays() {
        assertThat(fineService.calculateFine(5))
                .isEqualTo(2500.0);
    }

    @Test
    void shouldReturnZeroForNoOverdueDays() {
        assertThat(fineService.calculateFine(0))
                .isZero();
    }

    @Test
    void shouldLimitFineToMaximum() {
        assertThat(fineService.calculateFine(30))
                .isEqualTo(10000.0);
    }

    @Test
    void shouldThrowExceptionForNegativeDays() {
        assertThatThrownBy(() -> fineService.calculateFine(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Overdue days cannot be negative");
    }
}