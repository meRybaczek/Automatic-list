package project.backend.logging;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import project.backend.employee.EmployeeRole;

@Entity
@NoArgsConstructor
public class EntranceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeRfid;

    @Enumerated(EnumType.STRING)
    private EmployeeRole status;

    private String date;

    public EntranceLog(String employeeRfid, EmployeeRole status, String date) {
        this.employeeRfid = employeeRfid;
        this.status = status;
        this.date = date;
    }
}
