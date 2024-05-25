package project.backend.employee;

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

    public int getPriority() {
        return priority;
    }
}

