--------------------------------------------------------------------------------
-- Buildings

INSERT INTO buildings(id,address,income,name) VALUES (1,'c/Building 1',0,'building1');
INSERT INTO buildings(id,address,income,name) VALUES (2,'c/Building 2',0,'building2');
INSERT INTO buildings(id,address,income,name) VALUES (3,'c/Building 3',0,'building3');
INSERT INTO buildings(id,address,income,name) VALUES (4,'c/Building 4',0,'building4');

--------------------------------------------------------------------------------
-- Managers

INSERT INTO users(username,password,enabled) VALUES ('manager1','$2a$10$feQ/FyvwwnEQHo.QD.nXxOIsaY7AnimsZdZXgAFfrIkM/G.XlaZu6',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'manager1','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (1, 'Alice Manager', 'alice@manager.com', 'c/Manager 1', 'manager1');

INSERT INTO users(username,password,enabled) VALUES ('manager2','$2a$10$8uPG5b81CZbnMy0VAsW96eDASkkAq9r.R38oTFSeE/LBZhsXhrVGi',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'manager2','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (2, 'Bob Manager', 'bob@manager.com', 'c/Manager 2', 'manager2');

INSERT INTO users(username,password,enabled) VALUES ('manager3','$2a$10$4N52RLgqFFMA3hs.ZjEy/uNzSeAdtW0UnzBMC2bw0LhVMBTb0dufi',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'manager3','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (3, 'Charles Manager', 'charles@manager.com', 'c/Manager 3', 'manager3');

INSERT INTO users(username,password,enabled) VALUES ('manager4','$2a$10$3DxhOMamCXcFyhGOUPmgVeU5iMBJxqTqh9OiS.S0DHM2ecxbrFHY2',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'manager4','manager');
INSERT INTO managers(id,name,email,address,username) VALUES (4, 'Diane Manager', 'diane@manager.com', 'c/Manager 4', 'manager4');

--------------------------------------------------------------------------------
-- Employees

INSERT INTO users(username,password,enabled) VALUES ('employee1','$2a$10$yvf.ssW15F9t5qHysr2bkOnmsHa8oXFkcBjgmnwMoqBkt99xDCBwa',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'employee1','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (1, 'Alice Employee', 'alice@employee.com', 'c/Employee 1', 1, 'employee1');

INSERT INTO users(username,password,enabled) VALUES ('employee2','$2a$10$NYqdZ4VQ8D5mEvaw/Bzz1.mKY1tOvo84x5SK1r9eAxea6SMbi9eFK',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'employee2','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (2, 'Bob Employee', 'bob@employee.com', 'c/Employee 2', 1, 'employee2');

INSERT INTO users(username,password,enabled) VALUES ('employee3','$2a$10$qe5sd35rgX7WfeiuLYU36ewChzXo/SceStkIsICcuam8/yiyYmmai',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'employee3','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (3, 'Charles Employee', 'charles@employee.com', 'c/Employee 3', 1, 'employee3');

INSERT INTO users(username,password,enabled) VALUES ('employee4','$2a$10$aA5hz2By2D1I6W/4ttTIEuBC7JMfpcCTzzwu24mu7ze2nLeAEpI7i',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'employee4','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (4, 'Diane Employee', 'diane@employee.com', 'c/Employee 4', 1, 'employee4');

INSERT INTO users(username,password,enabled) VALUES ('employee5','$2a$10$FtLqccRyq/Z/j64MIYdfKek1JZpbwgim7cWf3n1nhvum2.6G1gEAO',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'employee5','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (5, 'Ernest Employee', 'ernest@employee.com', 'c/Employee 5', 2, 'employee5');

INSERT INTO users(username,password,enabled) VALUES ('employee6','$2a$10$uAFwGpjd3fpecda1WQCJsujgw64YJz.YxfgZOSSah9Df/AGaCLYkG',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'employee6','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (6, 'Fatima Employee', 'fatima@employee.com', 'c/Employee 6', 2, 'employee6');

INSERT INTO users(username,password,enabled) VALUES ('employee7','$2a$10$lpvfWdXAVduzwgbazCniRuV0NaZPe.kdDPJ9Wj1aIFzHFZsucnlYO',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'employee7','employee');
INSERT INTO employees(id,name,email,address,building_id,username) VALUES (7, 'Gloria Employee', 'gloria@employee.com', 'c/Employee 7', null, 'employee7');

INSERT INTO users(username,password,enabled) VALUES ('employee8','$2a$10$eQ342L9nWPMJurTpBmgCtOe5eS9qXqcWxKG8UrKWonByRhaRnlSN.',TRUE);
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
