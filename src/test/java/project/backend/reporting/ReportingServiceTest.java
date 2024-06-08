package project.backend.reporting;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import project.backend.employee.Employee;
import project.backend.employee.EmployeeRepository;
import project.backend.employee.EmployeeRole;
import project.backend.logging.EntranceLogRepository;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportingServiceTest {

    private final EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
    private final EntranceLogRepository entranceLogRepository = Mockito.mock(EntranceLogRepository.class);

    private final ReportingService reportingService = new ReportingService(employeeRepository, entranceLogRepository);
        @Test
        public void test_returns_valid_excel_file_when_active_users_exist() {
            // Given
            LocalDate today = LocalDate.now();
            List<String> activeRfids = List.of("rfid1", "rfid2");
            Employee employee = new Employee(1L, "John", "Doe", "rfid1", EmployeeRole.EMPLOYEE);
            Employee employe2 = new Employee(2L, "Jane", "Smith", "rfid2", EmployeeRole.EMPLOYEE);
            List<Employee> activeEmployees = List.of(
                    employee, employe2
            );
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(activeRfids);
            when(employeeRepository.findEmployeesByRfidIgnoreCase(activeRfids)).thenReturn(activeEmployees);

            // When
            ByteArrayInputStream result = reportingService.getActiveUsers();

            // Then
            assertNotNull(result);
        }

        @Test
        public void test_correctly_fetches_active_users_based_on_todays_date() {
            // Given
            LocalDate today = LocalDate.now();
            List<String> activeRfids = List.of("rfid1");
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(activeRfids);

            // When
            reportingService.getActiveUsers();

            // Then
            verify(entranceLogRepository).findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        }

        @Test
        public void test_converts_list_of_active_employees_to_excel_file() {
            // Given
            LocalDate today = LocalDate.now();
            List<String> activeRfids = List.of("rfid1");
            List<Employee> activeEmployees = List.of(new Employee(1L, "John", "Doe", "rfid1", EmployeeRole.EMPLOYEE));
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(activeRfids);
            when(employeeRepository.findEmployeesByRfidIgnoreCase(activeRfids)).thenReturn(activeEmployees);

            // When
            ByteArrayInputStream result = reportingService.getActiveUsers();

            // Then
            assertNotNull(result);
        }

        @Test
        public void test_no_active_users_found_on_given_date() {
            // Given
            LocalDate today = LocalDate.now();
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(List.of());

            // When
            ByteArrayInputStream result = reportingService.getActiveUsers();

            // Then
            assertNotNull(result);
        }

        @Test
        public void test_employee_repository_returns_empty_list() {
            // Given
            LocalDate today = LocalDate.now();
            List<String> activeRfids = List.of("rfid1");
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(activeRfids);
            when(employeeRepository.findEmployeesByRfidIgnoreCase(activeRfids)).thenReturn(List.of());

            // When
            ByteArrayInputStream result = reportingService.getActiveUsers();

            // Then
            assertNotNull(result);
        }

        @Test
        public void test_entrance_log_repository_returns_empty_list() {
            // Given
            LocalDate today = LocalDate.now();
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(List.of());

            // When
            ByteArrayInputStream result = reportingService.getActiveUsers();

            // Then
            assertNotNull(result);
        }

        @Test
        public void test_invalid_or_corrupted_data_in_entrance_logs() {
            // Given
            LocalDate today = LocalDate.now();
            List<String> activeRfids = List.of("invalid_rfid");
            when(entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay())).thenReturn(activeRfids);
            when(employeeRepository.findEmployeesByRfidIgnoreCase(activeRfids)).thenThrow(new RuntimeException("Invalid data"));

            // When / Then
            assertThrows(RuntimeException.class, reportingService::getActiveUsers);
        }


}