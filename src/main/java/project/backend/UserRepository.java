package project.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <Employee, Long> {
    Employee findUserByFirstName(String name);
    Employee findUserByLastName(String lastName);
    Employee findUserByRfid(String rfid);

}

