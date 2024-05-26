package project.backend.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.employee.Employee;
import project.backend.employee.EmployeeRepository;
import project.backend.logging.EntranceLogRepository;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntranceLogRepository entranceLogRepository;
    public ByteArrayInputStream getLogs(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    public ByteArrayInputStream getEmployee(long id, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    public ByteArrayInputStream getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return ExcelHelper.employeesToExcel(employees);
    }
}
