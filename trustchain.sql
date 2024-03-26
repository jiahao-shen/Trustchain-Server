CREATE DATABASE /*!32312 IF NOT EXISTS */ `trustchain` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `trustchain`;

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization`
(
    `id`                VARCHAR(32)  NOT NULL,
    `name`              VARCHAR(64)  NOT NULL,
    `type`              TINYINT(4)   NOT NULL,
    `telephone`         VARCHAR(32)  NOT NULL,
    `email`             VARCHAR(32)  NOT NULL,
    `city`              VARCHAR(32)  NOT NULL,
    `address`           VARCHAR(128) NOT NULL,
    `introduction`      VARCHAR(1024) DEFAULT NULL,
    `superior_id`       VARCHAR(32)   DEFAULT NULL,
    `creation_time`     DATETIME     NOT NULL,
    `registration_time` DATETIME     NOT NULL,
    `last_modified`     DATETIME     NOT NULL,
    `version`           VARCHAR(32)   DEFAULT NULL,
    `logo`              VARCHAR(1024) DEFAULT NULL,
    `file`              VARCHAR(1024) DEFAULT NULL,
    `is_delete`         TINYINT(4)    DEFAULT NULL,
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
    `apply_id`      VARCHAR(32)  NOT NULL,
    `apply_status`  TINYINT(4)   NOT NULL,
    `apply_time`    DATETIME     NOT NULL,
    `reply_time`    DATETIME      DEFAULT NULL,
    `reply_reason`  VARCHAR(1024) DEFAULT NULL,
    `id`            VARCHAR(32)   DEFAULT NULL,
    `name`          VARCHAR(64)  NOT NULL,
    `type`          TINYINT(4)   NOT NULL,
    `telephone`     VARCHAR(32)  NOT NULL,
    `email`         VARCHAR(32)  NOT NULL,
    `city`          VARCHAR(32)  NOT NULL,
    `address`       VARCHAR(128) NOT NULL,
    `introduction`  VARCHAR(1024) DEFAULT NULL,
    `superior_id`   VARCHAR(32)   DEFAULT NULL,
    `creation_time` DATETIME     NOT NULL,
    `logo`          VARCHAR(1024) DEFAULT NULL,
    `file`          VARCHAR(1024) DEFAULT NULL,
    `is_delete`     TINYINT(4)    DEFAULT NULL,
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
    `id`                VARCHAR(32)  NOT NULL,
    `username`          VARCHAR(64)  NOT NULL,
    `password`          VARCHAR(128) NOT NULL,
    `telephone`         VARCHAR(32)  NOT NULL,
    `email`             VARCHAR(32)  NOT NULL,
    `role`              TINYINT(4)   NOT NULL,
    `organization_id`   VARCHAR(32)  NOT NULL,
    `registration_time` DATETIME     NOT NULL,
    `last_modified`     DATETIME     NOT NULL,
    `version`           VARCHAR(32)   DEFAULT NULL,
    `logo`              VARCHAR(1024) DEFAULT NULL,
    `wallet_id`         VARCHAR(32)   DEFAULT NULL,
    `is_delete`         TINYINT(4)    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `organization_id` (`organization_id`, `username`),
    CONSTRAINT `user_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`),
    CONSTRAINT `user_ibfk_2` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `user_register`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_register`
(
    `apply_id`        VARCHAR(32)  NOT NULL,
    `apply_status`    TINYINT(4)   NOT NULL,
    `apply_time`      DATETIME     NOT NULL,
    `reply_time`      DATETIME      DEFAULT NULL,
    `reply_reason`    VARCHAR(1024) DEFAULT NULL,
    `id`              VARCHAR(32)   DEFAULT NULL,
    `username`        VARCHAR(64)  NOT NULL,
    `password`        VARCHAR(128) NOT NULL,
    `telephone`       VARCHAR(32)  NOT NULL,
    `email`           VARCHAR(32)  NOT NULL,
    `role`            TINYINT(4)   NOT NULL,
    `organization_id` VARCHAR(32)  NOT NULL,
    `logo`            VARCHAR(1024) DEFAULT NULL,
    `is_delete`       TINYINT(4)    DEFAULT NULL,
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
    `id`                VARCHAR(32) DEFAULT NULL,
    `user_id`           VARCHAR(32)   NOT NULL,
    `name`              VARCHAR(64)   NOT NULL,
    `price`             DOUBLE        NOT NULL,
    `protocol`          TINYINT(4)    NOT NULL,
    `url`               VARCHAR(2083) NOT NULL,
    `method`            TINYINT(4)    NOT NULL,
    `introduction`      VARCHAR(1024) NOT NULL,
    `visible`           TINYINT(4)    NOT NULL,
    `param`             JSON        DEFAULT NULL,
    `query`             JSON        DEFAULT NULL,
    `request_header`    JSON        DEFAULT NULL,
    `request_body`      JSON        DEFAULT NULL,
    `response_header`   JSON        DEFAULT NULL,
    `response_body`     JSON        DEFAULT NULL,
    `version`           VARCHAR(32) DEFAULT NULL,
    `registration_time` DATETIME      NOT NULL,
    `last_modified`     DATETIME      NOT NULL,
    `is_delete`         TINYINT(4)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `api_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `api_register`;
CREATE TABLE `api_register`
(
    `apply_id`        VARCHAR(32)   NOT NULL,
    `apply_status`    TINYINT(4)    NOT NULL,
    `apply_time`      DATETIME      NOT NULL,
    `reply_time`      DATETIME      DEFAULT NULL,
    `reply_reason`    VARCHAR(1024) DEFAULT NULL,
    `id`              VARCHAR(32)   DEFAULT NULL,
    `user_id`         VARCHAR(32)   NOT NULL,
    `name`            VARCHAR(64)   NOT NULL,
    `price`           DOUBLE        NOT NULL,
    `protocol`        TINYINT(4)    NOT NULL,
    `url`             VARCHAR(2083) NOT NULL,
    `method`          TINYINT(4)    NOT NULL,
    `introduction`    VARCHAR(1024) NOT NULL,
    `visible`         TINYINT(4)    NOT NULL,
    `param`           JSON          DEFAULT NULL,
    `query`           JSON          DEFAULT NULL,
    `request_header`  JSON          DEFAULT NULL,
    `request_body`    JSON          DEFAULT NULL,
    `response_header` JSON          DEFAULT NULL,
    `response_body`   JSON          DEFAULT NULL,
    `is_delete`       TINYINT(4)    DEFAULT NULL,
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
    `apply_id`      VARCHAR(32) NOT NULL,
    `apply_status`  TINYINT(4)  NOT NULL,
    `apply_time`    DATETIME    NOT NULL,
    `apply_reason`  VARCHAR(1024) DEFAULT NULL,
    `reply_time`    DATETIME      DEFAULT NULL,
    `reply_reason`  VARCHAR(1024) DEFAULT NULL,
    `invoke_status` TINYINT(4)  NOT NULL,
    `api_id`        VARCHAR(32) NOT NULL,
    `user_id`       VARCHAR(32) NOT NULL,
    `range`         TINYINT(4)  NOT NULL,
    `start_time`    DATETIME    NOT NULL,
    `end_time`      DATETIME    NOT NULL,
    `app_key`       VARCHAR(32)   DEFAULT NULL,
    `secret_key`    VARCHAR(32)   DEFAULT NULL,
    `is_delete`     TINYINT(4)    DEFAULT NULL,
    PRIMARY KEY (`apply_id`),
    KEY `api_id` (`api_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `api_invoke_apply_ibfk_1` FOREIGN KEY (`api_id`) REFERENCES `api` (`id`),
    CONSTRAINT `api_invoke_apply_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `api_invoke_log`;
CREATE TABLE `api_invoke_log`
(
    `log_id`          VARCHAR(32) NOT NULL,
    `apply_id`        VARCHAR(32) NOT NULL,
    `invoke_user_id`  VARCHAR(32) NOT NULL,
    `invoke_method`   TINYINT(4)  NOT NULL,
    `param`           JSON DEFAULT NULL,
    `query`           JSON DEFAULT NULL,
    `request_header`  JSON DEFAULT NULL,
    `request_body`    JSON DEFAULT NULL,
    `status_code`     INT(4)      NOT NULL,
    `error_message`   TEXT DEFAULT NULL,
    `response_header` JSON DEFAULT NULL,
    `response_body`   JSON DEFAULT NULL,
    `time`            DATETIME    NOT NULL,
    PRIMARY KEY (`log_id`),
    KEY `apply_id` (`apply_id`),
    KEY `invoke_user_id` (`invoke_user_id`),
    CONSTRAINT `api_invoke_log_ibfk_1` FOREIGN KEY (`apply_id`) REFERENCES `api_invoke_apply` (`apply_id`),
    CONSTRAINT `api_invoke_log_ibfk_2` FOREIGN KEY (`invoke_user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `wallet`;
CREATE TABLE `wallet`
(
    `id`                VARCHAR(32) NOT NULL,
    `user_id`           VARCHAR(32) NOT NULL,
    `balance`           DOUBLE      NOT NULL,
    `state`             TINYINT(4)  NOT NULL,
    `registration_time` DATETIME    NOT NULL,
    `last_modified`     DATETIME    NOT NULL,
    `is_delete`         TINYINT(4) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `wallet_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`
(
    `id`           VARCHAR(32) NOT NULL,
    `wallet_id`    VARCHAR(32) NOT NULL,
    `tx_id`        VARCHAR(32) NOT NULL,
    `amount`       DOUBLE      NOT NULL,
    `balance`      DOUBLE      NOT NULL,
    `balance_type` TINYINT(4)  NOT NULL,
    `method`       TINYINT(4)  NOT NULL,
    `channel`      TINYINT(4)  NOT NULL,
    `comment`      VARCHAR(1024) DEFAULT NULL,
    `time`         DATETIME    NOT NULL,
    `is_delete`    TINYINT(4)    DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `wallet_id` (`wallet_id`),
    KEY `tx_id` (`tx_id`),
    CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`wallet_id`) REFERENCES `wallet` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;