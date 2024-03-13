package project.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ArduinoController {

    @Autowired
    private final EmployeeService employeeService;

    @CrossOrigin
    @GetMapping("/{gateId}/")
    public ArduinoResponse sendSth(@RequestParam String uid, @PathVariable Integer gateId){
        return new ArduinoResponse("Welcome Name", "Logged IN.", true);//example response
        // when Employee is out:  return new ArduinoResponse("Bye Name", "Logged OUT.", true)

    }

    @CrossOrigin
    @GetMapping("/{gateId}/lastLogInfo/")
    public ArduinoResponse getLastLog(@RequestParam String uid, @PathVariable Integer gateId){
        return new ArduinoResponse("Logged <IN> at:", "2024/03/10 07:00", false);//example response

    }

}
