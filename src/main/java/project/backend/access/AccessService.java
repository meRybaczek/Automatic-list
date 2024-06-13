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

    public AccessResponse getLastLog(String rfid) {
        EntranceLog entranceLog = getEntranceLogByRfid(rfid);
        String date;
        String status;

        if (entranceLog == null) {
            date = "No data";
            status = "No data";
        } else {
            date = entranceLog.getDate().toString().replace("T", " ");
            status = entranceLog.getGateAccessStatus().name();
        }



        return new AccessResponse("Status:" + status + " at:", "%s".formatted(date), false, false);
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
        EntranceLog entranceLogList = getEntranceLogByRfid(employee.getRfid());

        EntranceLog entranceLog = new EntranceLog(employee.getRfid(), employee.getStatus(), LocalDateTime.now());
        setEntranceLogStatus(hasPriority, entranceLog, entranceLogList);
        return entranceLogRepository.save(entranceLog);
    }

    private EntranceLog getEntranceLogByRfid(String rfid) {
        List<EntranceLog> entranceLogByRfid = entranceLogRepository.findEntranceLogByRfid(rfid);
        if (entranceLogByRfid.isEmpty()) {
            return null;
        }
        return entranceLogByRfid.get(0);
    }

    private static void setEntranceLogStatus(boolean hasPriority, EntranceLog newEntranceLog, EntranceLog receivedEntranceLog) {
        if (!hasPriority) {
            newEntranceLog.setGateAccessStatus(REJECTED);
        }
        else if (receivedEntranceLog == null || OUT.equals(receivedEntranceLog.getGateAccessStatus())) {
            newEntranceLog.setGateAccessStatus(IN);
        } else {
            newEntranceLog.setGateAccessStatus(OUT);
        }
    }

    private boolean isPriorityMatching(EmployeeRole actualRole, EmployeeRole expectedRole) {
        return actualRole.getPriority() >= expectedRole.getPriority();
    }
}
