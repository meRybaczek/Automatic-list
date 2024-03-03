package project.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/id")
    public Employee getEmployeeById(@RequestParam long id) {
        return employeeService.getEmployee(id);
    }

    @GetMapping("/rfid")
    public Employee getEmployeeByRfid(@RequestParam String rfid) {
        return employeeService.getEmployeeByRfid(rfid);
    }

    @GetMapping("/lastName")
    public Employee getEmployeeBySurname(@RequestParam String lastName) {
        return employeeService.getEmployeeByLastName(lastName);
    }

    @GetMapping("/firstName")
    public Employee getEmployeeByName(@RequestParam String firstName) {
        return employeeService.getEmployeeByFirstName(firstName);
    }

    @PatchMapping("/deactivate")
    public Employee deactivateEmployeeById(@RequestParam Long id){
        return employeeService.deactivateEmployee(id);
    }

    @PatchMapping("/activate")
    public Employee activateEmployeeById(@RequestParam Long id){
        return employeeService.activateEmployee(id);
    }
}
