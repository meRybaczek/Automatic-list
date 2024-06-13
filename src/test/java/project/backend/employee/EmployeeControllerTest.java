package project.backend.employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final EmployeeDTO EMPLOYEE = new EmployeeDTO(NAME, LAST_NAME, RFID, "EMPLOYEE");

    private final EmployeeService employeeService = Mockito.mock(EmployeeService.class);
    private final EmployeeController employeeController = new EmployeeController(employeeService);

    @Test
    void shouldAddEmployee() {
        //when
        employeeController.addEmployee(EMPLOYEE);

        //then
        Mockito.verify(employeeService).addEmployee(EMPLOYEE);
    }

    @Test
    void shouldFindEmployeeById(){
        //when
        employeeController.getEmployeeById(1L);

        //then
        Mockito.verify(employeeService).getEmployee(1L);
    };
    @Test
    void shouldFindEmployeeByRfid(){
        //when
        employeeController.getEmployeeByRfid(RFID);

        //then
        Mockito.verify(employeeService).getEmployeeByRfid(RFID);
    };
    @Test
    void shouldFindEmployeeBySurname(){
        //when
        employeeController.getEmployeeBySurname(LAST_NAME);

        //then
        Mockito.verify(employeeService).getEmployeeByLastName(LAST_NAME);
    };
    @Test
    void shouldFindEmployeeByName(){
        //when
        employeeController.getEmployeeByName(NAME);

        //then
        Mockito.verify(employeeService).getEmployeeByFirstName(NAME);
    };
    @Test
    void shouldDeleteEmployee(){
        //when
        employeeController.deactivateEmployeeById(1L);

        //then
        Mockito.verify(employeeService).deactivateEmployee(1L);
    };

}