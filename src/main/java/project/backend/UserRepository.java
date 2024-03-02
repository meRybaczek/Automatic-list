package project.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <Employee, Long> {
    public Employee findUserByFirstName(String name);
    public Employee findUserByLastName(String lastName);
    public Employee findUserByRfid(String rfid);
}
