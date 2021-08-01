--------------------------------------------------------------------------------
-- Managers

INSERT INTO users(username,password,enabled) VALUES ('manager1','manager1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'manager1','manager1');
INSERT INTO managers(id,name,email,address,username) VALUES (1, 'Alice Manager', 'alice@manager.com', 'c/Manager 1', 'manager1');

INSERT INTO users(username,password,enabled) VALUES ('manager2','manager2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'manager2','manager2');
INSERT INTO managers(id,name,email,address,username) VALUES (2, 'Bob Manager', 'bob@manager.com', 'c/Manager 2', 'manager2');

--------------------------------------------------------------------------------
-- Employees

INSERT INTO users(username,password,enabled) VALUES ('employee1','employee1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'employee1','employee1');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (1, 'Alice Employee', 'alice@employee.com', 'c/Employee 1', null, 'employee1');

INSERT INTO users(username,password,enabled) VALUES ('employee2','employee2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'employee2','employee2');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (2, 'Bob Employee', 'bob@employee.com', 'c/Employee 2', null, 'employee2');

INSERT INTO users(username,password,enabled) VALUES ('employee3','employee3',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'employee3','employee3');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (3, 'Charles Employee', 'charles@employee.com', 'c/Employee 3', null, 'employee3');

INSERT INTO users(username,password,enabled) VALUES ('employee4','employee4',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'employee4','employee4');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (4, 'Diane Employee', 'diane@employee.com', 'c/Employee 4', null, 'employee4');
