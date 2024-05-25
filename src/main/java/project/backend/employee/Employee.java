package project.backend.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String status;


    public Employee(String firstName, String lastName, String rfid, boolean hasPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rfid = rfid;
    }
}
