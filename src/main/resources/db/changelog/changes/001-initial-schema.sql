CREATE TABLE EMPLOYEE (
                          ID INT NOT NULL PRIMARY KEY,
                          FIRST_NAME VARCHAR(50),
                          LAST_NAME VARCHAR(50),
                          RFID VARCHAR(50),
                          HAS_PERMISSION BIT
);

INSERT INTO EMPLOYEE (ID, FIRST_NAME, LAST_NAME, RFID, HAS_PERMISSION)
VALUES
    ('1', 'Iza', 'Wozniak', '34F7CF4F', false),
    ('2', 'Pawel', 'Cz.', 'C0EBD69', true),
    ('3', 'Wojciech', 'R.', 'BAA62640', true),
    ('4', 'Darek', 'N.', '20CA8973', true),
    ('5', 'Maciej', 'S.', 'C0BC073', true);