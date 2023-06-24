-- MariaDB dump 10.17  Distrib 10.5.6-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: db4
-- ------------------------------------------------------
-- Server version	10.5.6-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `db4`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db4` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db4`;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrollment` (
  `E_Pnumber` varchar(20) NOT NULL,
  `E_title` int(11) NOT NULL,
  PRIMARY KEY (`E_Pnumber`,`E_title`),
  KEY `E_title` (`E_title`),
  CONSTRAINT `enrollment_ibfk_1` FOREIGN KEY (`E_title`) REFERENCES `music` (`Musicnum`),
  CONSTRAINT `enrollment_playlist` FOREIGN KEY (`E_Pnumber`) REFERENCES `playlist` (`Pnumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES ('POP',10002),('POP',10011),('POP',10012),('POP',10013),('S_balad',10004),('S_balad',10005),('S_balad',10007),('S_balad',10008),('S_balad',10009),('S_Dance',10006),('S_Dance',10017),('S_DAnce',10020),('S_Dance',10021),('S_Dance',10022),('S_Hip-hop',10010),('S_Hip-hop',10014),('S_Hip-hop',10015),('S_Hip-hop',10018),('S_Hip-hop',10019);
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music` (
  `Musictitle` varchar(100) NOT NULL,
  `Musicpublisher` varchar(100) NOT NULL,
  `Genre` varchar(100) NOT NULL,
  `Agency` varchar(100) NOT NULL,
  `Streamingcount` int(11) DEFAULT NULL,
  `Mgr_num` varchar(20) NOT NULL,
  `Musicnum` int(11) NOT NULL,
  PRIMARY KEY (`Musicnum`),
  KEY `music_FK` (`Mgr_num`),
  CONSTRAINT `music_FK` FOREIGN KEY (`Mgr_num`) REFERENCES `musicmanager` (`MID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES ('Painkiller','Ruel','POP','dknow',44,'hso2341',10002),('To name','IU','balad','IU',35,'han5398',10004),('I cant love','monday kids','balad','balad',31,'han5398',10005),('Fake Love','Bangtan boys','Dance','Bighits',29,'hso2341',10006),('Snowflower','parkhyosin','balad','dknow',26,'hso2341',10007),('after-work bus','Lee jin ho','balad','Lee',24,'hso2341',10008),('Love two','Kim fill','balad','Kim',9,'hso2341',10009),('Outer Ring Road','Gary','Hip-hop','gary',11,'hso2341',10010),('2 soon','keshi','POP','dknow',16,'hso2341',10011),('blue','Kamal.','POP','dknow',17,'hso2341',10012),('All I Want For Christmas Is You','Mariah Carey','POP','dknow',18,'hso2341',10013),('VVS','miran and mush','Hip-hop','Show me',17,'hso2341',10014),('Freak','Rilboy','Hip-hop','Show me',15,'hso2341',10015),('DONT TOUCH ME','Refund Expeditionary Force','Dance','Refund Expeditionary Force',14,'hso2341',10016),('When We Disco','JYP','Dance','JYP',13,'hso2341',10017),('NUNU NANA','Jessi','Hip-hop','Jessi',5,'hso2341',10018),('How are you going?','Oban','Hip-hop','Oban',32,'hso2341',10019),('Dynamite','Bangtan boys','Dance','Bighits',1,'hso2341',10020),('Alreay 12o clock','Chung ha','Dance','Chung ha',23,'hso2341',10021),('Roly-Poly','Tiara','Dance','Tiara',0,'hso2341',10022);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musicmanager`
--

DROP TABLE IF EXISTS `musicmanager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musicmanager` (
  `MID` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Mname` varchar(20) NOT NULL,
  `Snumber` int(11) NOT NULL,
  `Mphone` varchar(20) NOT NULL,
  `Mpassword` varchar(100) NOT NULL,
  PRIMARY KEY (`MID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musicmanager`
--

LOCK TABLES `musicmanager` WRITE;
/*!40000 ALTER TABLE `musicmanager` DISABLE KEYS */;
INSERT INTO `musicmanager` VALUES ('han5398','busan','han',0,'010-9900-2341','53982341do'),('hso2341','busan','HanSeungU',1223,'010-7299-2341','53982341do');
/*!40000 ALTER TABLE `musicmanager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist` (
  `Pnumber` varchar(20) NOT NULL,
  `Prank` int(11) DEFAULT NULL,
  `Musicnumber` int(11) DEFAULT NULL,
  `P_UID` varchar(20) NOT NULL,
  PRIMARY KEY (`Pnumber`),
  KEY `playlist_FK` (`P_UID`),
  CONSTRAINT `playlist_FK` FOREIGN KEY (`P_UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES ('POP',-1,4,'hso2341'),('S_balad',-1,5,'hso2341'),('S_Dance',-1,5,'hso2341'),('S_Hip-hop',-1,5,'hso2341');
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `Grade` varchar(20) DEFAULT NULL,
  `UID` varchar(20) NOT NULL,
  `Uphone` varchar(20) NOT NULL,
  `Uaddress` varchar(100) NOT NULL,
  `Usnumber` varchar(20) NOT NULL,
  `Upassword` varchar(100) NOT NULL,
  `Mgr_UID` varchar(100) NOT NULL,
  `Remain` int(11) DEFAULT NULL,
  `Paynumber` int(11) DEFAULT 1,
  PRIMARY KEY (`UID`),
  KEY `user_FK` (`Mgr_UID`),
  CONSTRAINT `user_FK` FOREIGN KEY (`Mgr_UID`) REFERENCES `musicmanager` (`MID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Bronze','Han','010-0000-0000','Busan','0000000-0000000','Han','han5398',20,1),('Diamond','hso2341','010-0000-0000','Jeju','001223-3095812','53982341do','hso2341',9999,72),('Bronze','Seung','010-1111-1111','Daegu','111111-1111111','Seung','hso2341',3,1),('Bronze','U','010-3333-3333','Ulsan','333333','U','han5398',28,1),('Bronze','Woo','010-2222-2222','Daejeon','222222-2222222','Wo','han5398',24,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-06 11:27:46
