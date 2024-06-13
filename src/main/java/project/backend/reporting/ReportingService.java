package project.backend.reporting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.backend.employee.Employee;
import project.backend.employee.EmployeeRepository;
import project.backend.employee.EmployeeRole;
import project.backend.logging.EntranceLogRepository;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final EmployeeRepository employeeRepository;

    private final EntranceLogRepository entranceLogRepository;

    public ByteArrayInputStream getLogs(LocalDate startDate, LocalDate endDate) {
        List<ReportingDTO> employeeLogs = employeeRepository.findAllEntranceLogsBetweenDates(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        return ExcelHelper.EmployeeLogsToExcel(employeeLogs);
    }

    public ByteArrayInputStream getLogsOfGivenEmployee(long id, LocalDate startDate, LocalDate endDate) {
        List<ReportingDTO> employeeLogs = employeeRepository.findAllEntranceLogsBetweenDatesForGivenEmployeeId(id, startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
        return ExcelHelper.EmployeeLogsToExcel(employeeLogs);
    }

    public ByteArrayInputStream getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return ExcelHelper.employeesToExcel(employees);
    }

    public ByteArrayInputStream getActiveUsers() {
        LocalDate today = LocalDate.now();
        List<String> activeEmployeesRfid = entranceLogRepository.findActiveEmployeesRfid(today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        List<Employee> employees = employeeRepository.findEmployeesByRfidIgnoreCase(activeEmployeesRfid);
        return ExcelHelper.employeesToExcel(employees);
    }

    public ByteArrayInputStream getEmployeesByRoles(List<String> rawEmployeeRoles) {
        Set<EmployeeRole> employeeRoles = mapRawRolesToSet(rawEmployeeRoles);
        List<Employee> employees = employeeRepository.getEmployeesByRoles(employeeRoles);
        return ExcelHelper.employeesToExcel(employees);
    }

    private static Set<EmployeeRole> mapRawRolesToSet(List<String> rawEmployeeRoles) {
        List<String> allRoles = Arrays.stream(EmployeeRole.values()).map(Enum::toString).toList();
        return rawEmployeeRoles.stream().filter(allRoles::contains).map(EmployeeRole::valueOf).collect(Collectors.toSet());
    }
}
