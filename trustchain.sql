CREATE DATABASE /*!32312 IF NOT EXISTS */ `trustchain` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `trustchain`;

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization`
(
    `id`                varchar(32)  NOT NULL,
    `name`              varchar(64)  NOT NULL,
    `type`              tinyint(4)   NOT NULL,
    `telephone`         varchar(32)  NOT NULL,
    `email`             varchar(32)  NOT NULL,
    `city`              varchar(32)  NOT NULL,
    `address`           varchar(128) NOT NULL,
    `introduction`      varchar(1024) DEFAULT NULL,
    `superior_id`       varchar(32)   DEFAULT NULL,
    `creation_time`     datetime     NOT NULL,
    `registration_time` datetime     NOT NULL,
    `last_modified`     datetime     NOT NULL,
    `version`           varchar(32)   DEFAULT NULL,
    `logo`              varchar(1024) DEFAULT NULL,
    `file`              varchar(1024) DEFAULT NULL,
    `is_delete`         tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    KEY `superior_id` (`superior_id`),
    CONSTRAINT `organization_ibfk_1` FOREIGN KEY (`superior_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `organization_register`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_register`
(
    `apply_id`      varchar(32)  NOT NULL,
    `apply_status`  tinyint(4)   NOT NULL,
    `apply_time`    datetime     NOT NULL,
    `reply_time`    datetime      DEFAULT NULL,
    `reply_reason`  varchar(1024) DEFAULT NULL,
    `id`            varchar(32)   DEFAULT NULL,
    `name`          varchar(64)  NOT NULL,
    `type`          tinyint(4)   NOT NULL,
    `telephone`     varchar(32)  NOT NULL,
    `email`         varchar(32)  NOT NULL,
    `city`          varchar(32)  NOT NULL,
    `address`       varchar(128) NOT NULL,
    `introduction`  varchar(1024) DEFAULT NULL,
    `superior_id`   varchar(32)   DEFAULT NULL,
    `creation_time` datetime     NOT NULL,
    `logo`          varchar(1024) DEFAULT NULL,
    `file`          varchar(1024) DEFAULT NULL,
    `is_delete`     tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`apply_id`),
    UNIQUE KEY `name` (`name`),
    KEY `id` (`id`),
    CONSTRAINT `organization_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `id`                varchar(32)  NOT NULL,
    `username`          varchar(64)  NOT NULL,
    `password`          varchar(128) NOT NULL,
    `telephone`         varchar(32)  NOT NULL,
    `email`             varchar(32)  NOT NULL,
    `role`              tinyint(4)   NOT NULL,
    `organization_id`   varchar(32)  NOT NULL,
    `registration_time` datetime     NOT NULL,
    `last_modified`     datetime     NOT NULL,
    `version`           varchar(32)   DEFAULT NULL,
    `logo`              varchar(1024) DEFAULT NULL,
    `is_delete`         tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `organization_id` (`organization_id`, `username`),
    CONSTRAINT `user_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `user_register`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_register`
(
    `apply_id`        varchar(32)  NOT NULL,
    `apply_status`    tinyint(4)   NOT NULL,
    `apply_time`      datetime     NOT NULL,
    `reply_time`      datetime      DEFAULT NULL,
    `reply_reason`    varchar(1024) DEFAULT NULL,
    `id`              varchar(32)   DEFAULT NULL,
    `username`        varchar(64)  NOT NULL,
    `password`        varchar(128) NOT NULL,
    `telephone`       varchar(32)  NOT NULL,
    `email`           varchar(32)  NOT NULL,
    `role`            tinyint(4)   NOT NULL,
    `organization_id` varchar(32)  NOT NULL,
    `logo`            varchar(1024) DEFAULT NULL,
    `is_delete`       tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`apply_id`),
    KEY `id` (`id`),
    KEY `organization_id` (`organization_id`),
    CONSTRAINT `user_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
    CONSTRAINT `user_register_ibfk_2` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api`;
CREATE TABLE `api`
(
    `id`                varchar(32) DEFAULT NULL,
    `user_id`           varchar(32)   NOT NULL,
    `name`              varchar(64)   NOT NULL,
    `price`             double        NOT NULL,
    `protocol`          tinyint(4)    NOT NULL,
    `url`               varchar(2083) NOT NULL,
    `method`            tinyint(4)    NOT NULL,
    `introduction`      varchar(1024) NOT NULL,
    `visible`           tinyint(4)    NOT NULL,
    `param`             JSON        DEFAULT NULL,
    `query`             JSON        DEFAULT NULL,
    `request_header`    JSON        DEFAULT NULL,
    `request_body`      JSON        DEFAULT NULL,
    `response_header`   JSON        DEFAULT NULL,
    `response_body`     JSON        DEFAULT NULL,
    `version`           varchar(32) DEFAULT NULL,
    `registration_time` datetime      NOT NULL,
    `last_modified`     datetime      NOT NULL,
    `is_delete`         tinyint(4)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `api_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api_register`;
CREATE TABLE `api_register`
(
    `apply_id`        varchar(32)   NOT NULL,
    `apply_status`    tinyint(4)    NOT NULL,
    `apply_time`      datetime      NOT NULL,
    `reply_time`      datetime      DEFAULT NULL,
    `reply_reason`    varchar(1024) DEFAULT NULL,
    `id`              varchar(32)   DEFAULT NULL,
    `user_id`         varchar(32)   NOT NULL,
    `name`            varchar(64)   NOT NULL,
    `price`           double        NOT NULL,
    `protocol`        tinyint(4)    NOT NULL,
    `url`             varchar(2083) NOT NULL,
    `method`          tinyint(4)    NOT NULL,
    `introduction`    varchar(1024) NOT NULL,
    `visible`         tinyint(4)    NOT NULL,
    `param`           JSON          DEFAULT NULL,
    `query`           JSON          DEFAULT NULL,
    `request_header`  JSON          DEFAULT NULL,
    `request_body`    JSON          DEFAULT NULL,
    `response_header` JSON          DEFAULT NULL,
    `response_body`   JSON          DEFAULT NULL,
    `is_delete`       tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`apply_id`),
    KEY `id` (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `api_register_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `api_register_ibfk_2` FOREIGN KEY (`id`) REFERENCES `api` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `api_invoke_apply`;
CREATE TABLE `api_invoke_apply`
(
    `apply_id`      varchar(32) NOT NULL,
    `apply_status`  tinyint(4)  NOT NULL,
    `apply_time`    datetime    NOT NULL,
    `apply_reason`  varchar(1024) DEFAULT NULL,
    `reply_time`    datetime      DEFAULT NULL,
    `reply_reason`  varchar(1024) DEFAULT NULL,
    `invoke_status` tinyint(4)  NOT NULL,
    `api_id`        varchar(32) NOT NULL,
    `user_id`       varchar(32) NOT NULL,
    `range`         tinyint(4)  NOT NULL,
    `start_time`    datetime    NOT NULL,
    `end_time`      datetime    NOT NULL,
    `app_key`       varchar(32)   DEFAULT NULL,
    `secret_key`    varchar(32)   DEFAULT NULL,
    `is_delete`     tinyint(4)    DEFAULT NULL,
    PRIMARY KEY (`apply_id`),
    KEY `api_id` (`api_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `api_invoke_apply_ibfk_1` FOREIGN KEY (`api_id`) REFERENCES `api` (`id`),
    CONSTRAINT `api_invoke_apply_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


