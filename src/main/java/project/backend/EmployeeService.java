package project.backend;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public Employee addEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id){
        return employeeRepository.findById(id).orElseThrow();
    }

    public Employee getEmployeeByFirstName(String name){
        return employeeRepository.findEmployeeByFirstName(name);
    }

    public Employee getEmployeeByLastName(String lastName){
        return employeeRepository.findEmployeeByLastName(lastName);
    }

    public Employee getEmployeeByRfid(String rfid){
        return employeeRepository.findEmployeeByRfid(rfid);
    }

    public Employee deactivateEmployee(Long id) {
        Employee employee = this.getEmployee(id);

        employee.setHasPermission(false);
        return employeeRepository.save(employee);
    }

    public Employee activateEmployee(Long id) {
        Employee employee = this.getEmployee(id);

        employee.setHasPermission(true);
        return employeeRepository.save(employee);
    }
}
