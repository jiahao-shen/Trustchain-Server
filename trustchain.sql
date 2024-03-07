-- MariaDB dump 10.19  Distrib 10.8.3-MariaDB, for osx10.17 (x86_64)
--
-- Host: localhost    Database: trustchain
-- ------------------------------------------------------
-- Server version	10.8.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Current Database: `trustchain`
--

CREATE DATABASE /*!32312 IF NOT EXISTS */ `trustchain` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `trustchain`;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization`
(
    `id`                varchar(32)  NOT NULL,
    `name`              varchar(32)  NOT NULL,
    `type`              int(11)      NOT NULL,
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
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    KEY `superior_id` (`superior_id`),
    CONSTRAINT `organization_ibfk_1` FOREIGN KEY (`superior_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization`
    DISABLE KEYS */;
INSERT INTO `organization`
VALUES ('e675a62fa8f24e9ebc0cff4e1a1634c5', '北京航空航天大学', 3, '0106176562', 'support@buaa.edu.cn',
        '11,1101,110108', '北京市海淀区学院路37号',
        '北京航空航天大学（Beihang University）简称“北航”，位于首都北京市，是中华人民共和国工业和信息化部直属的全国重点大学，中央直管高校， 位列国家“双一流”、“985工程”、“211工程”重点建设高校，入选珠峰计划、2011计划、111计划、卓越工程师教育培养计划、国家建设高水平大学公派研究生项目、中国政府奖学金来华留学生接收院校、国家级新工科研究与实践项目、国家级大学生创新创业训练计划、国家大学生创新性实验计划、全国深化创新创业教育改革示范高校、强基计划试点高校，为国际宇航联合会、中欧精英大学联盟、中国-西班牙大学联盟、中俄工科大学联盟、中国高校行星科学联盟、中国人工智能教育联席会、全国高等军工院校课程思政联盟、W3C组织成员。',
        'feb15885025d4b8590928d55d9083140', '1952-10-25 00:00:00', '2024-03-07 19:38:58', '2024-03-07 19:38:58',
        '795312ec197240b0b3fdbb27e2966d6b', 'organization/5e87c537c4b2403bb2247d8cac035bc9.jpg',
        'organization/333352dd75a641ec8b99c0fb333e74a9.zip'),
       ('feb15885025d4b8590928d55d9083140', '数据资源可信共享平台', 1, '13915558435', '1843781563@qq.com',
        '11,1101,110108', '学院路37号',
        '我们致力于建立一个安全、高效的平台，帮助用户实现数据的可信共享。我们的平台提供了一个可靠的环境，使用户能够安全地共享和访问敏感数据，同时保护数据的隐私和安全。通过我们的平台，您可以与合作伙伴、研究机构和其他利益相关者共享数据，促进创新和合作。我们采用先进的技术和严格的安全措施来保护数据的完整性和机密性，并确保数据在传输和存储过程中得到适当的加密和权限控制。我们的平台还提供灵活的数据管理工具和分析功能，帮助用户探索和利用数据的潜力。无论您是企业、研究机构还是政府部门，我们的数据资源可信共享平台将为您提供一个可靠的合作平台，帮助您实现数据驱动的决策和创新。加入我们的平台，共享可信数据，推动您的业务和项目取得更大的成功！',
        NULL, '2024-03-05 00:00:00', '2024-03-05 16:46:15', '2024-03-05 16:46:15', 'e185479ade4d43e9a1862b96c534302c',
        NULL, NULL);
/*!40000 ALTER TABLE `organization`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_register`
--

DROP TABLE IF EXISTS `organization_register`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_register`
(
    `reg_id`        varchar(32)  NOT NULL,
    `reg_status`    int(11)      NOT NULL,
    `apply_time`    datetime     NOT NULL,
    `reply_time`    datetime      DEFAULT NULL,
    `reply_message` varchar(1024) DEFAULT NULL,
    `id`            varchar(32)   DEFAULT NULL,
    `name`          varchar(32)  NOT NULL,
    `type`          int(11)      NOT NULL,
    `telephone`     varchar(32)  NOT NULL,
    `email`         varchar(32)  NOT NULL,
    `city`          varchar(32)  NOT NULL,
    `address`       varchar(128) NOT NULL,
    `introduction`  varchar(1024) DEFAULT NULL,
    `superior_id`   varchar(32)   DEFAULT NULL,
    `creation_time` datetime     NOT NULL,
    `logo`          varchar(1024) DEFAULT NULL,
    `file`          varchar(1024) DEFAULT NULL,
    PRIMARY KEY (`reg_id`),
    UNIQUE KEY `name` (`name`),
    KEY `id` (`id`),
    CONSTRAINT `organization_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_register`
--

LOCK TABLES `organization_register` WRITE;
/*!40000 ALTER TABLE `organization_register`
    DISABLE KEYS */;
INSERT INTO `organization_register`
VALUES ('0a2896f9e182453f9ed56a0982f80e97', 2, '2024-03-07 15:45:32', '2024-03-07 19:38:58', NULL,
        'e675a62fa8f24e9ebc0cff4e1a1634c5', '北京航空航天大学', 3, '0106176562', 'support@buaa.edu.cn',
        '11,1101,110108', '北京市海淀区学院路37号',
        '北京航空航天大学（Beihang University）简称“北航”，位于首都北京市，是中华人民共和国工业和信息化部直属的全国重点大学，中央直管高校， 位列国家“双一流”、“985工程”、“211工程”重点建设高校，入选珠峰计划、2011计划、111计划、卓越工程师教育培养计划、国家建设高水平大学公派研究生项目、中国政府奖学金来华留学生接收院校、国家级新工科研究与实践项目、国家级大学生创新创业训练计划、国家大学生创新性实验计划、全国深化创新创业教育改革示范高校、强基计划试点高校，为国际宇航联合会、中欧精英大学联盟、中国-西班牙大学联盟、中俄工科大学联盟、中国高校行星科学联盟、中国人工智能教育联席会、全国高等军工院校课程思政联盟、W3C组织成员。',
        'feb15885025d4b8590928d55d9083140', '1952-10-25 00:00:00', 'tmp/5e87c537c4b2403bb2247d8cac035bc9.jpg',
        'tmp/333352dd75a641ec8b99c0fb333e74a9.zip'),
       ('eafc2cc2cfda4f07a5a97fe78cb5ee1d', 3, '2024-03-07 19:50:29', '2024-03-07 19:54:03', '证明文件要求不符合规定',
        NULL, '北京大学', 3, '1062751201', 'support@pku.edu.cn', '11,1101,110108', '北京市海淀区颐和园路5号',
        '北京大学（英语：Peking University，缩写：PKU），简称北大，创建于1898年，初名京师大学堂，成立之初为中国最高学府，同时也是国家最高教育行政机关，行使教育部职能，统管全国教育。中华民国建立后，校名改为北京大学校，后又改名为国立北京大学。1916年—1927年，蔡元培任北京大学校长时期，“循思想自由原则、取兼容并包之义”，推行改革，把北大办成以文、理两科为重点的综合性大学，使北京大学成为新文化运动和五四运动的策源地。1937年中华民国政府在平津作战失守北平后，北大与清华、南开迁昆明组建新的国立西南联合大学，留守北京的学者于1939-1945年在北京成立沦陷区北京大学招收留守北京的贫寒学生，1946年北京大学由昆明回迁北平，接收了沦陷区北大学生。1952年院系调整后，北京大学聚集了原北大、清华、燕大三校的自然科学、人文学者，奠定了北大文理学科在中国高校中长期领先的地位。2000年，北京医科大学并入北京大学，成为北京大学医学部。',
        'feb15885025d4b8590928d55d9083140', '1989-06-11 00:00:00', 'tmp/96fe8379432d4d298d23c1900c193cdf.png',
        'tmp/e61a7bc913b349c794ad631c6aae7c25.zip');
/*!40000 ALTER TABLE `organization_register`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user`
(
    `id`                varchar(32)  NOT NULL,
    `username`          varchar(32)  NOT NULL,
    `password`          varchar(128) NOT NULL,
    `telephone`         varchar(32)  NOT NULL,
    `email`             varchar(32)  NOT NULL,
    `role`              int(11)      NOT NULL,
    `organization_id`   varchar(32)  NOT NULL,
    `registration_time` datetime     NOT NULL,
    `last_modified`     datetime     NOT NULL,
    `version`           varchar(32)   DEFAULT NULL,
    `logo`              varchar(1024) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `organization_id` (`organization_id`, `username`),
    CONSTRAINT `user_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES ('852cceb530414f4685685066093defe5', 'admin', '$2a$10$wz5q.YmlZtEenThonxWt3ee2DOOY1/os/6Q80uY1ocbRfqXB0PEVS',
        '13915558435', '1843781563@qq.com', 1, 'feb15885025d4b8590928d55d9083140', '2024-03-05 17:04:17',
        '2024-03-05 17:04:17', NULL, NULL),
       ('ccf44c48755244bbb945b4d8c9877b63', 'admin', '$2a$10$m58TlRAbzJ4qfDUiq01NMO/K9Qgbrbx8iMwpZRea4OVkNSHekSMTm',
        '0106176562', 'support@buaa.edu.cn', 1, 'e675a62fa8f24e9ebc0cff4e1a1634c5', '2024-03-07 21:15:26',
        '2024-03-07 21:15:26', NULL, NULL);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_register`
--

DROP TABLE IF EXISTS `user_register`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_register`
(
    `reg_id`          varchar(32)  NOT NULL,
    `reg_status`      int(11)      NOT NULL,
    `apply_time`      datetime     NOT NULL,
    `reply_time`      datetime      DEFAULT NULL,
    `reply_message`   varchar(1024) DEFAULT NULL,
    `id`              varchar(32)   DEFAULT NULL,
    `username`        varchar(32)  NOT NULL,
    `password`        varchar(128) NOT NULL,
    `telephone`       varchar(32)  NOT NULL,
    `email`           varchar(32)  NOT NULL,
    `role`            int(11)      NOT NULL,
    `organization_id` varchar(32)  NOT NULL,
    `logo`            varchar(1024) DEFAULT NULL,
    PRIMARY KEY (`reg_id`),
    KEY `id` (`id`),
    KEY `organization_id` (`organization_id`),
    CONSTRAINT `user_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
    CONSTRAINT `user_register_ibfk_2` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_register`
--

LOCK TABLES `user_register` WRITE;
/*!40000 ALTER TABLE `user_register`
    DISABLE KEYS */;
INSERT INTO `user_register`
VALUES ('66883c9a2bdc4b649ee45c76499db800', 1, '2024-03-07 22:32:12', NULL, NULL, NULL, 'plus',
        '$2a$10$GT5M6VsIOu5yQbhYoT7YlugbYhsRiT8w00S1x1zXB74Ju3LRcrmRa', '13915558435', '1843781563@qq.com', 2,
        'e675a62fa8f24e9ebc0cff4e1a1634c5', 'tmp/679d08b9ae46425e9b59d6154ee21edd.JPG');
/*!40000 ALTER TABLE `user_register`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2024-03-07 23:01:04
