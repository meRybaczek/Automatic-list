package project.backend.employee;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final Employee EMPLOYEE = new Employee(NAME, LAST_NAME, RFID, EmployeeRole.EMPLOYEE);
    private final EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
    private final EmployeeService employeeService = new EmployeeService(employeeRepository);

    @Test
    void shouldAddEmployee() {
        //when
        employeeService.addEmployee(new EmployeeDTO(NAME, LAST_NAME, RFID, EmployeeRole.EMPLOYEE.name()));

        //then
        Mockito.verify(employeeRepository).save(EMPLOYEE);
    }

    @Test
    void shouldFindEmployeeByName() {
        //given

        Mockito.when(employeeRepository.findEmployeeByFirstNameIgnoreCase(NAME)).thenReturn(EMPLOYEE);



        //when
        Employee receivedEmployee = employeeService.getEmployeeByFirstName(NAME);

        //then
        Assertions.assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByName() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByFirstName(NAME);

        //then
        Assertions.assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeByLastName() {
        //given

        Mockito.when(employeeRepository.findEmployeeByLastNameIgnoreCase(LAST_NAME)).thenReturn(EMPLOYEE);

        //when
        Employee receivedEmployee = employeeService.getEmployeeByLastName(LAST_NAME);

        //then
        Assertions.assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByLastName() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByLastName(LAST_NAME);

        //then
        Assertions.assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeByRfid() {
        //given
        Mockito.when(employeeRepository.findEmployeeByRfidIgnoreCase(RFID)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = employeeService.getEmployeeByRfid(RFID);

        //then
        Assertions.assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByRfid() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByRfid(RFID);

        //then
        Assertions.assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeById(){
        //given
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));

        //when
        Employee receivedEmployee = employeeService.getEmployee(1L);

        //then
        Assertions.assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }
    @Test
    void shouldDeactivateEmployee() {
        //given
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));
        Mockito.when(employeeRepository.save(EMPLOYEE)).thenReturn(EMPLOYEE);
        Assertions.assertThat(EMPLOYEE.getStatus()).isEqualTo(EmployeeRole.EMPLOYEE);
        //when
        Employee receivedEmployee = employeeService.deactivateEmployee(1L);
        //then
        Assertions.assertThat(EMPLOYEE.getStatus()).isEqualTo(EmployeeRole.DEACTIVATED);
    }

    @Test
    void shouldActivateEmployee() {
        //given
        Employee employee = new Employee(NAME, LAST_NAME, RFID, EmployeeRole.DEACTIVATED);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Assertions.assertThat(employee.getStatus()).isEqualTo(EmployeeRole.DEACTIVATED);
        //when
        Employee receivedEmployee = employeeService.activateEmployee(1L);
        //then
        Assertions.assertThat(receivedEmployee.getStatus()).isEqualTo(EmployeeRole.EMPLOYEE);
    }


}