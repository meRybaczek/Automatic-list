package project.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private UserService userService;
    @PostMapping("/")
    public Employee addUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String rfid,
                            @RequestParam boolean hasPermission){
        return userService.addUser(firstName, lastName, rfid, hasPermission);
    }
    @GetMapping("/id")
    public Employee getUserById(@RequestParam long id){
        return userService.getUser(id);
    }
    @GetMapping("/rfid")
    public Employee getUserByRfid(@RequestParam String rfidUid){
        return userService.getUserByRfid(rfidUid);
    }
    @GetMapping("/ser")
    public Employee getUserBySurname(@RequestParam String lastName){
        return userService.getUserByLastName(lastName);
    }
    @GetMapping("/name")
    public Employee getUserByName(@RequestParam String firstName){
        return userService.getUserByFirstName(firstName);
    }
    @DeleteMapping("/")
    public void deleteUserById(@RequestParam Long id){
        userService.deleteById(id);
    }


}
