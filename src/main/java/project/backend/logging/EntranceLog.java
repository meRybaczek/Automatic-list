package project.backend.logging;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.backend.access.GateAccessStatus;
import project.backend.employee.EmployeeRole;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class EntranceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeRfid;

    @Enumerated(EnumType.STRING)
    private EmployeeRole status;

    @Getter
    private LocalDateTime date;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private GateAccessStatus gateAccessStatus;

    public EntranceLog(String employeeRfid, EmployeeRole status, LocalDateTime date) {
        this.employeeRfid = employeeRfid;
        this.status = status;
        this.date = date;
    }
}
