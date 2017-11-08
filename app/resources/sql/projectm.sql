/*
Navicat MySQL Data Transfer

Source Server         : new
Source Server Version : 50627
Source Host           : localhost:3306
Source Database       : projectm

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2017-11-05 14:50:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `event`
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `eventName` varchar(255) DEFAULT NULL,
  `eventDescription` text,
  `eventDeadline` datetime DEFAULT NULL,
  `assignedBy` int(11) NOT NULL,
  `eventStatus` varchar(255) DEFAULT NULL,
  `createdAt` datetime NOT NULL,
  `assignedTo` int(11) DEFAULT NULL,
  `updatedAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `event_user` (`assignedBy`),
  CONSTRAINT `event_user` FOREIGN KEY (`assignedBy`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of event
-- ----------------------------
INSERT INTO `event` VALUES ('1', '2', 'title2', '232323', '2017-11-04 00:00:00', '2', 'started', '2017-11-04 23:42:05', '2', '2017-11-04 23:42:05');
INSERT INTO `event` VALUES ('2', '2', 'title2', '232323', '2017-11-04 00:00:00', '2', 'started', '2017-11-04 23:42:29', '2', '2017-11-04 23:42:29');
INSERT INTO `event` VALUES ('3', '2', 'title2', '232323', '2017-11-05 00:00:00', '2', 'started', '2017-11-05 13:05:58', '2', '2017-11-05 13:05:58');
INSERT INTO `event` VALUES ('4', '2', 'title2', '232323', '2017-11-05 00:00:00', '2', 'started', '2017-11-05 13:46:09', '2', '2017-11-05 13:46:09');

-- ----------------------------
-- Table structure for `group`
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(255) DEFAULT NULL,
  `groupDescription` text,
  `project_id` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_project` (`project_id`),
  CONSTRAINT `group_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES ('1', 'personal', null, '1', '0000-00-00 00:00:00', '0000-00-00 00:00:00');
INSERT INTO `group` VALUES ('2', 'G8', 'description', '2', '2017-11-04 23:21:45', '2017-11-04 23:21:48');

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msg_body` text,
  `timestamp` datetime NOT NULL,
  `group_id` int(11) DEFAULT NULL,
  `sender_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sender_id` (`sender_id`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for `project`
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectName` varchar(255) DEFAULT NULL,
  `projectDescription` text,
  `projectDeadline` datetime DEFAULT NULL,
  `creator` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_user` (`creator`),
  CONSTRAINT `project_user` FOREIGN KEY (`creator`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('1', 'personal', null, null, '1', '0000-00-00 00:00:00', '0000-00-00 00:00:00');
INSERT INTO `project` VALUES ('2', 'pm', 'description', '2017-11-26 23:20:47', '2', '2017-11-04 23:20:55', '2017-11-04 23:21:03');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `accountType` varchar(255) DEFAULT NULL,
  `facebook` varchar(255) DEFAULT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  `avatar` blob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `User_username_unique` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'sys_admin', null, null, null, null, '0000-00-00 00:00:00', '0000-00-00 00:00:00', null);
INSERT INTO `user` VALUES ('2', 'q45hu', 'q45hu@uwaterloo.ca', '123', 'normal', 'q45hu', '2017-11-04 23:19:49', '2017-11-04 23:19:54', null);

-- ----------------------------
-- Table structure for `user_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`group_id`,`user_id`),
  KEY `user_group2` (`user_id`),
  CONSTRAINT `user_group1` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_group2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES ('2', '2');

-- ----------------------------
-- Procedure structure for `assign_event`
-- ----------------------------
DROP PROCEDURE IF EXISTS `assign_event`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `assign_event`(
IN group_id int,
IN title varchar(255),
IN description varchar(255),
IN deadline datetime,
IN assignedBy int,
IN status varchar(255),
IN assignedTo int,
OUT event_id int
)
BEGIN

insert 
into event(group_id,eventName,eventDescription,eventDeadline,assignedBy,assignedTo,eventStatus,createdAt,updatedAt)
values(group_id,title,description,deadline,assignedBy,assignedTo,status,now(),now());

select max(id) into event_id
from event;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `delete_event`
-- ----------------------------
DROP PROCEDURE IF EXISTS `delete_event`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_event`(
IN event_id int
)
BEGIN

delete from `event`
where `event`.id = event_id;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_dates_having_events`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_dates_having_events`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_dates_having_events`(
IN user_id int,
IN eventStatus varchar(255)
)
BEGIN   
	
select DISTINCT date(eventDeadline) as eventDate
from event
where event.assignedTo = user_id 
and event.eventStatus = eventStatus;
  
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_event_by_date`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_event_by_date`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_event_by_date`(
IN userId int,
IN deadline datetime
)
BEGIN

select event.*,groupName
from event,`group`
where event.assignedTo = userId
and event.group_id = `group`.id
and date(`event`.eventDeadline) = deadline;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_event_by_group`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_event_by_group`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_event_by_group`(
IN groupId int,
IN userId int
)
BEGIN

select event.*,groupName
from event,`group`
where event.assignedTo = userId
and event.group_id = `group`.id
and `group`.id = groupId;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_event_by_id`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_event_by_id`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_event_by_id`(
IN eventId int
)
BEGIN

select event.*,groupName
from event,`group`
where event.id = eventId and event.group_id = `group`.id;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_groups_of_user`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_groups_of_user`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_groups_of_user`(
IN user_id int
)
BEGIN   
	SELECT  g.* FROM pm.user AS u, pm.user_group AS ug, pm.group AS g
  WHERE ug.user_id = u.id and u.id=user_id and g.id=ug.group_id;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `get_users_of_group`
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_users_of_group`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_users_of_group`(
IN group_id int
)
BEGIN   
	SELECT  u.* FROM pm.user AS u, pm.user_group AS ug, pm.group AS g
  WHERE ug.user_id = u.id and g.id = group_id and g.id=ug.group_id;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `insert_a_group_chat_msg`
-- ----------------------------
DROP PROCEDURE IF EXISTS `insert_a_group_chat_msg`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_a_group_chat_msg`(
IN group_id int,
IN msg_body varchar(10000),
IN msg_datetime datetime,
IN sender_id int
)
BEGIN   
	INSERT INTO message(msg_body,timestamp,group_id,sender_id) 
	values(msg_body,msg_datetime,group_id,sender_id);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `log_in_by_email`
-- ----------------------------
DROP PROCEDURE IF EXISTS `log_in_by_email`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `log_in_by_email`(
IN email varchar(255),
IN pwd varchar(255)
)
BEGIN   
select * 
from user 
where user.email = email 
and user.password = pwd;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `log_in_by_facebook`
-- ----------------------------
DROP PROCEDURE IF EXISTS `log_in_by_facebook`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `log_in_by_facebook`(
IN facebook varchar(255)
)
BEGIN   
select * 
from user 
where user.facebook = facebook;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `log_in_by_username`
-- ----------------------------
DROP PROCEDURE IF EXISTS `log_in_by_username`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `log_in_by_username`(
IN username varchar(255),
IN pwd varchar(255)
)
BEGIN   
	select * from user where user.username = username and user.password = pwd;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `register_new_account`
-- ----------------------------
DROP PROCEDURE IF EXISTS `register_new_account`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `register_new_account`(
IN username varchar(255),
IN email varchar(255),
IN pwd varchar(255),
IN accountType varchar(255),
OUT id int
)
BEGIN
IF exists(select id from user where user.email = email or user.username = username) THEN

select 0 into id;

ELSE
   
insert 
into user(username,email,password,accountType,createdAt,updatedAt,facebook)
values(username,email,pwd,accountType,now(),now(),"facebook");

select MAX(id) into id
from user;

END IF;

END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `update_status_of_event`
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_status_of_event`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_status_of_event`(
IN event_id int,
IN status varchar(255)
)
BEGIN

update `event`
set eventStatus = status
where `event`.id = event_id;

end
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `update_user_info`
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_user_info`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_user_info`(
IN id int,
IN email varchar(255),
IN facebook varchar(255),
OUT result int
)
BEGIN
IF exists(select id from user where user.email = email) THEN

select 0 into result;

ELSE

IF facebook != "facebook" and exists(select id from user where user.facebook = facebook) THEN

select 0 into result;

ELSE

UPDATE user
set email = email,facebook = facebook
where id = id;

select 1 into result;

end if;

end if; 
END
;;
DELIMITER ;
