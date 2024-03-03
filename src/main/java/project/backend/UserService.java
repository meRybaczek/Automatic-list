package project.backend;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public Employee addUser(Employee employee) {

        return userRepository.save(employee);
    }

    public Employee getUser(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    public Employee getUserByFirstName(String name){
        return userRepository.findUserByFirstName(name);
    }

    public Employee getUserByLastName(String lastName){
        return userRepository.findUserByLastName(lastName);
    }

    public Employee getUserByRfid(String rfid){
        return userRepository.findUserByRfid(rfid);
    }

    public Employee deactivateUser(Long id) {
        Employee employee = this.getUser(id);

        employee.setHasPermission(false);
        return userRepository.save(employee);
    }

    public Employee activateUser(Long id) {
        Employee employee = this.getUser(id);

        employee.setHasPermission(true);
        return userRepository.save(employee);
    }
}
