package project.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final boolean HAS_PERMISSION = false;
    private static final Employee EMPLOYEE = new Employee(NAME, LAST_NAME, RFID, HAS_PERMISSION);

    private final EmployeeService employeeService = mock(EmployeeService.class);
    private final EmployeeController employeeController = new EmployeeController(employeeService);

    @Test
    void shouldAddEmployee() {
        //when
        employeeController.addEmployee(EMPLOYEE);

        //then
        verify(employeeService).addEmployee(EMPLOYEE);
    }

    @Test
    void shouldFindEmployeeById(){
        //when
        employeeController.getEmployeeById(1L);

        //then
        verify(employeeService).getEmployee(1L);
    };
    @Test
    void shouldFindEmployeeByRfid(){
        //when
        employeeController.getEmployeeByRfid(RFID);

        //then
        verify(employeeService).getEmployeeByRfid(RFID);
    };
    @Test
    void shouldFindEmployeeBySurname(){
        //when
        employeeController.getEmployeeBySurname(LAST_NAME);

        //then
        verify(employeeService).getEmployeeByLastName(LAST_NAME);
    };
    @Test
    void shouldFindEmployeeByName(){
        //when
        employeeController.getEmployeeByName(NAME);

        //then
        verify(employeeService).getEmployeeByFirstName(NAME);
    };
    @Test
    void shouldDeleteEmployee(){
        //when
        employeeController.deactivateEmployeeById(1L);

        //then
        verify(employeeService).deactivateEmployee(1L);
    };

}