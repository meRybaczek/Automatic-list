package project.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final boolean HAS_PERMISSION = true;
    private static final Employee EMPLOYEE = new Employee(NAME, LAST_NAME, RFID, HAS_PERMISSION);
    private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
    private EmployeeService employeeService = new EmployeeService(employeeRepository);

    @Test
    void shouldAddEmployee() {
        //when
        employeeService.addEmployee(EMPLOYEE);

        //then
        verify(employeeRepository).save(EMPLOYEE);
    }

    @Test
    void shouldFindEmployeeByName() {
        //given
        when(employeeRepository.findEmployeeByFirstName(NAME)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = employeeService.getEmployeeByFirstName(NAME);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByName() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByFirstName(NAME);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeByLastName() {
        //given
        when(employeeRepository.findEmployeeByLastName(LAST_NAME)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = employeeService.getEmployeeByLastName(LAST_NAME);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByLastName() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByLastName(LAST_NAME);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeByRfid() {
        //given
        when(employeeRepository.findEmployeeByRfid(RFID)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = employeeService.getEmployeeByRfid(RFID);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindEmployeeByRfid() {
        //when
        Employee receivedEmployee = employeeService.getEmployeeByRfid(RFID);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeById(){
        //given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));

        //when
        Employee receivedEmployee = employeeService.getEmployee(1L);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }
    @Test
    void shouldDeactivateEmployee() {
        //given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));
        when(employeeRepository.save(EMPLOYEE)).thenReturn(EMPLOYEE);
        assertThat(EMPLOYEE.isHasPermission()).isTrue();
        //when
        Employee receivedEmployee = employeeService.deactivateEmployee(1L);
        //then
        assertThat(receivedEmployee.isHasPermission()).isFalse();
    }

    @Test
    void shouldActivateEmployee() {
        //given
        Employee employee = new Employee(NAME, LAST_NAME, RFID, false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        assertThat(employee.isHasPermission()).isFalse();
        //when
        Employee receivedEmployee = employeeService.activateEmployee(1L);
        //then
        assertThat(receivedEmployee.isHasPermission()).isTrue();
    }
}