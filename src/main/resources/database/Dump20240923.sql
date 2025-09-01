-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: oku_dbpdv
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `box`
--

DROP TABLE IF EXISTS `box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `box` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `total` double DEFAULT '0',
  `status` int DEFAULT '0',
  `date_open` varchar(45) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `box`
--

LOCK TABLES `box` WRITE;
/*!40000 ALTER TABLE `box` DISABLE KEYS */;
/*!40000 ALTER TABLE `box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` varchar(191) NOT NULL,
  `nif` varchar(15) NOT NULL DEFAULT 'XXXXXXXXXX',
  `phone` varchar(30) NOT NULL,
  `email` varchar(255) NOT NULL,
  `country` int NOT NULL DEFAULT '0',
  `city` varchar(100) NOT NULL,
  `zip_code` varchar(15) NOT NULL,
  `state` varchar(50) NOT NULL DEFAULT 'desconhecido',
  `address` varchar(191) NOT NULL DEFAULT 'desconhecido',
  `status` int NOT NULL DEFAULT '1',
  `group_id` int NOT NULL DEFAULT '0',
  `isdefault` int NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'Consumidor Final','XXXXXXXXXXX','XXXXXXXXXXX','XXXXXXXXXXX',1,'Desconecido','AO','Desconehcido','Desconhecido',1,0,1,'2024-07-19 19:55:45',NULL,NULL),(2,'AngolaCable','1243124312','989898988','ang@gmail.com',3,'Maianga','AO','Luanda','Luanda M',0,0,0,'2024-09-21 12:12:20',NULL,NULL);
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `id` int NOT NULL AUTO_INCREMENT,
  `iso2` char(2) DEFAULT NULL,
  `short_name` varchar(80) NOT NULL DEFAULT '',
  `long_name` varchar(80) NOT NULL DEFAULT '',
  `iso3` char(3) DEFAULT NULL,
  `numcode` varchar(6) DEFAULT NULL,
  `un_member` varchar(12) DEFAULT NULL,
  `calling_code` varchar(8) DEFAULT NULL,
  `cctld` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'AF','Afghanistan','Islamic Republic of Afghanistan','AFG','004','yes','93','.af'),(2,'AX','Aland Islands','&Aring;land Islands','ALA','248','no','358','.ax'),(3,'AL','Albania','Republic of Albania','ALB','008','yes','355','.al'),(4,'DZ','Algeria','People\'s Democratic Republic of Algeria','DZA','012','yes','213','.dz'),(5,'AS','American Samoa','American Samoa','ASM','016','no','1+684','.as'),(6,'AD','Andorra','Principality of Andorra','AND','020','yes','376','.ad'),(7,'AO','Angola','Republic of Angola','AGO','024','yes','244','.ao'),(8,'AI','Anguilla','Anguilla','AIA','660','no','1+264','.ai'),(9,'AQ','Antarctica','Antarctica','ATA','010','no','672','.aq'),(10,'AG','Antigua and Barbuda','Antigua and Barbuda','ATG','028','yes','1+268','.ag'),(11,'AR','Argentina','Argentine Republic','ARG','032','yes','54','.ar'),(12,'AM','Armenia','Republic of Armenia','ARM','051','yes','374','.am'),(13,'AW','Aruba','Aruba','ABW','533','no','297','.aw'),(14,'AU','Australia','Commonwealth of Australia','AUS','036','yes','61','.au'),(15,'AT','Austria','Republic of Austria','AUT','040','yes','43','.at'),(16,'AZ','Azerbaijan','Republic of Azerbaijan','AZE','031','yes','994','.az'),(17,'BS','Bahamas','Commonwealth of The Bahamas','BHS','044','yes','1+242','.bs'),(18,'BH','Bahrain','Kingdom of Bahrain','BHR','048','yes','973','.bh'),(19,'BD','Bangladesh','People\'s Republic of Bangladesh','BGD','050','yes','880','.bd'),(20,'BB','Barbados','Barbados','BRB','052','yes','1+246','.bb'),(21,'BY','Belarus','Republic of Belarus','BLR','112','yes','375','.by'),(22,'BE','Belgium','Kingdom of Belgium','BEL','056','yes','32','.be'),(23,'BZ','Belize','Belize','BLZ','084','yes','501','.bz'),(24,'BJ','Benin','Republic of Benin','BEN','204','yes','229','.bj'),(25,'BM','Bermuda','Bermuda Islands','BMU','060','no','1+441','.bm'),(26,'BT','Bhutan','Kingdom of Bhutan','BTN','064','yes','975','.bt'),(27,'BO','Bolivia','Plurinational State of Bolivia','BOL','068','yes','591','.bo'),(28,'BQ','Bonaire, Sint Eustatius and Saba','Bonaire, Sint Eustatius and Saba','BES','535','no','599','.bq'),(29,'BA','Bosnia and Herzegovina','Bosnia and Herzegovina','BIH','070','yes','387','.ba'),(30,'BW','Botswana','Republic of Botswana','BWA','072','yes','267','.bw'),(31,'BV','Bouvet Island','Bouvet Island','BVT','074','no','NONE','.bv'),(32,'BR','Brazil','Federative Republic of Brazil','BRA','076','yes','55','.br'),(33,'IO','British Indian Ocean Territory','British Indian Ocean Territory','IOT','086','no','246','.io'),(34,'BN','Brunei','Brunei Darussalam','BRN','096','yes','673','.bn'),(35,'BG','Bulgaria','Republic of Bulgaria','BGR','100','yes','359','.bg'),(36,'BF','Burkina Faso','Burkina Faso','BFA','854','yes','226','.bf'),(37,'BI','Burundi','Republic of Burundi','BDI','108','yes','257','.bi'),(38,'KH','Cambodia','Kingdom of Cambodia','KHM','116','yes','855','.kh'),(39,'CM','Cameroon','Republic of Cameroon','CMR','120','yes','237','.cm'),(40,'CA','Canada','Canada','CAN','124','yes','1','.ca'),(41,'CV','Cape Verde','Republic of Cape Verde','CPV','132','yes','238','.cv'),(42,'KY','Cayman Islands','The Cayman Islands','CYM','136','no','1+345','.ky'),(43,'CF','Central African Republic','Central African Republic','CAF','140','yes','236','.cf'),(44,'TD','Chad','Republic of Chad','TCD','148','yes','235','.td'),(45,'CL','Chile','Republic of Chile','CHL','152','yes','56','.cl'),(46,'CN','China','People\'s Republic of China','CHN','156','yes','86','.cn'),(47,'CX','Christmas Island','Christmas Island','CXR','162','no','61','.cx'),(48,'CC','Cocos (Keeling) Islands','Cocos (Keeling) Islands','CCK','166','no','61','.cc'),(49,'CO','Colombia','Republic of Colombia','COL','170','yes','57','.co'),(50,'KM','Comoros','Union of the Comoros','COM','174','yes','269','.km'),(51,'CG','Congo','Republic of the Congo','COG','178','yes','242','.cg'),(52,'CK','Cook Islands','Cook Islands','COK','184','some','682','.ck'),(53,'CR','Costa Rica','Republic of Costa Rica','CRI','188','yes','506','.cr'),(54,'CI','Cote d\'ivoire (Ivory Coast)','Republic of C&ocirc;te D\'Ivoire (Ivory Coast)','CIV','384','yes','225','.ci'),(55,'HR','Croatia','Republic of Croatia','HRV','191','yes','385','.hr'),(56,'CU','Cuba','Republic of Cuba','CUB','192','yes','53','.cu'),(57,'CW','Curacao','Cura&ccedil;ao','CUW','531','no','599','.cw'),(58,'CY','Cyprus','Republic of Cyprus','CYP','196','yes','357','.cy'),(59,'CZ','Czech Republic','Czech Republic','CZE','203','yes','420','.cz'),(60,'CD','Democratic Republic of the Congo','Democratic Republic of the Congo','COD','180','yes','243','.cd'),(61,'DK','Denmark','Kingdom of Denmark','DNK','208','yes','45','.dk'),(62,'DJ','Djibouti','Republic of Djibouti','DJI','262','yes','253','.dj'),(63,'DM','Dominica','Commonwealth of Dominica','DMA','212','yes','1+767','.dm'),(64,'DO','Dominican Republic','Dominican Republic','DOM','214','yes','1+809, 8','.do'),(65,'EC','Ecuador','Republic of Ecuador','ECU','218','yes','593','.ec'),(66,'EG','Egypt','Arab Republic of Egypt','EGY','818','yes','20','.eg'),(67,'SV','El Salvador','Republic of El Salvador','SLV','222','yes','503','.sv'),(68,'GQ','Equatorial Guinea','Republic of Equatorial Guinea','GNQ','226','yes','240','.gq'),(69,'ER','Eritrea','State of Eritrea','ERI','232','yes','291','.er'),(70,'EE','Estonia','Republic of Estonia','EST','233','yes','372','.ee'),(71,'ET','Ethiopia','Federal Democratic Republic of Ethiopia','ETH','231','yes','251','.et'),(72,'FK','Falkland Islands (Malvinas)','The Falkland Islands (Malvinas)','FLK','238','no','500','.fk'),(73,'FO','Faroe Islands','The Faroe Islands','FRO','234','no','298','.fo'),(74,'FJ','Fiji','Republic of Fiji','FJI','242','yes','679','.fj'),(75,'FI','Finland','Republic of Finland','FIN','246','yes','358','.fi'),(76,'FR','France','French Republic','FRA','250','yes','33','.fr'),(77,'GF','French Guiana','French Guiana','GUF','254','no','594','.gf'),(78,'PF','French Polynesia','French Polynesia','PYF','258','no','689','.pf'),(79,'TF','French Southern Territories','French Southern Territories','ATF','260','no',NULL,'.tf'),(80,'GA','Gabon','Gabonese Republic','GAB','266','yes','241','.ga'),(81,'GM','Gambia','Republic of The Gambia','GMB','270','yes','220','.gm'),(82,'GE','Georgia','Georgia','GEO','268','yes','995','.ge'),(83,'DE','Germany','Federal Republic of Germany','DEU','276','yes','49','.de'),(84,'GH','Ghana','Republic of Ghana','GHA','288','yes','233','.gh'),(85,'GI','Gibraltar','Gibraltar','GIB','292','no','350','.gi'),(86,'GR','Greece','Hellenic Republic','GRC','300','yes','30','.gr'),(87,'GL','Greenland','Greenland','GRL','304','no','299','.gl'),(88,'GD','Grenada','Grenada','GRD','308','yes','1+473','.gd'),(89,'GP','Guadaloupe','Guadeloupe','GLP','312','no','590','.gp'),(90,'GU','Guam','Guam','GUM','316','no','1+671','.gu'),(91,'GT','Guatemala','Republic of Guatemala','GTM','320','yes','502','.gt'),(92,'GG','Guernsey','Guernsey','GGY','831','no','44','.gg'),(93,'GN','Guinea','Republic of Guinea','GIN','324','yes','224','.gn'),(94,'GW','Guinea-Bissau','Republic of Guinea-Bissau','GNB','624','yes','245','.gw'),(95,'GY','Guyana','Co-operative Republic of Guyana','GUY','328','yes','592','.gy'),(96,'HT','Haiti','Republic of Haiti','HTI','332','yes','509','.ht'),(97,'HM','Heard Island and McDonald Islands','Heard Island and McDonald Islands','HMD','334','no','NONE','.hm'),(98,'HN','Honduras','Republic of Honduras','HND','340','yes','504','.hn'),(99,'HK','Hong Kong','Hong Kong','HKG','344','no','852','.hk'),(100,'HU','Hungary','Hungary','HUN','348','yes','36','.hu'),(101,'IS','Iceland','Republic of Iceland','ISL','352','yes','354','.is'),(102,'IN','India','Republic of India','IND','356','yes','91','.in'),(103,'ID','Indonesia','Republic of Indonesia','IDN','360','yes','62','.id'),(104,'IR','Iran','Islamic Republic of Iran','IRN','364','yes','98','.ir'),(105,'IQ','Iraq','Republic of Iraq','IRQ','368','yes','964','.iq'),(106,'IE','Ireland','Ireland','IRL','372','yes','353','.ie'),(107,'IM','Isle of Man','Isle of Man','IMN','833','no','44','.im'),(108,'IL','Israel','State of Israel','ISR','376','yes','972','.il'),(109,'IT','Italy','Italian Republic','ITA','380','yes','39','.jm'),(110,'JM','Jamaica','Jamaica','JAM','388','yes','1+876','.jm'),(111,'JP','Japan','Japan','JPN','392','yes','81','.jp'),(112,'JE','Jersey','The Bailiwick of Jersey','JEY','832','no','44','.je'),(113,'JO','Jordan','Hashemite Kingdom of Jordan','JOR','400','yes','962','.jo'),(114,'KZ','Kazakhstan','Republic of Kazakhstan','KAZ','398','yes','7','.kz'),(115,'KE','Kenya','Republic of Kenya','KEN','404','yes','254','.ke'),(116,'KI','Kiribati','Republic of Kiribati','KIR','296','yes','686','.ki'),(117,'XK','Kosovo','Republic of Kosovo','---','---','some','381',''),(118,'KW','Kuwait','State of Kuwait','KWT','414','yes','965','.kw'),(119,'KG','Kyrgyzstan','Kyrgyz Republic','KGZ','417','yes','996','.kg'),(120,'LA','Laos','Lao People\'s Democratic Republic','LAO','418','yes','856','.la'),(121,'LV','Latvia','Republic of Latvia','LVA','428','yes','371','.lv'),(122,'LB','Lebanon','Republic of Lebanon','LBN','422','yes','961','.lb'),(123,'LS','Lesotho','Kingdom of Lesotho','LSO','426','yes','266','.ls'),(124,'LR','Liberia','Republic of Liberia','LBR','430','yes','231','.lr'),(125,'LY','Libya','Libya','LBY','434','yes','218','.ly'),(126,'LI','Liechtenstein','Principality of Liechtenstein','LIE','438','yes','423','.li'),(127,'LT','Lithuania','Republic of Lithuania','LTU','440','yes','370','.lt'),(128,'LU','Luxembourg','Grand Duchy of Luxembourg','LUX','442','yes','352','.lu'),(129,'MO','Macao','The Macao Special Administrative Region','MAC','446','no','853','.mo'),(130,'MK','North Macedonia','Republic of North Macedonia','MKD','807','yes','389','.mk'),(131,'MG','Madagascar','Republic of Madagascar','MDG','450','yes','261','.mg'),(132,'MW','Malawi','Republic of Malawi','MWI','454','yes','265','.mw'),(133,'MY','Malaysia','Malaysia','MYS','458','yes','60','.my'),(134,'MV','Maldives','Republic of Maldives','MDV','462','yes','960','.mv'),(135,'ML','Mali','Republic of Mali','MLI','466','yes','223','.ml'),(136,'MT','Malta','Republic of Malta','MLT','470','yes','356','.mt'),(137,'MH','Marshall Islands','Republic of the Marshall Islands','MHL','584','yes','692','.mh'),(138,'MQ','Martinique','Martinique','MTQ','474','no','596','.mq'),(139,'MR','Mauritania','Islamic Republic of Mauritania','MRT','478','yes','222','.mr'),(140,'MU','Mauritius','Republic of Mauritius','MUS','480','yes','230','.mu'),(141,'YT','Mayotte','Mayotte','MYT','175','no','262','.yt'),(142,'MX','Mexico','United Mexican States','MEX','484','yes','52','.mx'),(143,'FM','Micronesia','Federated States of Micronesia','FSM','583','yes','691','.fm'),(144,'MD','Moldava','Republic of Moldova','MDA','498','yes','373','.md'),(145,'MC','Monaco','Principality of Monaco','MCO','492','yes','377','.mc'),(146,'MN','Mongolia','Mongolia','MNG','496','yes','976','.mn'),(147,'ME','Montenegro','Montenegro','MNE','499','yes','382','.me'),(148,'MS','Montserrat','Montserrat','MSR','500','no','1+664','.ms'),(149,'MA','Morocco','Kingdom of Morocco','MAR','504','yes','212','.ma'),(150,'MZ','Mozambique','Republic of Mozambique','MOZ','508','yes','258','.mz'),(151,'MM','Myanmar (Burma)','Republic of the Union of Myanmar','MMR','104','yes','95','.mm'),(152,'NA','Namibia','Republic of Namibia','NAM','516','yes','264','.na'),(153,'NR','Nauru','Republic of Nauru','NRU','520','yes','674','.nr'),(154,'NP','Nepal','Federal Democratic Republic of Nepal','NPL','524','yes','977','.np'),(155,'NL','Netherlands','Kingdom of the Netherlands','NLD','528','yes','31','.nl'),(156,'NC','New Caledonia','New Caledonia','NCL','540','no','687','.nc'),(157,'NZ','New Zealand','New Zealand','NZL','554','yes','64','.nz'),(158,'NI','Nicaragua','Republic of Nicaragua','NIC','558','yes','505','.ni'),(159,'NE','Niger','Republic of Niger','NER','562','yes','227','.ne'),(160,'NG','Nigeria','Federal Republic of Nigeria','NGA','566','yes','234','.ng'),(161,'NU','Niue','Niue','NIU','570','some','683','.nu'),(162,'NF','Norfolk Island','Norfolk Island','NFK','574','no','672','.nf'),(163,'KP','North Korea','Democratic People\'s Republic of Korea','PRK','408','yes','850','.kp'),(164,'MP','Northern Mariana Islands','Northern Mariana Islands','MNP','580','no','1+670','.mp'),(165,'NO','Norway','Kingdom of Norway','NOR','578','yes','47','.no'),(166,'OM','Oman','Sultanate of Oman','OMN','512','yes','968','.om'),(167,'PK','Pakistan','Islamic Republic of Pakistan','PAK','586','yes','92','.pk'),(168,'PW','Palau','Republic of Palau','PLW','585','yes','680','.pw'),(169,'PS','Palestine','State of Palestine (or Occupied Palestinian Territory)','PSE','275','some','970','.ps'),(170,'PA','Panama','Republic of Panama','PAN','591','yes','507','.pa'),(171,'PG','Papua New Guinea','Independent State of Papua New Guinea','PNG','598','yes','675','.pg'),(172,'PY','Paraguay','Republic of Paraguay','PRY','600','yes','595','.py'),(173,'PE','Peru','Republic of Peru','PER','604','yes','51','.pe'),(174,'PH','Philippines','Republic of the Philippines','PHL','608','yes','63','.ph'),(175,'PN','Pitcairn','Pitcairn','PCN','612','no','NONE','.pn'),(176,'PL','Poland','Republic of Poland','POL','616','yes','48','.pl'),(177,'PT','Portugal','Portuguese Republic','PRT','620','yes','351','.pt'),(178,'PR','Puerto Rico','Commonwealth of Puerto Rico','PRI','630','no','1+939','.pr'),(179,'QA','Qatar','State of Qatar','QAT','634','yes','974','.qa'),(180,'RE','Reunion','R&eacute;union','REU','638','no','262','.re'),(181,'RO','Romania','Romania','ROU','642','yes','40','.ro'),(182,'RU','Russia','Russian Federation','RUS','643','yes','7','.ru'),(183,'RW','Rwanda','Republic of Rwanda','RWA','646','yes','250','.rw'),(184,'BL','Saint Barthelemy','Saint Barth&eacute;lemy','BLM','652','no','590','.bl'),(185,'SH','Saint Helena','Saint Helena, Ascension and Tristan da Cunha','SHN','654','no','290','.sh'),(186,'KN','Saint Kitts and Nevis','Federation of Saint Christopher and Nevis','KNA','659','yes','1+869','.kn'),(187,'LC','Saint Lucia','Saint Lucia','LCA','662','yes','1+758','.lc'),(188,'MF','Saint Martin','Saint Martin','MAF','663','no','590','.mf'),(189,'PM','Saint Pierre and Miquelon','Saint Pierre and Miquelon','SPM','666','no','508','.pm'),(190,'VC','Saint Vincent and the Grenadines','Saint Vincent and the Grenadines','VCT','670','yes','1+784','.vc'),(191,'WS','Samoa','Independent State of Samoa','WSM','882','yes','685','.ws'),(192,'SM','San Marino','Republic of San Marino','SMR','674','yes','378','.sm'),(193,'ST','Sao Tome and Principe','Democratic Republic of S&atilde;o Tom&eacute; and Pr&iacute;ncipe','STP','678','yes','239','.st'),(194,'SA','Saudi Arabia','Kingdom of Saudi Arabia','SAU','682','yes','966','.sa'),(195,'SN','Senegal','Republic of Senegal','SEN','686','yes','221','.sn'),(196,'RS','Serbia','Republic of Serbia','SRB','688','yes','381','.rs'),(197,'SC','Seychelles','Republic of Seychelles','SYC','690','yes','248','.sc'),(198,'SL','Sierra Leone','Republic of Sierra Leone','SLE','694','yes','232','.sl'),(199,'SG','Singapore','Republic of Singapore','SGP','702','yes','65','.sg'),(200,'SX','Sint Maarten','Sint Maarten','SXM','534','no','1+721','.sx'),(201,'SK','Slovakia','Slovak Republic','SVK','703','yes','421','.sk'),(202,'SI','Slovenia','Republic of Slovenia','SVN','705','yes','386','.si'),(203,'SB','Solomon Islands','Solomon Islands','SLB','090','yes','677','.sb'),(204,'SO','Somalia','Somali Republic','SOM','706','yes','252','.so'),(205,'ZA','South Africa','Republic of South Africa','ZAF','710','yes','27','.za'),(206,'GS','South Georgia and the South Sandwich Islands','South Georgia and the South Sandwich Islands','SGS','239','no','500','.gs'),(207,'KR','South Korea','Republic of Korea','KOR','410','yes','82','.kr'),(208,'SS','South Sudan','Republic of South Sudan','SSD','728','yes','211','.ss'),(209,'ES','Spain','Kingdom of Spain','ESP','724','yes','34','.es'),(210,'LK','Sri Lanka','Democratic Socialist Republic of Sri Lanka','LKA','144','yes','94','.lk'),(211,'SD','Sudan','Republic of the Sudan','SDN','729','yes','249','.sd'),(212,'SR','Suriname','Republic of Suriname','SUR','740','yes','597','.sr'),(213,'SJ','Svalbard and Jan Mayen','Svalbard and Jan Mayen','SJM','744','no','47','.sj'),(214,'SZ','Swaziland','Kingdom of Swaziland','SWZ','748','yes','268','.sz'),(215,'SE','Sweden','Kingdom of Sweden','SWE','752','yes','46','.se'),(216,'CH','Switzerland','Swiss Confederation','CHE','756','yes','41','.ch'),(217,'SY','Syria','Syrian Arab Republic','SYR','760','yes','963','.sy'),(218,'TW','Taiwan','Republic of China (Taiwan)','TWN','158','former','886','.tw'),(219,'TJ','Tajikistan','Republic of Tajikistan','TJK','762','yes','992','.tj'),(220,'TZ','Tanzania','United Republic of Tanzania','TZA','834','yes','255','.tz'),(221,'TH','Thailand','Kingdom of Thailand','THA','764','yes','66','.th'),(222,'TL','Timor-Leste (East Timor)','Democratic Republic of Timor-Leste','TLS','626','yes','670','.tl'),(223,'TG','Togo','Togolese Republic','TGO','768','yes','228','.tg'),(224,'TK','Tokelau','Tokelau','TKL','772','no','690','.tk'),(225,'TO','Tonga','Kingdom of Tonga','TON','776','yes','676','.to'),(226,'TT','Trinidad and Tobago','Republic of Trinidad and Tobago','TTO','780','yes','1+868','.tt'),(227,'TN','Tunisia','Republic of Tunisia','TUN','788','yes','216','.tn'),(228,'TR','Turkey','Republic of Turkey','TUR','792','yes','90','.tr'),(229,'TM','Turkmenistan','Turkmenistan','TKM','795','yes','993','.tm'),(230,'TC','Turks and Caicos Islands','Turks and Caicos Islands','TCA','796','no','1+649','.tc'),(231,'TV','Tuvalu','Tuvalu','TUV','798','yes','688','.tv'),(232,'UG','Uganda','Republic of Uganda','UGA','800','yes','256','.ug'),(233,'UA','Ukraine','Ukraine','UKR','804','yes','380','.ua'),(234,'AE','United Arab Emirates','United Arab Emirates','ARE','784','yes','971','.ae'),(235,'GB','United Kingdom','United Kingdom of Great Britain and Nothern Ireland','GBR','826','yes','44','.uk'),(236,'US','United States','United States of America','USA','840','yes','1','.us'),(237,'UM','United States Minor Outlying Islands','United States Minor Outlying Islands','UMI','581','no','NONE','NONE'),(238,'UY','Uruguay','Eastern Republic of Uruguay','URY','858','yes','598','.uy'),(239,'UZ','Uzbekistan','Republic of Uzbekistan','UZB','860','yes','998','.uz'),(240,'VU','Vanuatu','Republic of Vanuatu','VUT','548','yes','678','.vu'),(241,'VA','Vatican City','State of the Vatican City','VAT','336','no','39','.va'),(242,'VE','Venezuela','Bolivarian Republic of Venezuela','VEN','862','yes','58','.ve'),(243,'VN','Vietnam','Socialist Republic of Vietnam','VNM','704','yes','84','.vn'),(244,'VG','Virgin Islands, British','British Virgin Islands','VGB','092','no','1+284','.vg'),(245,'VI','Virgin Islands, US','Virgin Islands of the United States','VIR','850','no','1+340','.vi'),(246,'WF','Wallis and Futuna','Wallis and Futuna','WLF','876','no','681','.wf'),(247,'EH','Western Sahara','Western Sahara','ESH','732','no','212','.eh'),(248,'YE','Yemen','Republic of Yemen','YEM','887','yes','967','.ye'),(249,'ZM','Zambia','Republic of Zambia','ZMB','894','yes','260','.zm'),(250,'ZW','Zimbabwe','Republic of Zimbabwe','ZWE','716','yes','263','.zw');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups_product`
--

DROP TABLE IF EXISTS `groups_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_product`
--

LOCK TABLES `groups_product` WRITE;
/*!40000 ALTER TABLE `groups_product` DISABLE KEYS */;
INSERT INTO `groups_product` VALUES (1,'Nenhum',''),(2,'Grupo 1','GP1'),(3,'Grupo 2','GP2');
/*!40000 ALTER TABLE `groups_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_stock`
--

DROP TABLE IF EXISTS `inventory_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `purchase_id` int NOT NULL,
  `qty` int NOT NULL,
  `created_at` varchar(45) DEFAULT NULL,
  `type` enum('in','out') DEFAULT 'in',
  `description` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_stock`
--

LOCK TABLES `inventory_stock` WRITE;
/*!40000 ALTER TABLE `inventory_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `status` enum('0','1') NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
INSERT INTO `options` VALUES (1,'softwareStatus','1','1'),(2,'softwareNumberValidate','txt','1'),(3,'softwareCompanyName','txt','1'),(4,'softwareCompanyNif','hash','1'),(5,'companyKeyLicence','hash','1'),(6,'entity','hash','1'),(7,'companyCity','hash','1'),(8,'companyAddress','Luanda - Luanda','1'),(9,'companyPhone','989898987','1'),(10,'companyName','Company LDA','1'),(11,'companyNif','1212121212','1'),(12,'companyCountry','Angola','1'),(13,'companyEmail','company@company.com','1');
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` int NOT NULL DEFAULT '1',
  `datecreate` varchar(255) NOT NULL,
  `year` int NOT NULL,
  `number` int NOT NULL,
  `prefix` varchar(11) NOT NULL,
  `client_id` int NOT NULL,
  `seller_id` int NOT NULL,
  `total` double DEFAULT NULL,
  `sub_total` double DEFAULT '0',
  `pay_total` double NOT NULL DEFAULT '0',
  `amount_returned` decimal(15,2) NOT NULL DEFAULT '0.00',
  `hash` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `note` text,
  `key` varchar(255) DEFAULT NULL,
  `totalTaxe` double DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` enum('product','service') NOT NULL DEFAULT 'product',
  `code` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `price` decimal(15,2) NOT NULL DEFAULT '0.00',
  `purchase_price` float NOT NULL DEFAULT '0',
  `tax_id` int NOT NULL DEFAULT '0',
  `reason_tax_id` int NOT NULL DEFAULT '0',
  `group_id` int NOT NULL DEFAULT '0',
  `supplier_id` int NOT NULL,
  `barcode` varchar(255) NOT NULL,
  `status` enum('0','1') NOT NULL DEFAULT '0',
  `stock_total` int NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `warehause` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'product','5rerer','Docker min',10000.00,9000,1,41,1,2,'112346456456','1',65,'2024-07-21 13:56:02','2024-07-21 13:56:02','2024-07-21 13:56:02',NULL),(2,'service','444545','teste',0.00,400,1,1,1,2,'123456789','0',18,'2024-07-21 14:08:44','2024-07-21 14:08:44','2024-07-21 14:08:44',NULL),(3,'product','4567','Jogo de copo',5000.00,450,1,1,1,2,'45678678875','0',85,'2024-07-21 14:32:41','2024-07-21 14:32:41','2024-07-21 14:32:41',NULL),(4,'product','yt6776t','teste5',77.00,66,1,1,1,2,'4444447777','1',4990,'2024-07-21 14:39:15','2024-07-21 14:39:15','2024-07-21 14:39:15',NULL),(7,'product','7878','teste4',40.00,30,1,1,1,2,'455445458787','1',789,'2024-07-22 20:19:06','2024-07-22 20:19:06','2024-07-22 20:19:06',NULL),(8,'product','454545434','Embalagem de agua 5/4',55.00,50,1,1,1,2,'656656656','1',40,'2024-07-22 20:19:41','2024-07-22 20:19:41','2024-07-22 20:19:41',NULL),(10,'product','776777876','Descriacao do produto',25000.00,0,1,1,1,2,'9889688787','0',5,'2024-07-30 22:19:38','2024-07-30 22:19:38','2024-07-30 22:19:38',NULL),(12,'product','7868668587','Queijo 500 kg',5500.00,5000,3,2,1,4,'78877777689','0',30,'2024-08-05 20:50:25','2024-08-05 20:50:25','2024-08-05 20:50:25',NULL),(13,'product','43434343','Copos vidros',2000.00,1700,1,1,1,2,'4545454545','1',50,'2024-08-05 20:53:26','2024-08-05 20:53:26','2024-08-05 20:53:26',NULL),(14,'product','54564565','Produto de teste 18',150.00,0,1,1,1,2,'546547657457','0',0,'2024-08-06 21:06:12','2024-08-06 21:06:12','2024-08-06 21:06:12',NULL),(15,'product','435456','Producto teste 18',44444.90,4000,1,41,1,2,'7767766776','1',550,'2024-08-13 18:40:51','2024-08-13 18:40:51','2024-08-13 18:40:51',NULL),(16,'product','667664','Abacate',500.00,300,2,8,1,2,'7777777777','0',0,'2024-08-18 03:35:47','2024-08-18 03:35:47','2024-08-18 03:35:47',NULL),(17,'product','4656664','Saco Platico 50 kilos',10.00,9,3,1,1,2,'655555566','0',4000,'2024-08-18 03:41:02','2024-08-18 03:41:02','2024-08-18 03:41:02',NULL),(18,'product','6554434','Mause HP sem fio',5000.00,4500,1,1,2,1,'999999999','1',50,'2024-08-18 03:44:32','2024-08-18 03:44:32','2024-08-18 03:44:32',NULL),(19,'product','6657657','Copos de vidro',2500.00,2000,3,41,2,1,'456457657','1',50,'2024-08-30 18:01:21','2024-08-30 18:01:21','2024-08-30 18:01:21',NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products_order`
--

DROP TABLE IF EXISTS `products_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` mediumtext NOT NULL,
  `qty` int NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `unit` varchar(40) DEFAULT NULL,
  `prod_code` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `group_code` varchar(50) DEFAULT NULL,
  `taxe_code` varchar(255) DEFAULT NULL,
  `taxe_name` varchar(255) DEFAULT NULL,
  `taxe_percentage` varchar(10) DEFAULT NULL,
  `reason_tax` varchar(255) DEFAULT NULL,
  `reason_code` varchar(255) DEFAULT NULL,
  `reason_standard` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products_order`
--

LOCK TABLES `products_order` WRITE;
/*!40000 ALTER TABLE `products_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `products_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `id_product` varchar(255) NOT NULL,
  `total` decimal(15,2) NOT NULL DEFAULT '0.00',
  `purchase_price` decimal(15,2) NOT NULL DEFAULT '0.00',
  `sale_price` decimal(15,2) NOT NULL DEFAULT '0.00',
  `supplier_id` int NOT NULL,
  `user_id` int NOT NULL,
  `qty` int NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT '0',
  `status_payment` int DEFAULT '0',
  `expiry_date` varchar(45) DEFAULT NULL,
  `date_manufacture` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES (1,'  /  /    ','compra Abacate','16',50000.00,5000.00,6000.00,1,1,10,'2024-09-22 19:34:34','2024-09-22 19:34:34','2024-09-22 19:34:34',1,1,NULL,NULL),(2,'  /  /    ','compra Produto de teste 18','14',1000.00,200.00,220.00,1,1,5,'2024-09-22 19:47:22','2024-09-22 19:47:22','2024-09-22 19:47:22',0,0,NULL,NULL);
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reason_taxes`
--

DROP TABLE IF EXISTS `reason_taxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reason_taxes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `reason` text,
  `standard` varchar(255) DEFAULT NULL,
  `description` text,
  `isdefault` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reason_taxes`
--

LOCK TABLES `reason_taxes` WRITE;
/*!40000 ALTER TABLE `reason_taxes` DISABLE KEYS */;
INSERT INTO `reason_taxes` VALUES (1,'M00','Regime transitÃƒÂ³rio','','',0),(2,'M02','TransmissÃƒÂ£o de bens e serviÃƒÂ§o nÃƒÂ£o sujeita','','',0),(3,'M04','IVA Ã¢â‚¬â€œ Regime de nÃƒÂ£o sujeiÃƒÂ§ÃƒÂ£o','','',0),(4,'M10','Isento nos termos da alÃƒÂ­nea b) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','A transmissÃƒÂ£o dos bens alimentares, conforme anexo I do presente cÃƒÂ³digo',0),(5,'M11','Isento nos termos da alÃƒÂ­nea b) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As transmissÃƒÂµes de medicamentos destinados exclusivamente a fins terapÃƒÂªuticos e profilÃƒÂ¡cticos',0),(6,'M12','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As transmissÃƒÂµes de cadeiras de rodas e veÃƒÂ­culos semelhantes, accionados manualmente ou por motor, para portadores de deficiÃƒÂªncia, aparelhos, mÃƒÂ¡quinas de escrever com caracteres braille, impressoras para caracteres braille e os artefactos que se destinam a ser utilizados por invisuais ou a corrigir a audiÃƒÂ§ÃƒÂ£o',0),(7,'M13','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','A transmissÃƒÂ£o de livros, incluindo em formato digital',0),(8,'M14','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','A locaÃƒÂ§ÃƒÂ£o de bens imÃƒÂ³veis destinados a fins habitacionais, designadamente prÃƒÂ©dios urbanos, fracÃƒÂ§ÃƒÂµes autÃƒÂ³nomas destes ou terrenos para construÃƒÂ§ÃƒÂ£o, com excepÃƒÂ§ÃƒÂ£o das prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§os de alojamento efectuadas no ÃƒÂ¢mbito da actividade hoteleira ou de outras com funÃƒÂ§ÃƒÂµes anÃƒÂ¡logas',0),(9,'M15','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As operaÃƒÂ§ÃƒÂµes sujeitas ao imposto de SISA, ainda que dele isentas',0),(10,'M16','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','\"A exploraÃƒÂ§ÃƒÂ£o e a prÃƒÂ¡tica de jogos de fortuna ou azar e de diversÃƒÂ£o social, bem como as respectivas comissÃƒÂµes e todas as operaÃƒÂ§ÃƒÂµes relacionadas, quando as mesmas estejam sujeitas a Imposto Especial sobre o Jogos, nos',0),(11,'M11','Isento nos termos da alÃƒÂ­nea b) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As transmissÃƒÂµes de medicamentos destinados exclusivamente a fins terapÃƒÂªuticos e profilÃƒÂ¡cticos',0),(12,'M17','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','O transporte colectivo de passageiros',0),(13,'M18','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As operaÃƒÂ§ÃƒÂµes de intermediaÃƒÂ§ÃƒÂ£o financeira, incluindo a locaÃƒÂ§ÃƒÂ£o financeira, exceptuando-se aquelas em que uma taxa, ou contraprestaÃƒÂ§ÃƒÂ£o, especifica e predeterminada ÃƒÂ© cobrada pelo serviÃƒÂ§o.',0),(14,'M19','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','O seguro de saÃƒÂºde, bem como a prestaÃƒÂ§ÃƒÂ£o de seguros e resseguros do ramo vida;',0),(15,'M20','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As transmissÃƒÂµes de produtos petrolÃƒÂ­feros conforme anexo II do presente cÃƒÂ³digo.',0),(16,'M21','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§o que tenham por objecto o ensino, efectuadas por estabelecimentos integrados conforme definidos na Lei de Bases do Sistema de EducaÃƒÂ§ÃƒÂ£o e Ensino, bem como por estabelecimentos de Ensino Superior devidamente reconhecidos pelo MinistÃƒÂ©rio de Tutela.',0),(17,'M22','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','As prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§o mÃƒÂ©dico sanitÃƒÂ¡rio, efectuadas por estabelecimentos hospitalares, clinicas, dispensÃƒÂ¡rios e similares',0),(18,'M23','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','O transporte de doentes ou feridos em ambulÃƒÂ¢ncias ou outros veÃƒÂ­culos apropriados efectuados por organismos devidamente autorizados',0),(19,'M24','Isento nos termos da alÃƒÂ­nea c) do nÃ‚Âº1 do artigo 12.Ã‚Âº do CIVA','Artigo 12.Ã‚Âº do CIVA','Os equipamentos mÃƒÂ©dicos para o exercÃƒÂ­cio da actividade dos estabelecimentos de saÃƒÂºde.',0),(20,'M30','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens expedidos ou transportados com destino ao estrangeiro pelo vendedor ou por um terceiro por conta deste',0),(21,'M31','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens de abastecimento postos a bordo das embarcaÃƒÂ§ÃƒÂµes que efectuem navegaÃƒÂ§ÃƒÂ£o marÃƒÂ­tima em alto mar e que assegurem o transporte remunerado de passageiros ou o exercÃƒÂ­cio de uma actividade comercial, industrial ou de pesca;',0),(22,'M32','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens de abastecimento postos a bordo das aeronaves utilizadas por companhias de navegaÃƒÂ§ÃƒÂ£o aÃƒÂ©rea que se dediquem principalmente ao trÃƒÂ¡fego internacional e que assegurem o transporte remunerado de passageiros, ou o exercÃƒÂ­cio de uma actividade comercial ou industria',0),(23,'M33','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens de abastecimento postos a bordo das embarcaÃƒÂ§ÃƒÂµes de salvamento, assistÃƒÂªncia marÃƒÂ­tima, pesca costeira e embarcaÃƒÂ§ÃƒÂµes de guerra, quando deixem o paÃƒÂ­s com destino a um porto ou ancoradouro situado no estrangeiro',0),(24,'M34','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes, transformaÃƒÂ§ÃƒÂµes, reparaÃƒÂ§ÃƒÂµes, manutenÃƒÂ§ÃƒÂ£o, frete e aluguer, incluindo a locaÃƒÂ§ÃƒÂ£o financeira, de embarcaÃƒÂ§ÃƒÂµes e aeronaves afectas ÃƒÂ s companhias de navegaÃƒÂ§ÃƒÂ£o aÃƒÂ©rea e marÃƒÂ­tima que se dediquem principalmente ao trÃƒÂ¡fego internacional, assim como as transmissÃƒÂµes de bens de abastecimento postos a bordo das mesmas e as prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§os efectuadas com vista ÃƒÂ  satisfaÃƒÂ§ÃƒÂ£o das suas necessidades directas e da respectiva carga;',0),(25,'M35','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens efectuadas no ÃƒÂ¢mbito de relaÃƒÂ§ÃƒÂµes diplomÃƒÂ¡ticas e consulares cuja isenÃƒÂ§ÃƒÂ£o resulte de acordos e convÃƒÂ©nios internacionais celebrados por Angola;',0),(26,'M36','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens destinados a organismos internacionais reconhecidos por Angola ou a membros dos mesmos organismos, nos limites e com as condiÃƒÂ§ÃƒÂµes fixadas em acordos e convÃƒÂ©nios internacionais celebrados por Angola',0),(27,'M37','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','As transmissÃƒÂµes de bens efectuadas no ÃƒÂ¢mbito de tratados e acordos internacionais de que a RepÃƒÂºblica de Angola seja parte, quando a isenÃƒÂ§ÃƒÂ£o resulte desses mesmos tratados e acordos',0),(28,'M38','Isento nos termos da alÃƒÂ­nea h) do artigo 15.Ã‚Âº do CIVA','Artigo 15.Ã‚Âº do CIVA','O transporte de pessoas provenientes ou com destino ao estrangeiro.',0),(29,'M80','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','As importaÃƒÂ§ÃƒÂµes definitivas de bens cuja transmissÃƒÂ£o no territÃƒÂ³rio nacional seja isenta de imposto;',0),(30,'M81','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','As importaÃƒÂ§ÃƒÂµes de ouro, moedas ou notas de banco, efectuadas pelo Banco Nacional de Angola',0),(31,'M82','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','A importaÃƒÂ§ÃƒÂ£o de bens destinados a ofertas para atenuar os efeitos das calamidades naturais, tais como cheias, tempestades, secas, ciclones, sismos, terramotos e outros de idÃƒÂªntica natureza, desde que devidamente autorizado pelo Titular do Poder Executivo',0),(32,'M83','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','A importaÃƒÂ§ÃƒÂ£o de mercadorias ou equipamentos destinadas exclusiva e directamente ÃƒÂ  execuÃƒÂ§ÃƒÂ£o das operaÃƒÂ§ÃƒÂµes petrolÃƒÂ­feras e mineiras nos termos da Lei que estabelece o Regime Aduaneiro do Sector PetrolÃƒÂ­fero e do CÃƒÂ³digo Mineiro, respectivamente.',0),(33,'M84','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','ImportaÃƒÂ§ÃƒÂ£o de moeda estrangeira efectuada pelas instituiÃƒÂ§ÃƒÂµes financeiras bancÃƒÂ¡rias, nos termos definidos pelo Banco Nacional de Angola.',0),(34,'M85','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','No ÃƒÂ¢mbito de tratados e acordos internacionais de que a RepÃƒÂºblica de Angola seja parte, nos termos previstos nesses tratados e acordos;',0),(35,'M86','Isento nos termos da alinea b) do nÃ‚Âº2 do artigo 14.Ã‚Âº','Artigo 14.Ã‚Âº do CIVA ','No ÃƒÂ¢mbito de relaÃƒÂ§ÃƒÂµes diplomÃƒÂ¡ticas e consulares, quando a isenÃƒÂ§ÃƒÂ£o resulte de tratados e acordos internacionais celebrados pela RepÃƒÂºblica de Angola',0),(36,'M90','Isento nos termos da alinea a) do nÃ‚Âº1 do artigo 16.Ã‚Âº','Artigo 16Ã‚Âº do CIVA','As importaÃƒÂ§ÃƒÂµes de bens que, sob controlo aduaneiro e de acordo com as disposiÃƒÂ§ÃƒÂµes aduaneiras especificamente aplicÃƒÂ¡veis, sejam postas nos regimes de zona franca, que sejam introduzidos em armazÃƒÂ©ns de regimes aduaneiros ou lojas francas, enquanto permanecerem sob tais regimes;',0),(37,'M91','Isento nos termos da alinea a) do nÃ‚Âº1 do artigo 16.Ã‚Âº','Artigo 16Ã‚Âº do CIVA','As transmissÃƒÂµes de bens que sejam expedidos ou transportados para as zonas ou depÃƒÂ³sitos mencionados na alÃƒÂ­nea anterior, bem como as prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§os directamente conexas com tais transmissÃƒÂµes;',0),(38,'M92','Isento nos termos da alinea a) do nÃ‚Âº1 do artigo 16.Ã‚Âº','Artigo 16Ã‚Âº do CIVA','As transmissÃƒÂµes de bens que se efectuem nos regimes a que se refere a alÃƒÂ­nea a), assim como as prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§os directamente conexas com tais transmissÃƒÂµes, enquanto os bens permanecerem naquelas situaÃƒÂ§ÃƒÂµes',0),(39,'M93','Isento nos termos da alinea a) do nÃ‚Âº1 do artigo 16.Ã‚Âº','Artigo 16Ã‚Âº do CIVA','As transmissÃƒÂµes de bens que se encontrem nos regimes de trÃƒÂ¢nsito, draubaque ou importaÃƒÂ§ÃƒÂ£o temporÃƒÂ¡ria e as prestaÃƒÂ§ÃƒÂµes de serviÃƒÂ§os directamente conexas com tais operaÃƒÂ§ÃƒÂµes, enquanto os mesmos forem considerados abrangidos por aqueles regimes',0),(40,'M94','Isento nos termos da alinea a) do nÃ‚Âº1 do artigo 16.Ã‚Âº','Artigo 16Ã‚Âº do CIVA','A reimportaÃƒÂ§ÃƒÂ£o de bens por quem os exportou, no mesmo estado em que foram exportados, quando beneficiem de isenÃƒÂ§ÃƒÂ£o de direitos aduaneiros',0),(41,'Nenhum','Nenhum','Nenhum','Nenhum',0);
/*!40000 ALTER TABLE `reason_taxes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `hash` varchar(255) NOT NULL DEFAULT '0',
  `code` varchar(255) NOT NULL DEFAULT '0',
  `granted_amount` double NOT NULL,
  `incurred_amount` double NOT NULL DEFAULT '0',
  `closing_amount` double NOT NULL DEFAULT '0',
  `status` enum('open','close') NOT NULL DEFAULT 'open',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `dateClose` varchar(45) DEFAULT NULL,
  `dateOpen` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
INSERT INTO `shift` VALUES (7,1,'aH/DCZou07KEP+QHtD2gZLPcwXUJYnFt1Y8DNYvfd1Q=','aTtn/mFrBp+zGlxCueSECHW5CIAh222Qx6uvsj344KA=',400,0,0,'open','2024-09-22 20:58:24','2024-09-22 20:58:24',NULL,'2024-09-22 20:58:24');
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` varchar(191) NOT NULL,
  `nif` varchar(15) NOT NULL DEFAULT 'XXXXXXXXXX',
  `phone` varchar(30) NOT NULL,
  `email` varchar(255) NOT NULL,
  `country` int NOT NULL DEFAULT '0',
  `city` varchar(100) NOT NULL,
  `zip_code` varchar(15) NOT NULL,
  `state` varchar(50) NOT NULL,
  `address` varchar(191) NOT NULL,
  `status` int NOT NULL DEFAULT '1',
  `group_id` int NOT NULL DEFAULT '0',
  `isdefault` int NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'Fornecedor LDA','XXXXXXXXXXX','XXXXXXXXXXX','XXXXXXXXXXX',9,'desconhecido','ao1','desconhecido','desconhecido',1,0,0,'2024-07-20 20:31:35',NULL,NULL);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxes`
--

DROP TABLE IF EXISTS `taxes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taxes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `percentage` decimal(15,2) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `isdefault` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxes`
--

LOCK TABLES `taxes` WRITE;
/*!40000 ALTER TABLE `taxes` DISABLE KEYS */;
INSERT INTO `taxes` VALUES (1,'IVA',7.00,'IVA',0,0),(2,'IVA 14',14.00,'IVA',0,0),(3,'ISENTO',0.00,'ISE',0,0);
/*!40000 ALTER TABLE `taxes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `profile` enum('user','admin','seller','manager') NOT NULL DEFAULT 'user',
  `code` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `nif` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `birthdate` varchar(100) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `password` varchar(250) NOT NULL DEFAULT '1234',
  `status` int DEFAULT '1',
  `country` int NOT NULL,
  `city` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','123456789','admin@admin.com','','Administrador','','','12345678',1,1,'','0','2024-07-20 19:33:47','2024-07-20 19:33:47','2024-07-20 19:33:47'),(11,'admin',NULL,'admin@company.com','1212121212','Company LDA','','989898987','123456',1,7,'','Luanda - Luanda','2024-09-21 11:16:51','2024-09-21 11:16:51','2024-09-21 11:16:51');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1,'Armazem 1','Desconhecido');
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-23  6:31:55
