INSERT INTO pm.user (username, email, password,accountType,createdAt,updatedAt)
VALUES ('ellen', 'ex@example.com', 123,'normal',NOW(),now());
INSERT INTO pm.project (projectName, projectDescription,projectDeadline, creator,createdAt,updatedAt)
VALUES ('E3', 'testDescription','2017-11-01 12:00:00', 1,NOW(),now());
INSERT INTO pm.project (projectName, projectDescription,projectDeadline, creator,createdAt,updatedAt)
VALUES ('P2', 'testDescription','2017-11-02 12:00:00', 1,NOW(),now());
INSERT INTO pm.group (groupName, groupDescription, project_id,createdAt,updatedAt)
VALUES ('E3_G', 'testDescription', 2,NOW(),now());
INSERT INTO pm.group (groupName, groupDescription, project_id,createdAt,updatedAt)
VALUES ('E3_G1', 'testDescription', 3,NOW(),now());
INSERT INTO pm.user_group (group_id,user_id)
VALUES (4,1);
INSERT INTO pm.user_group (group_id,user_id)
VALUES (5,1);
