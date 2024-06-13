CREATE TABLE EMPLOYEE (
                          id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(50),
                          last_name VARCHAR(50),
                          rfid VARCHAR(50),
                          status VARCHAR(50)
);

CREATE TABLE ROLE (
                      id INT NOT NULL PRIMARY KEY,
                      role_name VARCHAR(50)
);

CREATE TABLE ENTRANCE_LOG (
                              id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              employee_rfid VARCHAR(50),
                              status VARCHAR(50),
                              date TIMESTAMP,
                              gate_access_status VARCHAR(8)
);



CREATE TABLE GATE (
                      id INT NOT NULL PRIMARY KEY,
                      gate_number INT,
                      required_status VARCHAR(50)
);

INSERT INTO EMPLOYEE (first_name, last_name, rfid, status)
VALUES
    ('Iza', 'Wozniak', '34F7CF4F', 'UNKNOWN'),
    ('Pawel', 'Cz.', '4ae9415b', 'MANAGER'),
    ('Wojciech', 'R.', 'c0ebd609', 'MANAGER'),
    ('Darek', 'N.', '20ca8973', 'EMPLOYEE'),
    ('Maciej', 'S.', '8e8e7730', 'SUSPENDED');

INSERT INTO GATE(id, gate_number, required_status)
VALUES
    (1, 1, 'EMPLOYEE'),
    (2, 2, 'MANAGER');