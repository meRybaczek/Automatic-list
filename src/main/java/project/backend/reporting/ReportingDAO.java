package project.backend.reporting;

import project.backend.access.GateAccessStatus;
import project.backend.employee.EmployeeRole;

import java.time.LocalDateTime;

public record ReportingDAO (Long id, String firstName, String lastName, String rfid, EmployeeRole status, LocalDateTime date, GateAccessStatus gateAccessStatus){
}
