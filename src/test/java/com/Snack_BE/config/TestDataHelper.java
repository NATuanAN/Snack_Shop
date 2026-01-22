package com.Snack_BE.config;

/**
 * Test Data Constants & Utilities
 * Simple class for sharing test data - No @TestConfiguration to avoid conflicts
 */
public class TestDataHelper {

    // Test User Data
    public static final String TEST_USER_EMAIL = "test@example.com";
    public static final String TEST_USER_PASSWORD = "Test123!@#";
    public static final String TEST_OAUTH_EMAIL = "oauth@gmail.com";
    
    // Test IDs
    public static final Long TEST_USER_ID = 1L;
    public static final Long TEST_PRODUCT_ID = 1L;
    public static final Long TEST_ORDER_ID = 1L;
    public static final Long TEST_SHOP_ID = 1L;
    
    // Test Product Data
    public static final String TEST_PRODUCT_NAME = "Test Snack";
    public static final Double TEST_PRODUCT_PRICE = 10000.0;
    public static final Integer TEST_PRODUCT_STOCK = 100;
    
    // Test JWT Token (mock)
    public static final String TEST_JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test";
    
    /**
     * Generate unique test email with timestamp
     */
    public static String generateTestEmail() {
        return "test_" + System.currentTimeMillis() + "@example.com";
    }
    
    /**
     * Sleep helper for async operations
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
