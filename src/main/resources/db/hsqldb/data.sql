--------------------------------------------------------------------------------
-- Buildings

INSERT INTO buildings(id,address,income,name) VALUES (1,'c/Building 1',0,'building1');
INSERT INTO buildings(id,address,income,name) VALUES (2,'c/Building 2',0,'building2');
INSERT INTO buildings(id,address,income,name) VALUES (3,'c/Building 3',0,'building3');
INSERT INTO buildings(id,address,income,name) VALUES (4,'c/Building 4',0,'building4');

--------------------------------------------------------------------------------
-- Managers

INSERT INTO users(username,password,enabled) VALUES ('manager1','manager1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'manager1','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (1, 'Alice Manager', 'alice@manager.com', 'c/Manager 1', 'manager1');

INSERT INTO users(username,password,enabled) VALUES ('manager2','manager2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'manager2','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (2, 'Bob Manager', 'bob@manager.com', 'c/Manager 2', 'manager2');

INSERT INTO users(username,password,enabled) VALUES ('manager3','manager3',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'manager3','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (3, 'Charles Manager', 'charles@manager.com', 'c/Manager 3', 'manager3');

INSERT INTO users(username,password,enabled) VALUES ('manager4','manager4',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'manager4','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (4, 'Diane Manager', 'diane@manager.com', 'c/Manager 4', 'manager4');

--------------------------------------------------------------------------------
-- Employees

INSERT INTO users(username,password,enabled) VALUES ('employee1','employee1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'employee1','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (1, 'Alice Employee', 'alice@employee.com', 'c/Employee 1', 1, 'employee1');

INSERT INTO users(username,password,enabled) VALUES ('employee2','employee2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'employee2','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (2, 'Bob Employee', 'bob@employee.com', 'c/Employee 2', 1, 'employee2');

INSERT INTO users(username,password,enabled) VALUES ('employee3','employee3',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'employee3','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (3, 'Charles Employee', 'charles@employee.com', 'c/Employee 3', 1, 'employee3');

INSERT INTO users(username,password,enabled) VALUES ('employee4','employee4',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'employee4','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (4, 'Diane Employee', 'diane@employee.com', 'c/Employee 4', 1, 'employee4');

INSERT INTO users(username,password,enabled) VALUES ('employee5','employee5',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'employee5','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (5, 'Ernest Employee', 'ernest@employee.com', 'c/Employee 5', 2, 'employee5');

INSERT INTO users(username,password,enabled) VALUES ('employee6','employee6',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'employee6','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (6, 'Fatima Employee', 'fatima@employee.com', 'c/Employee 6', 2, 'employee6');

INSERT INTO users(username,password,enabled) VALUES ('employee7','employee7',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'employee7','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (7, 'Gloria Employee', 'gloria@employee.com', 'c/Employee 7', null, 'employee7');

INSERT INTO users(username,password,enabled) VALUES ('employee8','employee8',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'employee8','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (8, 'Hector Employee', 'hector@employee.com', 'c/Employee 8', null, 'employee8');

--------------------------------------------------------------------------------
-- Tasks

INSERT INTO tasks(id,complete,income,name) VALUES (1,false,1000,'task1');
INSERT INTO tasks(id,complete,income,name) VALUES (2,false,2000,'task2');
INSERT INTO tasks(id,complete,income,name) VALUES (3,false,3000,'task3');
INSERT INTO tasks(id,complete,income,name) VALUES (4,false,4000,'task4');

--------------------------------------------------------------------------------
-- Tools

INSERT INTO tools(id,name,building_id,task_id) VALUES (1,'tool1',1,null);
INSERT INTO tools(id,name,building_id,task_id) VALUES (2,'tool2',1,null);
INSERT INTO tools(id,name,building_id,task_id) VALUES (3,'tool3',2,null);
INSERT INTO tools(id,name,building_id,task_id) VALUES (4,'tool4',2,null);
