package project.backend.reporting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingControllerTest {

    private ReportingController reportingController;

    @Mock
    private ReportingService reportingService;

    @BeforeEach
    void setup() {
        this.reportingController = new ReportingController();
    }

    @Test
    void testGetLogs() {
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        when(reportingService.getLogs(startDate, endDate)).thenReturn(any());

        ResponseEntity<InputStreamResource> response = reportingController.getLogs(startDate, endDate);

        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=Access Logs.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        // Add more assertions as needed
    }

    @Test
    void testGetLogsByEmployeeId() {
        long employeeId = 123;
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);
        when(reportingService.getLogsOfGivenEmployee(employeeId, startDate, endDate)).thenReturn(any());

        ResponseEntity<InputStreamResource> response = reportingController.getLogsByEmployeeId(employeeId, startDate, endDate);

        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=Employees.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        // Add more assertions as needed
    }

    @Test
    void testGetAllUsers() {

        when(reportingService.getAllUsers()).thenReturn(any());

        ResponseEntity<InputStreamResource> response = reportingController.getAllUsers();

        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=Employees.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        // Add more assertions as needed
    }

    @Test
    void testGetActiveUsers() {

        when(reportingService.getActiveUsers()).thenReturn(any());

        ResponseEntity<InputStreamResource> response = reportingController.getActiveUsers();
        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=Active Employees.xlsx", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        // Add more assertions as needed
    }


}