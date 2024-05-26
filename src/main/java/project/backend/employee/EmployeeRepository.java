package project.backend.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.reporting.ReportingDAO;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Employee findEmployeeByFirstNameIgnoreCase(String name);
    Employee findEmployeeByLastNameIgnoreCase(String lastName);
    Employee findEmployeeByRfidIgnoreCase(String rfid);

    @Query(value = "SELECT emp.id, emp.firstName, emp.lastName, emp.rfid, emp.status, el.date, el.gateAccessStatus FROM Employee emp JOIN EntranceLog el ON emp.rfid = el.employeeRfid WHERE el.date BETWEEN :startDate AND :endDate ORDER BY el.date DESC")
    List<ReportingDAO> findAllEntranceLogsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT emp.id, emp.firstName, emp.lastName, emp.rfid, emp.status, el.date, el.gateAccessStatus FROM Employee emp JOIN EntranceLog el ON emp.rfid = el.employeeRfid WHERE emp.id = :id AND (el.date BETWEEN :startDate AND :endDate) ORDER BY el.date DESC")
    List<ReportingDAO> findAllEntranceLogsBetweenDatesForGivenEmployeeId(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

