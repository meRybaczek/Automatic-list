package project.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String RFID = "rfid";
    private static final boolean HAS_PERMISSION = false;
    private static final Employee EMPLOYEE = new Employee(NAME, LAST_NAME, RFID, HAS_PERMISSION);

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void shouldAddUser() {
        //when
        userController.addUser(EMPLOYEE);

        //then
        verify(userService).addUser(EMPLOYEE);
    }

    @Test
    void shouldFindUserById(){
        //when
        userController.getUserById(1L);

        //then
        verify(userService).getUser(1L);
    };
    @Test
    void shouldFindUserByRfid(){
        //when
        userController.getUserByRfid(RFID);

        //then
        verify(userService).getUserByRfid(RFID);
    };
    @Test
    void shouldFindUserBySurname(){
        //when
        userController.getUserBySurname(LAST_NAME);

        //then
        verify(userService).getUserByLastName(LAST_NAME);
    };
    @Test
    void shouldFindUserByName(){
        //when
        userController.getUserByName(NAME);

        //then
        verify(userService).getUserByFirstName(NAME);
    };
    @Test
    void shouldDeleteUser(){
        //when
        userController.deleteUserById(1L);

        //then
        verify(userService).deleteById(1L);
    };

}