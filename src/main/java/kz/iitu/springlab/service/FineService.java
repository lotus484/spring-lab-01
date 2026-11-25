package kz.iitu.springlab.service;

import org.springframework.stereotype.Service;

@Service
public class FineService {

    public static final double DAILY_FINE = 500.0;
    public static final double MAX_FINE = 10000.0;

    public double calculateFine(int overdueDays) {

        if (overdueDays < 0) {
            throw new IllegalArgumentException(
                    "Overdue days cannot be negative"
            );
        }

        double fine = overdueDays * DAILY_FINE;

        return Math.min(fine, MAX_FINE);
    }
}
