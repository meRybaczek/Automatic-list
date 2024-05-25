CREATE TABLE EMPLOYEE (
                          ID INT NOT NULL PRIMARY KEY,
                          FIRST_NAME VARCHAR(50),
                          LAST_NAME VARCHAR(50),
                          RFID VARCHAR(50),
                          STATUS VARCHAR(50)
);

CREATE TABLE ROLE (
                      ID INT NOT NULL PRIMARY KEY,
                      ROLE_NAME VARCHAR(50)
)

CREATE TABLE ENTRANCE_LOG (
                      ID INT NOT NULL PRIMARY KEY,
                      EMP_RFID VARCHAR(50),
                      STATUS VARCHAR(50),
                      DATE VARCHAR(50),
)

CREATE TABLE GATE (
                      ID INT NOT NULL PRIMARY KEY,
                      GATE_NUMBER VARCHAR(5),
                      REQUIRED_ROLE_NAME VARCHAR(50)
)

INSERT INTO EMPLOYEE (ID, FIRST_NAME, LAST_NAME, RFID)
VALUES
    ('1', 'Iza', 'Wozniak', '34F7CF4F'),
    ('2', 'Pawel', 'Cz.', '4ae9415b'),
    ('3', 'Wojciech', 'R.', 'c0ebd609'),
    ('4', 'Darek', 'N.', '20ca8973'),
    ('5', 'Maciej', 'S.', '8e8e7730');