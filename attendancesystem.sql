/*
Navicat MySQL Data Transfer

Source Server         : 39.105.171.169_3306
Source Server Version : 50557
Source Host           : 39.105.171.169:3306
Source Database       : attendancesystem

Target Server Type    : MYSQL
Target Server Version : 50557
File Encoding         : 65001

Date: 2019-08-21 12:29:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `classid` varchar(20) NOT NULL,
  `college` varchar(40) DEFAULT NULL,
  `major` varchar(40) DEFAULT NULL,
  `grade` varchar(20) DEFAULT NULL,
  `tid` varchar(20) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`classid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses` (
  `courseid` int(20) NOT NULL,
  `tusername` varchar(20) DEFAULT NULL COMMENT '教师id',
  `name` varchar(255) DEFAULT NULL COMMENT '课程名',
  `currentweek` varchar(3) DEFAULT NULL,
  `property` varchar(20) DEFAULT NULL,
  `credit` double(11,2) DEFAULT NULL,
  `classweek` varchar(20) NOT NULL,
  `workweek` varchar(255) NOT NULL,
  `time` varchar(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `did` int(8) DEFAULT NULL,
  `nid` int(8) DEFAULT NULL,
  `isodd` enum('单','双') DEFAULT NULL,
  `signtime` int(11) NOT NULL DEFAULT '0',
  `ing` int(1) DEFAULT '0',
  `starttime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`courseid`,`classweek`,`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for dests
-- ----------------------------
DROP TABLE IF EXISTS `dests`;
CREATE TABLE `dests` (
  `did` int(8) NOT NULL AUTO_INCREMENT,
  `dest` varchar(150) NOT NULL,
  `longitude` double(10,6) DEFAULT NULL,
  `latitude` double(10,6) DEFAULT NULL,
  PRIMARY KEY (`did`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=247165564 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for macs
-- ----------------------------
DROP TABLE IF EXISTS `macs`;
CREATE TABLE `macs` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `did` int(11) NOT NULL,
  `mac` varchar(20) NOT NULL,
  `db` int(3) DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for notices
-- ----------------------------
DROP TABLE IF EXISTS `notices`;
CREATE TABLE `notices` (
  `nid` int(8) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) DEFAULT NULL,
  `sType` varchar(20) NOT NULL,
  `sendUsername` varchar(20) NOT NULL,
  `sendType` varchar(20) DEFAULT NULL,
  `recvUsername` varchar(20) DEFAULT NULL,
  `recvType` varchar(20) DEFAULT NULL,
  `data` blob NOT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `extra` blob,
  `outdate` int(1) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for plugins
-- ----------------------------
DROP TABLE IF EXISTS `plugins`;
CREATE TABLE `plugins` (
  `pluginid` int(8) NOT NULL,
  `name` varchar(30) NOT NULL,
  `fileName` varchar(30) NOT NULL,
  `mainClass` varchar(100) NOT NULL,
  `version` varchar(10) NOT NULL,
  `isOpen` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`pluginid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for stusigns
-- ----------------------------
DROP TABLE IF EXISTS `stusigns`;
CREATE TABLE `stusigns` (
  `sid` int(8) NOT NULL AUTO_INCREMENT,
  `cid` int(20) DEFAULT NULL,
  `nid` int(8) DEFAULT NULL,
  `uid` int(8) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for syllabus
-- ----------------------------
DROP TABLE IF EXISTS `syllabus`;
CREATE TABLE `syllabus` (
  `uid` varchar(20) NOT NULL,
  `cid` int(20) NOT NULL,
  `stime` int(11) DEFAULT '0',
  `totaltime` int(11) DEFAULT '0',
  PRIMARY KEY (`uid`,`cid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `username` varchar(20) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` enum('男','女') DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `level` enum('讲师','导师') DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pic` blob,
  `signtime` datetime DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for unreads
-- ----------------------------
DROP TABLE IF EXISTS `unreads`;
CREATE TABLE `unreads` (
  `uid` varchar(20) NOT NULL,
  `type` varchar(20) NOT NULL,
  `nid` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `classid` varchar(20) DEFAULT NULL,
  `schoolid` varchar(30) DEFAULT NULL,
  `schoolpw` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` enum('男','女') DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `pic` blob,
  `lastlogintime` datetime DEFAULT NULL,
  `signtime` datetime DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE,
  KEY `classid` (`classid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Procedure structure for updateIngByCid
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateIngByCid`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `updateIngByCid`(IN `cid` int(20))
BEGIN
	DECLARE _ing INT;
	SELECT ing INTO _ing FROM courses
	WHERE courseid = cid;
	IF _ing = 1 THEN
		UPDATE courses SET ing = 0 	WHERE courseid = cid;
	ELSE
		UPDATE courses SET ing = 1 	WHERE courseid = cid;
	END IF;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateStartTimeByCid
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateStartTimeByCid`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `updateStartTimeByCid`(IN `cid` int(20))
BEGIN
	UPDATE courses SET startTime = now()	WHERE courseid = cid;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateTotalTimeByCid
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateTotalTimeByCid`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `updateTotalTimeByCid`(IN `_cid` int(20))
BEGIN
	UPDATE syllabus SET totaltime= (totaltime+1)	WHERE cid = _cid;
END
;;
DELIMITER ;
