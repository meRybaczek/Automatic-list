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

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;
import static project.backend.access.GateAccessStatus.*;

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

        EntranceLog entranceLog = createEntranceLog(employee, hasPriority);
        String message = getMessage(entranceLog);
        if (hasPriority) {
            log.info("New successful entrance, name: {} {}, rfid: {}, time: {}", employee.getFirstName(), employee.getLastName(), rfid, now());
            return new AccessResponse("%s %s".formatted(message, employee.getFirstName()), "Access granted.", true, false);
        }

        log.info("New failed entrance, name: {} {}, rfid: {}, time: {}", employee.getFirstName(), employee.getLastName(), rfid, now());
        return new AccessResponse("Hi %s".formatted(employee.getFirstName()), "Access denied!", false, false);
    }

    private String getMessage(EntranceLog entranceLog) {
        String welcomeMessage = "Hi";
        if (entranceLog.getGateAccessStatus() == null) {
            return welcomeMessage;
        }
        return switch (entranceLog.getGateAccessStatus()) {
            case IN, REJECTED -> welcomeMessage;
            case OUT -> "Bye";
            default -> throw new IllegalArgumentException("Unexpected log status: %s".formatted(entranceLog.getGateAccessStatus()));
        };
    }

    private EntranceLog createEntranceLog(Employee employee, boolean hasPriority) {
        List<EntranceLog> entranceLogList = entranceLogRepository.findEntranceLogByRfid(employee.getRfid());

        EntranceLog entranceLog = new EntranceLog(employee.getRfid(), employee.getStatus(), LocalDateTime.now());
        setEntranceLogStatus(hasPriority, entranceLog, entranceLogList);
        return entranceLogRepository.save(entranceLog);
    }

    private static void setEntranceLogStatus(boolean hasPriority, EntranceLog entranceLog, List<EntranceLog> entranceLogList) {
        if (!hasPriority) {
            entranceLog.setGateAccessStatus(REJECTED);
        }
        else if (entranceLogList.isEmpty() || OUT.equals(entranceLogList.get(0).getGateAccessStatus())) {
            entranceLog.setGateAccessStatus(IN);
        } else {
            entranceLog.setGateAccessStatus(OUT);
        }
    }

    private boolean isPriorityMatching(EmployeeRole actualRole, EmployeeRole expectedRole) {
        return actualRole.getPriority() >= expectedRole.getPriority();
    }
}
