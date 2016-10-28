/* Create database nebula_admin_service first before running the following DDL */

DROP DATABASE IF EXISTS nebula_admin_service;
CREATE DATABASE nebula_admin_service;

GRANT ALL PRIVILEGES ON nebula_admin_service.* TO 'nebula'@'%';
GRANT ALL PRIVILEGES ON nebula_admin_service.* TO 'nebula'@'localhost';
FLUSH PRIVILEGES;

USE nebula_admin_service;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `username` VARCHAR(10) NOT NULL COMMENT 'user name',
  `nickname` VARCHAR(20) DEFAULT NULL COMMENT 'nickname',
  `password` CHAR(32) NOT NULL COMMENT 'password md5',
  `accessId` CHAR(10) NOT NULL COMMENT 'the unique user id to access Nebula service',
  `secretKey` CHAR(32) NOT NULL COMMENT 'UUID without dashes for signature',
  `email`  VARCHAR(100) NOT NULL COMMENT 'email',
  `mobile` VARCHAR(50) DEFAULT NULL COMMENT 'mobile',
  `admin` TINYINT(1) DEFAULT 0 COMMENT '0: non-admin, 1: admin',
  `status` TINYINT(1) DEFAULT 1 COMMENT '0: disabled, 1: enabled.',
  `createdDate` DATETIME NOT NULL,
  `modifiedDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_username` (`username` )
) ENGINE=INNODB DEFAULT CHARSET=utf8;

# Admin User. Don't change the username!  Change the password(default is nebula) after the first login.
INSERT INTO `user` (username,nickname,password, accessId, secretKey, email,admin,createdDate,modifiedDate) VALUES ('admin','Administrator', '9614e8fbe2ec82892ec8ca5d702dea32', 'admin','9614e8fbe2ec82892ec8ca5d702dea32','admin@nebula-service.org',1, now(),now());

DROP TABLE IF EXISTS domain;

CREATE TABLE domain (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'domain id',
  `name` VARCHAR(10) NOT NULL COMMENT 'domain name',
  `dbUrl` VARCHAR(100) NOT NULL COMMENT 'db url',
  `redisHost` VARCHAR(100) NOT NULL COMMENT 'redisHost',
  `redisPort` INT(5) NOT NULL COMMENT 'redisPort',
  `description` VARCHAR(255) DEFAULT NULL COMMENT 'description',
  `status` TINYINT(1) DEFAULT 1 COMMENT '0: disabled, 1: normal, -1:locked.',
  `createdDate` DATETIME NOT NULL,
  `modifiedDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_name` (`name` )
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS userdomain;

CREATE TABLE userdomain (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'userDomain id',
  `username` VARCHAR(20) NOT NULL COMMENT 'user name',
  `domainName` VARCHAR(10) NOT NULL COMMENT 'domain name',
  `createdDate` DATETIME NOT NULL,
  `modifiedDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_user_domain` (`username`, `domainName` )
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `nebulas`;

CREATE TABLE `nebulas` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'nebula id',
  `host` VARCHAR(255) NOT NULL COMMENT 'nebula host/ip',
  `description` VARCHAR(255) DEFAULT NULL COMMENT 'description',
  `domainName` VARCHAR(10) DEFAULT NULL COMMENT 'domain name',
  `heartbeat`  DATETIME NOT NULL COMMENT 'heartbeat time',
  `createdDate` DATETIME NOT NULL,
  `modifiedDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_host` (`host` )
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create index idx_nebulas_domainName on nebulas(domainName);
