package project.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @jakarta.persistence.Column(name = "id")
    private Long id;

    private String firstName;

    private String lastName;

    private String rfid;

    private boolean hasPermission;

    public Employee(String firstName, String lastName, String rfid, boolean hasPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.rfid = rfid;
        this.hasPermission = hasPermission;
    }
}
