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

    public Employee getUser(long id){
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

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
