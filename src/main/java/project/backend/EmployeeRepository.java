package project.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    Employee findEmployeeByFirstName(String name);
    Employee findEmployeeByLastName(String lastName);
    Employee findEmployeeByRfid(String rfid);

}

