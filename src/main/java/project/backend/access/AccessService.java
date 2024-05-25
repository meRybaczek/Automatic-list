package project.backend.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.employee.Employee;
import project.backend.employee.EmployeeRole;
import project.backend.employee.EmployeeService;
import project.backend.logging.EntranceLog;
import project.backend.logging.EntranceLogRepository;

import static java.time.LocalTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccessService {

    @Autowired
    EmployeeService employeeService;

    private final GateRepository gateRepository;

    private final EntranceLogRepository entranceLogRepository;

    public AccessResponse logAccess(String rfid, Integer gateNumber) {
        Employee employee = employeeService.getEmployeeByRfid(rfid);
        if (employee == null) {
            log.error("Logged unauthorized person with {} rfid.", rfid);
            return new AccessResponse(EmployeeRole.UNKNOWN.name(), "Access denied!", false, true);
        }

        Gate gate = gateRepository.findGateByGateNumber(gateNumber);
        boolean hasPriority = this.isPriorityMatching(employee.getStatus(), gate.getRequiredStatus());

        EntranceLog entranceLog = new EntranceLog(rfid, employee.getStatus(), now().toString());
        entranceLogRepository.save(entranceLog);

        if (hasPriority) {
            log.info("New successful entrance, name: {} {}, rfid: {}, time: {}", employee.getFirstName(), employee.getLastName(), rfid, now());
            return new AccessResponse("Hi %s".formatted(employee.getFirstName()), "Access granted.", true, false);
        }

        log.info("New failed entrance, name: {} {}, rfid: {}, time: {}", employee.getFirstName(), employee.getLastName(), rfid, now());
        return new AccessResponse("Hi %s".formatted(employee.getFirstName()), "Access denied!", false, false);
    }

    public boolean isPriorityMatching(EmployeeRole actualRole, EmployeeRole expectedRole) {
        return actualRole.getPriority() >= expectedRole.getPriority();
    }
}
