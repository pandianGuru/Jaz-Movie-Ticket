/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.23 : Database - jaz
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jaz` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `jaz`;

/*Table structure for table `booked_ticket` */

DROP TABLE IF EXISTS `booked_ticket`;

CREATE TABLE `booked_ticket` (
  `booked_id` bigint NOT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `booked_seats` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `movie_booked_date` date DEFAULT NULL,
  `movie_id` bigint DEFAULT NULL,
  `movie_name` varchar(255) DEFAULT NULL,
  `movie_time` varchar(255) DEFAULT NULL,
  `no_of_persons` int NOT NULL,
  `payment_id` bigint DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `screen_id` bigint DEFAULT NULL,
  `show_id` bigint DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`booked_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `booked_ticket` */

insert  into `booked_ticket`(`booked_id`,`amount`,`booked_seats`,`created_date`,`email_id`,`movie_booked_date`,`movie_id`,`movie_name`,`movie_time`,`no_of_persons`,`payment_id`,`payment_status`,`screen_id`,`show_id`,`user_name`) values 
(38,12.00,'490',NULL,'pandijaz4695@gmail.com','2021-05-30',1,'FROZEN','10 AM',1,37,'SUCCESS',4,1,'Pandi'),
(41,100.00,'0',NULL,'pandijaz4695@gmail.com','2021-05-30',1,'FROZEN','10 AM',1,40,'SUCCESS',4,1,'Pandi'),
(46,10022.00,'7',NULL,'pandijaz4695@gmail.com','2021-05-31',2,'DRAGON','10 AM',1,45,'SUCCESS',4,1,'Pandi');

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values 
(47);

/*Table structure for table `movie` */

DROP TABLE IF EXISTS `movie`;

CREATE TABLE `movie` (
  `id` bigint NOT NULL,
  `certificate` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `movie` */

insert  into `movie`(`id`,`certificate`,`description`,`duration`,`language`,`name`,`release_date`,`type`) values 
(1,'U/A','DRAMA','2 HRS','ENGLISH','FROZEN','2021-05-30','CARTOON'),
(2,'U/A','DRAMA','2 HRS','ENGLISH','DRAGON','2021-05-30','CARTOON'),
(42,'U/A','Wonderfull movie','2 Hrs','EN','Jhon Wick 2','0007-08-12','action');

/*Table structure for table `movie_available_shows` */

DROP TABLE IF EXISTS `movie_available_shows`;

CREATE TABLE `movie_available_shows` (
  `id` bigint NOT NULL,
  `show_timings` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `movie_available_shows` */

insert  into `movie_available_shows`(`id`,`show_timings`) values 
(1,'10 AM'),
(2,'06 AM');

/*Table structure for table `partial_ticket_booking` */

DROP TABLE IF EXISTS `partial_ticket_booking`;

CREATE TABLE `partial_ticket_booking` (
  `id` bigint NOT NULL,
  `booked_ticket_id` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_payment_done` bit(1) DEFAULT NULL,
  `movie_date` date DEFAULT NULL,
  `movie_id` bigint DEFAULT NULL,
  `no_of_persons` int DEFAULT NULL,
  `screen_id` bigint DEFAULT NULL,
  `seat_id` varchar(255) DEFAULT NULL,
  `seats` varchar(255) DEFAULT NULL,
  `show_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `partial_ticket_booking` */

insert  into `partial_ticket_booking`(`id`,`booked_ticket_id`,`created_date`,`is_active`,`is_payment_done`,`movie_date`,`movie_id`,`no_of_persons`,`screen_id`,`seat_id`,`seats`,`show_id`,`user_id`) values 
(3,NULL,'2021-05-30 16:12:09.213000','','\0','2021-05-30',2,1,4,'1','1',1,111),
(4,NULL,'2021-05-30 16:48:09.854000','\0','\0','2021-05-30',2,1,4,'1','1',1,111),
(5,NULL,'2021-05-30 16:57:24.701000','\0','\0','2021-05-30',2,5,4,'2','2,23,3,32,44',1,111),
(6,NULL,'2021-05-30 16:57:24.861000','\0','\0','2021-05-30',2,5,4,'23','2,23,3,32,44',1,111),
(7,NULL,'2021-05-30 16:57:24.878000','\0','\0','2021-05-30',2,5,4,'3','2,23,3,32,44',1,111),
(8,NULL,'2021-05-30 16:57:24.891000','\0','\0','2021-05-30',2,5,4,'32','2,23,3,32,44',1,111),
(9,NULL,'2021-05-30 16:57:24.907000','\0','\0','2021-05-30',2,5,4,'44','2,23,3,32,44',1,111),
(10,NULL,'2021-05-30 16:59:28.009000','\0','\0','2021-05-30',1,5,4,'2','2,23,3,32,44',1,111),
(11,NULL,'2021-05-30 16:59:28.094000','\0','\0','2021-05-30',1,5,4,'23','2,23,3,32,44',1,111),
(12,NULL,'2021-05-30 16:59:28.129000','\0','\0','2021-05-30',1,5,4,'3','2,23,3,32,44',1,111),
(13,NULL,'2021-05-30 16:59:28.162000','\0','\0','2021-05-30',1,5,4,'32','2,23,3,32,44',1,111),
(14,NULL,'2021-05-30 16:59:28.191000','\0','\0','2021-05-30',1,5,4,'44','2,23,3,32,44',1,111),
(15,NULL,'2021-05-30 17:00:55.985000','','\0','2021-05-30',1,2,4,'10','10,6',1,111),
(16,NULL,'2021-05-30 17:00:56.038000','','\0','2021-05-30',1,2,4,'6','10,6',1,111),
(17,NULL,'2021-05-30 17:18:25.411000','\0','\0','2021-05-30',1,2,4,'[6','[6, 10]',1,111),
(18,NULL,'2021-05-30 17:18:25.475000','\0','\0','2021-05-30',1,2,4,' 10]','[6, 10]',1,111),
(23,NULL,'2021-05-30 17:36:25.011000','\0','\0','2021-05-30',1,2,4,'18','18, 26',1,111),
(24,NULL,'2021-05-30 17:36:25.743000','\0','\0','2021-05-30',1,2,4,' 26','18, 26',1,111),
(25,NULL,'2021-05-30 17:38:50.700000','\0','\0','2021-05-30',1,2,4,'13','13, 17',1,111),
(26,NULL,'2021-05-30 17:38:52.324000','\0','\0','2021-05-30',1,2,4,' 17','13, 17',1,111),
(27,NULL,'2021-05-30 17:40:16.409000','\0','\0','2021-05-30',1,2,4,'24','24,26',1,111),
(28,NULL,'2021-05-30 17:40:16.738000','\0','\0','2021-05-30',1,2,4,'26','24,26',1,111),
(29,NULL,'2021-05-30 17:47:59.546000','\0','\0','2021-05-30',1,1,4,'29','29',1,111),
(31,NULL,'2021-05-30 17:48:54.958000','\0','\0','2021-05-30',1,1,4,'49','49',1,111),
(32,NULL,'2021-05-30 17:48:59.535000','\0','\0','2021-05-30',1,1,4,'419','419',1,111),
(34,38,'2021-05-30 17:52:20.881000','','','2021-05-30',1,1,4,'490','490',1,111),
(36,38,'2021-05-30 18:07:27.605000','','','2021-05-30',1,1,4,'490','490',1,2),
(39,41,'2021-05-30 19:56:20.053000','','','2021-05-30',1,1,4,'0','0',1,2),
(43,NULL,'2021-05-31 13:20:16.476000','\0','\0','2021-05-31',1,1,4,'7','7',1,2),
(44,46,'2021-05-31 13:24:09.564000','','','2021-05-31',2,1,4,'7','7',1,2);

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `id` bigint NOT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `bank_transaction_id` varchar(255) DEFAULT NULL,
  `bank_txn_id` varchar(255) DEFAULT NULL,
  `gate_way_name` varchar(255) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `response_code` varchar(255) DEFAULT NULL,
  `response_mess` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_date` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `payment` */

insert  into `payment`(`id`,`amount`,`bank_name`,`bank_transaction_id`,`bank_txn_id`,`gate_way_name`,`payment_mode`,`response_code`,`response_mess`,`status`,`transaction_date`,`transaction_id`) values 
(30,12.00,'HDFC Bank',NULL,'777001820095752','HDFC','DC','227','Your payment has been declined by your bank. Please try again or use a different method to complete the payment.','FAILURE','2021-05-30 17:48:13.0','20210530111212800110168612702685281'),
(33,12.00,'HDFC Bank',NULL,'777001020867703','HDFC','DC','01','Txn Success','SUCCESS','2021-05-30 17:49:21.0','20210530111212800110168371202680192'),
(35,12.00,'HDFC Bank',NULL,'777001382609828','HDFC','DC','01','Txn Success','SUCCESS','2021-05-30 17:52:26.0','20210530111212800110168351002672277'),
(37,12.00,'HDFC Bank',NULL,'17000672840','HDFC','NB','01','Txn Success','SUCCESS','2021-05-30 18:07:38.0','20210530111212800110168332502663767'),
(40,100.00,'Axis Bank',NULL,'18169499246','AXIS','NB','01','Txn Success','SUCCESS','2021-05-30 19:56:43.0','20210530111212800110168304902796847'),
(45,10022.00,'Bharat Bank',NULL,'14347417149','BHARAT','NB','01','Txn Success','SUCCESS','2021-05-31 13:24:29.0','20210531111212800110168042802818875');

/*Table structure for table `screen` */

DROP TABLE IF EXISTS `screen`;

CREATE TABLE `screen` (
  `id` bigint NOT NULL,
  `max_seat` int NOT NULL,
  `screen_name` varchar(255) DEFAULT NULL,
  `fk_show_id` bigint DEFAULT NULL,
  `fk_movie_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxvj4nrg0p5h8qg5vhmuo8b9n` (`fk_show_id`),
  KEY `FKpdrtgjir1dxdnj9b4pefekog` (`fk_movie_id`),
  CONSTRAINT `FKpdrtgjir1dxdnj9b4pefekog` FOREIGN KEY (`fk_movie_id`) REFERENCES `movie` (`id`),
  CONSTRAINT `FKpxvj4nrg0p5h8qg5vhmuo8b9n` FOREIGN KEY (`fk_show_id`) REFERENCES `movie_available_shows` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `screen` */

insert  into `screen`(`id`,`max_seat`,`screen_name`,`fk_show_id`,`fk_movie_id`) values 
(1,100,'Jaz-1',1,1),
(2,100,'Jaz-2',2,1),
(3,100,'Jaz-2',1,2),
(4,100,'Jaz-1',1,2);

/*Table structure for table `screen_info` */

DROP TABLE IF EXISTS `screen_info`;

CREATE TABLE `screen_info` (
  `id` bigint NOT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `screen_info` */

insert  into `screen_info`(`id`,`amount`,`name`) values 
(1,100.00,'Normal'),
(2,200.00,'Royal'),
(3,300.00,'VIP');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `login_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`city`,`gender`,`login_id`,`name`,`password`,`phone_no`) values 
(1,'Chennai','Male','pandijaz4695@gmai.com','Pandi','password','2334234'),
(2,'Chennai','Male','pandijaz4695@gmail.com','Pandi','password','2334234');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
