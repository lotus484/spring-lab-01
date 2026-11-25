package kz.iitu.springlab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            JdbcTemplate jdbc,
            PasswordEncoder encoder) {

        return args -> {
            jdbc.update(
                    "MERGE INTO users (username, password, enabled) " +
                            "KEY(username) VALUES (?, ?, ?)",
                    "user", encoder.encode("user123"), true
            );

            jdbc.update(
                    "MERGE INTO users (username, password, enabled) " +
                            "KEY(username) VALUES (?, ?, ?)",
                    "admin", encoder.encode("admin123"), true
            );

            jdbc.update(
                    "MERGE INTO authorities (username, authority) " +
                            "KEY(username, authority) VALUES (?, ?)",
                    "user", "ROLE_USER"
            );

            jdbc.update(
                    "MERGE INTO authorities (username, authority) " +
                            "KEY(username, authority) VALUES (?, ?)",
                    "admin", "ROLE_USER"
            );

            jdbc.update(
                    "MERGE INTO authorities (username, authority) " +
                            "KEY(username, authority) VALUES (?, ?)",
                    "admin", "ROLE_ADMIN"
            );
        };
    }
}