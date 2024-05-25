package project.backend.employee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String rfid;

    @Setter
    @Enumerated(EnumType.STRING)
    private EmployeeRole status;


    public Employee(String firstName, String lastName, String rfid, boolean hasPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rfid = rfid;
    }
}
