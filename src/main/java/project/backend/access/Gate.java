package project.backend.access;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.backend.employee.EmployeeRole;

@Entity
@NoArgsConstructor
public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Integer gateNumber;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private EmployeeRole requiredStatus;
}
