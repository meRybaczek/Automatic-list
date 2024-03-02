package project.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    public User findUserByFirstName(String name);
    public User findUserByLastName(String lastName);
    public User findUserByRfid(String rfid);
}
