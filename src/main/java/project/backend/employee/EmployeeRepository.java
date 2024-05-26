package project.backend.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Employee findEmployeeByFirstNameIgnoreCase(String name);
    Employee findEmployeeByLastNameIgnoreCase(String lastName);
    Employee findEmployeeByRfidIgnoreCase(String rfid);

}

