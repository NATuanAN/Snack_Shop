package com.Snack_BE.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.Snack_BE.Model.SystemReportEntity;
import com.Snack_BE.Repo.SystemReportRepo;

/**
 * Unit Tests for SystemReportService
 * Tests system report retrieval functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SystemReportService Unit Tests")
class SystemReportServiceTest {

    @Mock
    private SystemReportRepo systemReportRepo;

    @InjectMocks
    private SystemReportService systemReportService;

    private SystemReportEntity testReport1;
    private SystemReportEntity testReport2;

    @BeforeEach
    void setUp() {
        // Setup test system reports
        testReport1 = new SystemReportEntity();
        
        testReport2 = new SystemReportEntity();
    }

    @Test
    @DisplayName("getAllSysReport should return list when reports exist")
    void getAllSysReport_ShouldReturnList_WhenReportsExist() {
        // Arrange
        List<SystemReportEntity> reports = Arrays.asList(testReport1, testReport2);
        when(systemReportRepo.findAll()).thenReturn(reports);

        // Act
        ResponseEntity<List<SystemReportEntity>> response = systemReportService.getAllSysReport();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(systemReportRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllSysReport should return empty list when no reports")
    void getAllSysReport_ShouldReturnEmptyList_WhenNoReports() {
        // Arrange
        when(systemReportRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<SystemReportEntity>> response = systemReportService.getAllSysReport();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(systemReportRepo, times(1)).findAll();
    }
}
