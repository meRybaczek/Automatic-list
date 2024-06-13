package project.backend.employee;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public Employee addEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee(
                employeeDTO.firstName(), employeeDTO.lastName(), employeeDTO.rfid(), mapStatus(employeeDTO));

        return employeeRepository.save(employee);
    }

    private static EmployeeRole mapStatus(EmployeeDTO employeeDTO) {
        return Stream.of(EmployeeRole.values()).filter(r -> r.name().equalsIgnoreCase(employeeDTO.status())).findAny().orElse(EmployeeRole.UNKNOWN);
    }

    public Employee getEmployee(Long id){
        return employeeRepository.findById(id).orElseThrow();
    }

    public Employee getEmployeeByFirstName(String name){
        return employeeRepository.findEmployeeByFirstNameIgnoreCase(name);
    }

    public Employee getEmployeeByLastName(String lastName){
        return employeeRepository.findEmployeeByLastNameIgnoreCase(lastName);

    }


    public Employee getEmployeeByRfid(String rfid){
        return employeeRepository.findEmployeeByRfidIgnoreCase(rfid);
    }

    public Employee suspendEmployee(Long id) {
        Employee employee = this.getEmployee(id);
        employee.setStatus(EmployeeRole.SUSPENDED);

        return employeeRepository.save(employee);
    }
    public Employee deactivateEmployee(Long id) {
        Employee employee = this.getEmployee(id);

        employee.setStatus(EmployeeRole.DEACTIVATED);
        return employeeRepository.save(employee);
    }

    public Employee activateEmployee(Long id) {
        Employee employee = this.getEmployee(id);
        employee.setStatus(EmployeeRole.EMPLOYEE);
        return employeeRepository.save(employee);
    }
}
