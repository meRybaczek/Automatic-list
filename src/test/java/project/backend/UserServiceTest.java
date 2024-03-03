package project.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final boolean HAS_PERMISSION = true;
    private static final Employee EMPLOYEE = new Employee(NAME, LAST_NAME, RFID, HAS_PERMISSION);
    private final UserRepository userRepository = mock(UserRepository.class);
    private UserService userService = new UserService(userRepository);

    @Test
    void shouldAddUser() {
        //when
        userService.addUser(EMPLOYEE);

        //then
        verify(userRepository).save(EMPLOYEE);
    }

    @Test
    void shouldFindUserByName() {
        //given
        when(userRepository.findUserByFirstName(NAME)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = userService.getUserByFirstName(NAME);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindUserByName() {
        //when
        Employee receivedEmployee = userService.getUserByFirstName(NAME);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindUserByLastName() {
        //given
        when(userRepository.findUserByLastName(LAST_NAME)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = userService.getUserByLastName(LAST_NAME);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindUserByLastName() {
        //when
        Employee receivedEmployee = userService.getUserByLastName(LAST_NAME);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindUserByRfid() {
        //given
        when(userRepository.findUserByRfid(RFID)).thenReturn(EMPLOYEE);
        //when
        Employee receivedEmployee = userService.getUserByRfid(RFID);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }

    @Test
    void shouldNotFindUserByRfid() {
        //when
        Employee receivedEmployee = userService.getUserByRfid(RFID);

        //then
        assertThat(receivedEmployee).isNull();
    }

    @Test
    void shouldFindEmployeeById(){
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));

        //when
        Employee receivedEmployee = userService.getUser(1L);

        //then
        assertThat(receivedEmployee).isEqualTo(EMPLOYEE);
    }
    @Test
    void shouldDeactivateEmployee() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(EMPLOYEE));
        when(userRepository.save(EMPLOYEE)).thenReturn(EMPLOYEE);
        assertThat(EMPLOYEE.isHasPermission()).isTrue();
        //when
        Employee receivedEmployee = userService.deactivateUser(1L);
        //then
        assertThat(receivedEmployee.isHasPermission()).isFalse();
    }

    @Test
    void shouldActivateEmployee() {
        //given
        Employee employee = new Employee(NAME, LAST_NAME, RFID, false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(userRepository.save(employee)).thenReturn(employee);
        assertThat(employee.isHasPermission()).isFalse();
        //when
        Employee receivedEmployee = userService.activateUser(1L);
        //then
        assertThat(receivedEmployee.isHasPermission()).isTrue();
    }
}