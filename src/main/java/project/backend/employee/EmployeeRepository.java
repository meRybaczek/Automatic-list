package project.backend.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.reporting.ReportingDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Employee findEmployeeByFirstNameIgnoreCase(String name);
    Employee findEmployeeByLastNameIgnoreCase(String lastName);
    Employee findEmployeeByRfidIgnoreCase(String rfid);

    @Query(value = "SELECT new project.backend.reporting.ReportingDTO(emp.id, emp.firstName, emp.lastName, emp.rfid, emp.status, el.date, el.gateAccessStatus) FROM Employee emp JOIN EntranceLog el ON emp.rfid = el.employeeRfid WHERE el.date BETWEEN :startDate AND :endDate ORDER BY el.date DESC")
    List<ReportingDTO> findAllEntranceLogsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT new project.backend.reporting.ReportingDTO(emp.id, emp.firstName, emp.lastName, emp.rfid, emp.status, el.date, el.gateAccessStatus) FROM Employee emp JOIN EntranceLog el ON emp.rfid = el.employeeRfid WHERE emp.id = :id AND (el.date BETWEEN :startDate AND :endDate) ORDER BY el.date DESC")
    List<ReportingDTO> findAllEntranceLogsBetweenDatesForGivenEmployeeId(@Param("id") Long id, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT emp from Employee emp WHERE emp.status IN :statusSet")
    List<Employee> getEmployeesByRoles(@Param("statusSet")Set<EmployeeRole> statusSet);

    @Query(value = "SELECT emp from Employee emp WHERE emp.status <> 'DEACTIVATED'")
    List<Employee> getActiveEmployees();
}

