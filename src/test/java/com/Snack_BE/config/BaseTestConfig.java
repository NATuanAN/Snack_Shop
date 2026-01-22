package com.Snack_BE.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Base Test Configuration
 * Only provides essential beans for tests
 * Import this in tests that need PasswordEncoder: @Import(BaseTestConfig.class)
 */
@TestConfiguration
public class BaseTestConfig {

    /**
     * Password encoder for testing
     * Using BCryptPasswordEncoder for consistency with production
     */
    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
