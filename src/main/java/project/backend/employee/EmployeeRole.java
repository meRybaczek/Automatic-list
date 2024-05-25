package project.backend.employee;

import java.util.Optional;
import java.util.stream.Stream;

public enum EmployeeRole {

    ADMIN(2),
    MANAGER(2),
    EMPLOYEE(1),
    SUSPENDED(0),
    DEACTIVATED(0),
    UNKNOWN(-1);

    private final int priority;

    EmployeeRole(int priority) {
        this.priority = priority;
    }

    public boolean isPriorityMatching(String actualRole, String expectedRole) {

        Optional<EmployeeRole> givenEmployeeRole = asEmployeeRole(actualRole);
        Optional<EmployeeRole> expectedEmployeeRole = asEmployeeRole(expectedRole);
        boolean areEmployeeRolesValid = givenEmployeeRole.isPresent() && expectedEmployeeRole.isPresent();

        if (areEmployeeRolesValid) {
            return givenEmployeeRole.get().priority >= expectedEmployeeRole.get().priority;
        }

        return false;
    }

    private static Optional<EmployeeRole> asEmployeeRole(String role) {
        return Stream.of(EmployeeRole.values()).filter(r -> r.name().equals(role)).findFirst();
    }
}

