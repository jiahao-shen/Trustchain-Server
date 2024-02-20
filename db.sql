-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: localhost    Database: trustchain
-- ------------------------------------------------------
-- Server version	8.0.35-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api`
--

DROP TABLE IF EXISTS `api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api` (
  `id` bigint NOT NULL,
  `author` bigint NOT NULL,
  `organization` bigint NOT NULL,
  `name` varchar(32) NOT NULL,
  `url` varchar(1024) NOT NULL,
  `method` int NOT NULL,
  `introduction` varchar(1024) DEFAULT NULL,
  `category` int DEFAULT NULL,
  `authorize` varchar(32) DEFAULT NULL,
  `version` varchar(32) NOT NULL,
  `header_type` int DEFAULT NULL,
  `header` varchar(1024) DEFAULT NULL,
  `request_type` int DEFAULT NULL,
  `request` varchar(2014) DEFAULT NULL,
  `response_type` int DEFAULT NULL,
  `response` varchar(1024) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `author` (`author`),
  KEY `organization` (`organization`),
  CONSTRAINT `api_ibfk_1` FOREIGN KEY (`author`) REFERENCES `user` (`id`),
  CONSTRAINT `api_ibfk_2` FOREIGN KEY (`organization`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_invoke`
--

DROP TABLE IF EXISTS `api_invoke`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_invoke` (
  `serial_number` bigint NOT NULL,
  `id` bigint NOT NULL,
  `applicant` bigint NOT NULL,
  `author` bigint NOT NULL,
  `invoke_method` int NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` int NOT NULL,
  `comment` varchar(512) DEFAULT NULL,
  `apply_time` datetime NOT NULL,
  `reply_time` datetime DEFAULT NULL,
  `reply_message` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`serial_number`),
  KEY `id` (`id`),
  KEY `applicant` (`applicant`),
  KEY `author` (`author`),
  CONSTRAINT `api_invoke_ibfk_1` FOREIGN KEY (`id`) REFERENCES `api` (`id`),
  CONSTRAINT `api_invoke_ibfk_2` FOREIGN KEY (`applicant`) REFERENCES `user` (`id`),
  CONSTRAINT `api_invoke_ibfk_3` FOREIGN KEY (`author`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_invoke_log`
--

DROP TABLE IF EXISTS `api_invoke_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_invoke_log` (
  `id` bigint NOT NULL,
  `applicant` bigint NOT NULL,
  `request_type` int NOT NULL,
  `request` varchar(2014) DEFAULT NULL,
  `header_type` int DEFAULT NULL,
  `header` varchar(2014) DEFAULT NULL,
  `invoke_time` datetime NOT NULL,
  `response_type` int NOT NULL,
  `response` varchar(2014) DEFAULT NULL,
  `serial_number` bigint NOT NULL,
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_register`
--

DROP TABLE IF EXISTS `api_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_register` (
  `serial_number` bigint NOT NULL,
  `id` bigint DEFAULT NULL,
  `author` bigint NOT NULL,
  `organization` bigint NOT NULL,
  `name` varchar(32) NOT NULL,
  `url` varchar(1024) NOT NULL,
  `method` int NOT NULL,
  `introduction` varchar(1024) DEFAULT NULL,
  `category` int DEFAULT NULL,
  `authorize` varchar(32) DEFAULT NULL,
  `version` varchar(32) NOT NULL,
  `header_type` int DEFAULT NULL,
  `header` varchar(1024) DEFAULT NULL,
  `request_type` int DEFAULT NULL,
  `request` varchar(2014) DEFAULT NULL,
  `response_type` int DEFAULT NULL,
  `response` varchar(1024) DEFAULT NULL,
  `status` int NOT NULL,
  `apply_time` datetime NOT NULL,
  `reply_time` datetime DEFAULT NULL,
  `reply_message` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`serial_number`),
  KEY `id` (`id`),
  CONSTRAINT `api_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `api` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` bigint NOT NULL,
  `name` varchar(32) NOT NULL,
  `logo` varchar(1024) DEFAULT NULL,
  `type` int NOT NULL,
  `telephone` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `city` varchar(128) NOT NULL,
  `address` varchar(128) NOT NULL,
  `introduction` varchar(1024) DEFAULT NULL,
  `superior` bigint DEFAULT NULL,
  `provide_node` tinyint(1) NOT NULL,
  `num_nodes` int NOT NULL,
  `file` varchar(1024) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `superior` (`superior`),
  CONSTRAINT `organization_ibfk_1` FOREIGN KEY (`superior`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization_register`
--

DROP TABLE IF EXISTS `organization_register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization_register` (
  `serial_number` bigint NOT NULL,
  `id` bigint DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `logo` varchar(1024) DEFAULT NULL,
  `type` int NOT NULL,
  `telephone` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `city` varchar(128) NOT NULL,
  `address` varchar(128) NOT NULL,
  `introduction` varchar(1024) DEFAULT NULL,
  `superior` bigint DEFAULT NULL,
  `provide_node` tinyint(1) NOT NULL,
  `num_nodes` int NOT NULL,
  `file` varchar(1024) DEFAULT NULL,
  `status` int NOT NULL,
  `apply_time` datetime NOT NULL,
  `reply_time` datetime DEFAULT NULL,
  `reply_message` varchar(1024) DEFAULT NULL,
  `password` varchar(128) NOT NULL DEFAULT '123456',
  PRIMARY KEY (`serial_number`),
  UNIQUE KEY `name` (`name`),
  KEY `id` (`id`),
  CONSTRAINT `organization_register_ibfk_1` FOREIGN KEY (`id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  `organization` bigint NOT NULL,
  `created_time` datetime NOT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `organization` (`organization`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`organization`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-06 15:55:52
