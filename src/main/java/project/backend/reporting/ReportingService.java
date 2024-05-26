package project.backend.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.employee.Employee;
import project.backend.employee.EmployeeRepository;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ByteArrayInputStream getLogs(LocalDate startDate, LocalDate endDate) {
        List<ReportingDAO> employeeLogs = employeeRepository.findAllEntranceLogsBetweenDates(startDate, endDate);
        return ExcelHelper.EmployeeLogsToExcel(employeeLogs);
    }

    public ByteArrayInputStream getLogsOfGivenEmployee(long id, LocalDate startDate, LocalDate endDate) {
        List<ReportingDAO> employeeLogs = employeeRepository.findAllEntranceLogsBetweenDatesForGivenEmployeeId(id, startDate, endDate);
        return ExcelHelper.EmployeeLogsToExcel(employeeLogs);
    }

    public ByteArrayInputStream getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return ExcelHelper.employeesToExcel(employees);
    }
}
