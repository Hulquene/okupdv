-- ============================================================================
-- install.sql - Schema e dados iniciais (MariaDB / MySQL compatível)
-- Charset: utf8mb4
-- Comentários: linhas iniciando com "--"
-- ============================================================================

-- Recomendação de sessão
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- 1) COUNTRIES
-- ============================================================================
DROP TABLE IF EXISTS `countries`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `countries` VALUES
(1,'AF','Afghanistan','Islamic Republic of Afghanistan','AFG','004','yes','93','.af'),
(2,'AX','Aland Islands','Åland Islands','ALA','248','no','358','.ax'),
(3,'AL','Albania','Republic of Albania','ALB','008','yes','355','.al'),
(4,'DZ','Algeria','People s Democratic Republic of Algeria','DZA','012','yes','213','.dz'),
(5,'AS','American Samoa','American Samoa','ASM','016','no','1+684','.as'),
(6,'AD','Andorra','Principality of Andorra','AND','020','yes','376','.ad'),
(7,'AO','Angola','Republic of Angola','AGO','024','yes','244','.ao'),
(8,'AI','Anguilla','Anguilla','AIA','660','no','1+264','.ai'),
(9,'AQ','Antarctica','Antarctica','ATA','010','no','672','.aq'),
(10,'AG','Antigua and Barbuda','Antigua and Barbuda','ATG','028','yes','1+268','.ag'),
(11,'AR','Argentina','Argentine Republic','ARG','032','yes','54','.ar'),
(12,'AM','Armenia','Republic of Armenia','ARM','051','yes','374','.am'),
(13,'AW','Aruba','Aruba','ABW','533','no','297','.aw'),
(14,'AU','Australia','Commonwealth of Australia','AUS','036','yes','61','.au'),
(15,'AT','Austria','Republic of Austria','AUT','040','yes','43','.at'),
(16,'AZ','Azerbaijan','Republic of Azerbaijan','AZE','031','yes','994','.az'),
(17,'BS','Bahamas','Commonwealth of The Bahamas','BHS','044','yes','1+242','.bs'),
(18,'BH','Bahrain','Kingdom of Bahrain','BHR','048','yes','973','.bh'),
(19,'BD','Bangladesh','Peoples Republic of Bangladesh','BGD','050','yes','880','.bd'),
(20,'BB','Barbados','Barbados','BRB','052','yes','1+246','.bb'),
(21,'BY','Belarus','Republic of Belarus','BLR','112','yes','375','.by'),
(22,'BE','Belgium','Kingdom of Belgium','BEL','056','yes','32','.be'),
(23,'BZ','Belize','Belize','BLZ','084','yes','501','.bz'),
(24,'BJ','Benin','Republic of Benin','BEN','204','yes','229','.bj'),
(25,'BM','Bermuda','Bermuda Islands','BMU','060','no','1+441','.bm'),
(26,'BT','Bhutan','Kingdom of Bhutan','BTN','064','yes','975','.bt'),
(27,'BO','Bolivia','Plurinational State of Bolivia','BOL','068','yes','591','.bo'),
(28,'BQ','Bonaire, Sint Eustatius and Saba','Bonaire, Sint Eustatius and Saba','BES','535','no','599','.bq'),
(29,'BA','Bosnia and Herzegovina','Bosnia and Herzegovina','BIH','070','yes','387','.ba'),
(30,'BW','Botswana','Republic of Botswana','BWA','072','yes','267','.bw'),
(31,'BV','Bouvet Island','Bouvet Island','BVT','074','no','NONE','.bv'),
(32,'BR','Brazil','Federative Republic of Brazil','BRA','076','yes','55','.br'),
(33,'IO','British Indian Ocean Territory','British Indian Ocean Territory','IOT','086','no','246','.io'),
(34,'BN','Brunei','Brunei Darussalam','BRN','096','yes','673','.bn'),
(35,'BG','Bulgaria','Republic of Bulgaria','BGR','100','yes','359','.bg'),
(36,'BF','Burkina Faso','Burkina Faso','BFA','854','yes','226','.bf'),
(37,'BI','Burundi','Republic of Burundi','BDI','108','yes','257','.bi'),
(38,'KH','Cambodia','Kingdom of Cambodia','KHM','116','yes','855','.kh'),
(39,'CM','Cameroon','Republic of Cameroon','CMR','120','yes','237','.cm'),
(40,'CA','Canada','Canada','CAN','124','yes','1','.ca'),
(41,'CV','Cape Verde','Republic of Cape Verde','CPV','132','yes','238','.cv'),
(42,'KY','Cayman Islands','The Cayman Islands','CYM','136','no','1+345','.ky'),
(43,'CF','Central African Republic','Central African Republic','CAF','140','yes','236','.cf'),
(44,'TD','Chad','Republic of Chad','TCD','148','yes','235','.td'),
(45,'CL','Chile','Republic of Chile','CHL','152','yes','56','.cl'),
(46,'CN','China','Peoples Republic of China','CHN','156','yes','86','.cn'),
(47,'CX','Christmas Island','Christmas Island','CXR','162','no','61','.cx'),
(48,'CC','Cocos (Keeling) Islands','Cocos (Keeling) Islands','CCK','166','no','61','.cc'),
(49,'CO','Colombia','Republic of Colombia','COL','170','yes','57','.co'),
(50,'KM','Comoros','Union of the Comoros','COM','174','yes','269','.km'),
(51,'CG','Congo','Republic of the Congo','COG','178','yes','242','.cg'),
(52,'CK','Cook Islands','Cook Islands','COK','184','some','682','.ck'),
(53,'CR','Costa Rica','Republic of Costa Rica','CRI','188','yes','506','.cr'),
(54,'CI','Cote d\'ivoire (Ivory Coast)','Republic of Côte D\'Ivoire (Ivory Coast)','CIV','384','yes','225','.ci'),
(55,'HR','Croatia','Republic of Croatia','HRV','191','yes','385','.hr'),
(56,'CU','Cuba','Republic of Cuba','CUB','192','yes','53','.cu'),
(57,'CW','Curacao','Curaçao','CUW','531','no','599','.cw'),
(58,'CY','Cyprus','Republic of Cyprus','CYP','196','yes','357','.cy'),
(59,'CZ','Czech Republic','Czech Republic','CZE','203','yes','420','.cz'),
(60,'CD','Democratic Republic of the Congo','Democratic Republic of the Congo','COD','180','yes','243','.cd'),
(61,'DK','Denmark','Kingdom of Denmark','DNK','208','yes','45','.dk'),
(62,'DJ','Djibouti','Republic of Djibouti','DJI','262','yes','253','.dj'),
(63,'DM','Dominica','Commonwealth of Dominica','DMA','212','yes','1+767','.dm'),
(64,'DO','Dominican Republic','Dominican Republic','DOM','214','yes','1+809, 8','.do'),
(65,'EC','Ecuador','Republic of Ecuador','ECU','218','yes','593','.ec'),
(66,'EG','Egypt','Arab Republic of Egypt','EGY','818','yes','20','.eg'),
(67,'SV','El Salvador','Republic of El Salvador','SLV','222','yes','503','.sv'),
(68,'GQ','Equatorial Guinea','Republic of Equatorial Guinea','GNQ','226','yes','240','.gq'),
(69,'ER','Eritrea','State of Eritrea','ERI','232','yes','291','.er'),
(70,'EE','Estonia','Republic of Estonia','EST','233','yes','372','.ee'),
(71,'ET','Ethiopia','Federal Democratic Republic of Ethiopia','ETH','231','yes','251','.et'),
(72,'FK','Falkland Islands (Malvinas)','The Falkland Islands (Malvinas)','FLK','238','no','500','.fk'),
(73,'FO','Faroe Islands','The Faroe Islands','FRO','234','no','298','.fo'),
(74,'FJ','Fiji','Republic of Fiji','FJI','242','yes','679','.fj'),
(75,'FI','Finland','Republic of Finland','FIN','246','yes','358','.fi'),
(76,'FR','France','French Republic','FRA','250','yes','33','.fr'),
(77,'GF','French Guiana','French Guiana','GUF','254','no','594','.gf'),
(78,'PF','French Polynesia','French Polynesia','PYF','258','no','689','.pf'),
(79,'TF','French Southern Territories','French Southern Territories','ATF','260','no',NULL,'.tf'),
(80,'GA','Gabon','Gabonese Republic','GAB','266','yes','241','.ga'),
(81,'GM','Gambia','Republic of The Gambia','GMB','270','yes','220','.gm'),
(82,'GE','Georgia','Georgia','GEO','268','yes','995','.ge'),
(83,'DE','Germany','Federal Republic of Germany','DEU','276','yes','49','.de'),
(84,'GH','Ghana','Republic of Ghana','GHA','288','yes','233','.gh'),
(85,'GI','Gibraltar','Gibraltar','GIB','292','no','350','.gi'),
(86,'GR','Greece','Hellenic Republic','GRC','300','yes','30','.gr'),
(87,'GL','Greenland','Greenland','GRL','304','no','299','.gl'),
(88,'GD','Grenada','Grenada','GRD','308','yes','1+473','.gd'),
(89,'GP','Guadaloupe','Guadeloupe','GLP','312','no','590','.gp'),
(90,'GU','Guam','Guam','GUM','316','no','1+671','.gu'),
(91,'GT','Guatemala','Republic of Guatemala','GTM','320','yes','502','.gt'),
(92,'GG','Guernsey','Guernsey','GGY','831','no','44','.gg'),
(93,'GN','Guinea','Republic of Guinea','GIN','324','yes','224','.gn'),
(94,'GW','Guinea-Bissau','Republic of Guinea-Bissau','GNB','624','yes','245','.gw'),
(95,'GY','Guyana','Co-operative Republic of Guyana','GUY','328','yes','592','.gy'),
(96,'HT','Haiti','Republic of Haiti','HTI','332','yes','509','.ht'),
(97,'HM','Heard Island and McDonald Islands','Heard Island and McDonald Islands','HMD','334','no','NONE','.hm'),
(98,'HN','Honduras','Republic of Honduras','HND','340','yes','504','.hn'),
(99,'HK','Hong Kong','Hong Kong','HKG','344','no','852','.hk'),
(100,'HU','Hungary','Hungary','HUN','348','yes','36','.hu'),
(101,'IS','Iceland','Republic of Iceland','ISL','352','yes','354','.is'),
(102,'IN','India','Republic of India','IND','356','yes','91','.in'),
(103,'ID','Indonesia','Republic of Indonesia','IDN','360','yes','62','.id'),
(104,'IR','Iran','Islamic Republic of Iran','IRN','364','yes','98','.ir'),
(105,'IQ','Iraq','Republic of Iraq','IRQ','368','yes','964','.iq'),
(106,'IE','Ireland','Ireland','IRL','372','yes','353','.ie'),
(107,'IM','Isle of Man','Isle of Man','IMN','833','no','44','.im'),
(108,'IL','Israel','State of Israel','ISR','376','yes','972','.il'),
(109,'IT','Italy','Italian Republic','ITA','380','yes','39','.jm'),
(110,'JM','Jamaica','Jamaica','JAM','388','yes','1+876','.jm'),
(111,'JP','Japan','Japan','JPN','392','yes','81','.jp'),
(112,'JE','Jersey','The Bailiwick of Jersey','JEY','832','no','44','.je'),
(113,'JO','Jordan','Hashemite Kingdom of Jordan','JOR','400','yes','962','.jo'),
(114,'KZ','Kazakhstan','Republic of Kazakhstan','KAZ','398','yes','7','.kz'),
(115,'KE','Kenya','Republic of Kenya','KEN','404','yes','254','.ke'),
(116,'KI','Kiribati','Republic of Kiribati','KIR','296','yes','686','.ki'),
(117,'XK','Kosovo','Republic of Kosovo','---','---','some','381',''),
(118,'KW','Kuwait','State of Kuwait','KWT','414','yes','965','.kw'),
(119,'KG','Kyrgyzstan','Kyrgyz Republic','KGZ','417','yes','996','.kg'),
(120,'LA','Laos','Lao Peoples Democratic Republic','LAO','418','yes','856','.la'),
(121,'LV','Latvia','Republic of Latvia','LVA','428','yes','371','.lv'),
(122,'LB','Lebanon','Republic of Lebanon','LBN','422','yes','961','.lb'),
(123,'LS','Lesotho','Kingdom of Lesotho','LSO','426','yes','266','.ls'),
(124,'LR','Liberia','Republic of Liberia','LBR','430','yes','231','.lr'),
(125,'LY','Libya','Libya','LBY','434','yes','218','.ly'),
(126,'LI','Liechtenstein','Principality of Liechtenstein','LIE','438','yes','423','.li'),
(127,'LT','Lithuania','Republic of Lithuania','LTU','440','yes','370','.lt'),
(128,'LU','Luxembourg','Grand Duchy of Luxembourg','LUX','442','yes','352','.lu'),
(129,'MO','Macao','The Macao Special Administrative Region','MAC','446','no','853','.mo'),
(130,'MK','North Macedonia','Republic of North Macedonia','MKD','807','yes','389','.mk'),
(131,'MG','Madagascar','Republic of Madagascar','MDG','450','yes','261','.mg'),
(132,'MW','Malawi','Republic of Malawi','MWI','454','yes','265','.mw'),
(133,'MY','Malaysia','Malaysia','MYS','458','yes','60','.my'),
(134,'MV','Maldives','Republic of Maldives','MDV','462','yes','960','.mv'),
(135,'ML','Mali','Republic of Mali','MLI','466','yes','223','.ml'),
(136,'MT','Malta','Republic of Malta','MLT','470','yes','356','.mt'),
(137,'MH','Marshall Islands','Republic of the Marshall Islands','MHL','584','yes','692','.mh'),
(138,'MQ','Martinique','Martinique','MTQ','474','no','596','.mq'),
(139,'MR','Mauritania','Islamic Republic of Mauritania','MRT','478','yes','222','.mr'),
(140,'MU','Mauritius','Republic of Mauritius','MUS','480','yes','230','.mu'),
(141,'YT','Mayotte','Mayotte','MYT','175','no','262','.yt'),
(142,'MX','Mexico','United Mexican States','MEX','484','yes','52','.mx'),
(143,'FM','Micronesia','Federated States of Micronesia','FSM','583','yes','691','.fm'),
(144,'MD','Moldava','Republic of Moldova','MDA','498','yes','373','.md'),
(145,'MC','Monaco','Principality of Monaco','MCO','492','yes','377','.mc'),
(146,'MN','Mongolia','Mongolia','MNG','496','yes','976','.mn'),
(147,'ME','Montenegro','Montenegro','MNE','499','yes','382','.me'),
(148,'MS','Montserrat','Montserrat','MSR','500','no','1+664','.ms'),
(149,'MA','Morocco','Kingdom of Morocco','MAR','504','yes','212','.ma'),
(150,'MZ','Mozambique','Republic of Mozambique','MOZ','508','yes','258','.mz'),
(151,'MM','Myanmar (Burma)','Republic of the Union of Myanmar','MMR','104','yes','95','.mm'),
(152,'NA','Namibia','Republic of Namibia','NAM','516','yes','264','.na'),
(153,'NR','Nauru','Republic of Nauru','NRU','520','yes','674','.nr'),
(154,'NP','Nepal','Federal Democratic Republic of Nepal','NPL','524','yes','977','.np'),
(155,'NL','Netherlands','Kingdom of the Netherlands','NLD','528','yes','31','.nl'),
(156,'NC','New Caledonia','New Caledonia','NCL','540','no','687','.nc'),
(157,'NZ','New Zealand','New Zealand','NZL','554','yes','64','.nz'),
(158,'NI','Nicaragua','Republic of Nicaragua','NIC','558','yes','505','.ni'),
(159,'NE','Niger','Republic of Niger','NER','562','yes','227','.ne'),
(160,'NG','Nigeria','Federal Republic of Nigeria','NGA','566','yes','234','.ng'),
(161,'NU','Niue','Niue','NIU','570','some','683','.nu'),
(162,'NF','Norfolk Island','Norfolk Island','NFK','574','no','672','.nf'),
(163,'KP','North Korea','Democratic Peoples Republic of Korea','PRK','408','yes','850','.kp'),
(164,'MP','Northern Mariana Islands','Northern Mariana Islands','MNP','580','no','1+670','.mp'),
(165,'NO','Norway','Kingdom of Norway','NOR','578','yes','47','.no'),
(166,'OM','Oman','Sultanate of Oman','OMN','512','yes','968','.om'),
(167,'PK','Pakistan','Islamic Republic of Pakistan','PAK','586','yes','92','.pk'),
(168,'PW','Palau','Republic of Palau','PLW','585','yes','680','.pw'),
(169,'PS','Palestine','State of Palestine (or Occupied Palestinian Territory)','PSE','275','some','970','.ps'),
(170,'PA','Panama','Republic of Panama','PAN','591','yes','507','.pa'),
(171,'PG','Papua New Guinea','Independent State of Papua New Guinea','PNG','598','yes','675','.pg'),
(172,'PY','Paraguay','Republic of Paraguay','PRY','600','yes','595','.py'),
(173,'PE','Peru','Republic of Peru','PER','604','yes','51','.pe'),
(174,'PH','Philippines','Republic of the Philippines','PHL','608','yes','63','.ph'),
(175,'PN','Pitcairn','Pitcairn','PCN','612','no','NONE','.pn'),
(176,'PL','Poland','Republic of Poland','POL','616','yes','48','.pl'),
(177,'PT','Portugal','Portuguese Republic','PRT','620','yes','351','.pt'),
(178,'PR','Puerto Rico','Commonwealth of Puerto Rico','PRI','630','no','1+939','.pr'),
(179,'QA','Qatar','State of Qatar','QAT','634','yes','974','.qa'),
(180,'RE','Reunion','Réunion','REU','638','no','262','.re'),
(181,'RO','Romania','Romania','ROU','642','yes','40','.ro'),
(182,'RU','Russia','Russian Federation','RUS','643','yes','7','.ru'),
(183,'RW','Rwanda','Republic of Rwanda','RWA','646','yes','250','.rw'),
(184,'BL','Saint Barthelemy','Saint Barthélemy','BLM','652','no','590','.bl'),
(185,'SH','Saint Helena','Saint Helena, Ascension and Tristan da Cunha','SHN','654','no','290','.sh'),
(186,'KN','Saint Kitts and Nevis','Federation of Saint Christopher and Nevis','KNA','659','yes','1+869','.kn'),
(187,'LC','Saint Lucia','Saint Lucia','LCA','662','yes','1+758','.lc'),
(188,'MF','Saint Martin','Saint Martin','MAF','663','no','590','.mf'),
(189,'PM','Saint Pierre and Miquelon','Saint Pierre and Miquelon','SPM','666','no','508','.pm'),
(190,'VC','Saint Vincent and the Grenadines','Saint Vincent and the Grenadines','VCT','670','yes','1+784','.vc'),
(191,'WS','Samoa','Independent State of Samoa','WSM','882','yes','685','.ws'),
(192,'SM','San Marino','Republic of San Marino','SMR','674','yes','378','.sm'),
(193,'ST','Sao Tome and Principe','Democratic Republic of São Tomé and Príncipe','STP','678','yes','239','.st'),
(194,'SA','Saudi Arabia','Kingdom of Saudi Arabia','SAU','682','yes','966','.sa'),
(195,'SN','Senegal','Republic of Senegal','SEN','686','yes','221','.sn'),
(196,'RS','Serbia','Republic of Serbia','SRB','688','yes','381','.rs'),
(197,'SC','Seychelles','Republic of Seychelles','SYC','690','yes','248','.sc'),
(198,'SL','Sierra Leone','Republic of Sierra Leone','SLE','694','yes','232','.sl'),
(199,'SG','Singapore','Republic of Singapore','SGP','702','yes','65','.sg'),
(200,'SX','Sint Maarten','Sint Maarten','SXM','534','no','1+721','.sx'),
(201,'SK','Slovakia','Slovak Republic','SVK','703','yes','421','.sk'),
(202,'SI','Slovenia','Republic of Slovenia','SVN','705','yes','386','.si'),
(203,'SB','Solomon Islands','Solomon Islands','SLB','090','yes','677','.sb'),
(204,'SO','Somalia','Somali Republic','SOM','706','yes','252','.so'),
(205,'ZA','South Africa','Republic of South Africa','ZAF','710','yes','27','.za'),
(206,'GS','South Georgia and the South Sandwich Islands','South Georgia and the South Sandwich Islands','SGS','239','no','500','.gs'),
(207,'KR','South Korea','Republic of Korea','KOR','410','yes','82','.kr'),
(208,'SS','South Sudan','Republic of South Sudan','SSD','728','yes','211','.ss'),
(209,'ES','Spain','Kingdom of Spain','ESP','724','yes','34','.es'),
(210,'LK','Sri Lanka','Democratic Socialist Republic of Sri Lanka','LKA','144','yes','94','.lk'),
(211,'SD','Sudan','Republic of the Sudan','SDN','729','yes','249','.sd'),
(212,'SR','Suriname','Republic of Suriname','SUR','740','yes','597','.sr'),
(213,'SJ','Svalbard and Jan Mayen','Svalbard and Jan Mayen','SJM','744','no','47','.sj'),
(214,'SZ','Swaziland','Kingdom of Swaziland','SWZ','748','yes','268','.sz'),
(215,'SE','Sweden','Kingdom of Sweden','SWE','752','yes','46','.se'),
(216,'CH','Switzerland','Swiss Confederation','CHE','756','yes','41','.ch'),
(217,'SY','Syria','Syrian Arab Republic','SYR','760','yes','963','.sy'),
(218,'TW','Taiwan','Republic of China (Taiwan)','TWN','158','former','886','.tw'),
(219,'TJ','Tajikistan','Republic of Tajikistan','TJK','762','yes','992','.tj'),
(220,'TZ','Tanzania','United Republic of Tanzania','TZA','834','yes','255','.tz'),
(221,'TH','Thailand','Kingdom of Thailand','THA','764','yes','66','.th'),
(222,'TL','Timor-Leste (East Timor)','Democratic Republic of Timor-Leste','TLS','626','yes','670','.tl'),
(223,'TG','Togo','Togolese Republic','TGO','768','yes','228','.tg'),
(224,'TK','Tokelau','Tokelau','TKL','772','no','690','.tk'),
(225,'TO','Tonga','Kingdom of Tonga','TON','776','yes','676','.to'),
(226,'TT','Trinidad and Tobago','Republic of Trinidad and Tobago','TTO','780','yes','1+868','.tt'),
(227,'TN','Tunisia','Republic of Tunisia','TUN','788','yes','216','.tn'),
(228,'TR','Turkey','Republic of Turkey','TUR','792','yes','90','.tr'),
(229,'TM','Turkmenistan','Turkmenistan','TKM','795','yes','993','.tm'),
(230,'TC','Turks and Caicos Islands','Turks and Caicos Islands','TCA','796','no','1+649','.tc'),
(231,'TV','Tuvalu','Tuvalu','TUV','798','yes','688','.tv'),
(232,'UG','Uganda','Republic of Uganda','UGA','800','yes','256','.ug'),
(233,'UA','Ukraine','Ukraine','UKR','804','yes','380','.ua'),
(234,'AE','United Arab Emirates','United Arab Emirates','ARE','784','yes','971','.ae'),
(235,'GB','United Kingdom','United Kingdom of Great Britain and Nothern Ireland','GBR','826','yes','44','.uk'),
(236,'US','United States','United States of America','USA','840','yes','1','.us'),
(237,'UM','United States Minor Outlying Islands','United States Minor Outlying Islands','UMI','581','no','NONE','NONE'),
(238,'UY','Uruguay','Eastern Republic of Uruguay','URY','858','yes','598','.uy'),
(239,'UZ','Uzbekistan','Republic of Uzbekistan','UZB','860','yes','998','.uz'),
(240,'VU','Vanuatu','Republic of Vanuatu','VUT','548','yes','678','.vu'),
(241,'VA','Vatican City','State of the Vatican City','VAT','336','no','39','.va'),
(242,'VE','Venezuela','Bolivarian Republic of Venezuela','VEN','862','yes','58','.ve'),
(243,'VN','Vietnam','Socialist Republic of Vietnam','VNM','704','yes','84','.vn'),
(244,'VG','Virgin Islands, British','British Virgin Islands','VGB','092','no','1+284','.vg'),
(245,'VI','Virgin Islands, US','Virgin Islands of the United States','VIR','850','no','1+340','.vi'),
(246,'WF','Wallis and Futuna','Wallis and Futuna','WLF','876','no','681','.wf'),
(247,'EH','Western Sahara','Western Sahara','ESH','732','no','212','.eh'),
(248,'YE','Yemen','Republic of Yemen','YEM','887','yes','967','.ye'),
(249,'ZM','Zambia','Republic of Zambia','ZMB','894','yes','260','.zm'),
(250,'ZW','Zimbabwe','Republic of Zimbabwe','ZWE','716','yes','263','.zw');

-- ============================================================================
-- 2) USERS (precisa antes de expenses)
-- ============================================================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `profile` enum('admin','seller','manager') NOT NULL DEFAULT 'seller',
  `code` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `nif` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `birthdate` varchar(100) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `password` varchar(250) NOT NULL DEFAULT '1234',
  `status` TINYINT(1) NOT NULL DEFAULT '0',
  `country` varchar(250) DEFAULT 'Angola',
  `city` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 3) SUPPLIERS (antes de expenses e purchases)
-- ============================================================================
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` varchar(191) NOT NULL,
  `nif` varchar(15) NOT NULL DEFAULT 'XXXXXXXXXX',
  `phone` varchar(30) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT 'desconhecido',
  `country` int NOT NULL DEFAULT '0',
  `city` varchar(100) NOT NULL DEFAULT 'desconhecido',
  `zip_code` varchar(15) NOT NULL,
  `state` varchar(50) NOT NULL DEFAULT 'desconhecido',
  `address` varchar(191) NOT NULL DEFAULT 'desconhecido',
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `group_id` int NOT NULL DEFAULT '0',
  `isdefault` TINYINT(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `suppliers` VALUES
(1,'EN Entrega LDA','XXXXXXXXXXX','XXXXXXXXXXX','XXXXXXXXXXX',1,'desconhecido','ao1','desconhecido','desconhecido',0,0,0,'2024-07-20 20:31:35',NULL,NULL);

-- ============================================================================
-- 4) EXPENSE CATEGORIES (antes de expenses)
-- ============================================================================
DROP TABLE IF EXISTS `expense_categories`;
CREATE TABLE `expense_categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 5) TAXES (antes de products)
-- ============================================================================
DROP TABLE IF EXISTS `taxes`;
CREATE TABLE `taxes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `percentage` decimal(15,2) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `isdefault` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `taxes` VALUES
(1,'IVA',7.00,'IVA',0,0),
(2,'IVA 14',14.00,'IVA',0,0),
(3,'ISENTO',0.00,'ISE',0,0);

-- ============================================================================
-- 6) REASON_TAXES (antes de products)
-- ============================================================================
DROP TABLE IF EXISTS `reason_taxes`;
CREATE TABLE `reason_taxes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `reason` text,
  `standard` varchar(255) DEFAULT NULL,
  `description` text,
  `isdefault` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `reason_taxes` (`id`,`code`,`reason`,`standard`,`description`,`isdefault`) VALUES
(1,'M00','Regime transitório','','',0),
(2,'M02','Transmissão de bens e serviços não sujeita','','',0),
(3,'M04','IVA – Regime de não sujeição','','',0),
(4,'M10','Isento nos termos da alínea b) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','A transmissão dos bens alimentares, conforme anexo I do presente código',0),
(5,'M11','Isento nos termos da alínea b) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','As transmissões de medicamentos destinados exclusivamente a fins terapêuticos e profiláticos',0),
(6,'M12','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','As transmissões de cadeiras de rodas e veículos semelhantes, acionados manualmente ou por motor, para portadores de deficiência; máquinas de escrever com caracteres braille; impressoras para braille; artefactos destinados a invisuais ou para corrigir a audição',0),
(7,'M13','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','A transmissão de livros, incluindo em formato digital',0),
(8,'M14','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','A locação de bens imóveis destinados a fins habitacionais, com exceção das prestações de serviços de alojamento no âmbito da atividade hoteleira',0),
(9,'M15','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','As operações sujeitas ao imposto de SISA, ainda que dele isentas',0),
(10,'M16','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','A exploração e a prática de jogos de fortuna ou azar, bem como operações relacionadas, sujeitas a Imposto Especial sobre Jogos',0),
(11,'M17','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','O transporte coletivo de passageiros',0),
(12,'M18','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','As operações de intermediação financeira, incluindo locação financeira (exceto quando há taxa específica predeterminada)',0),
(13,'M19','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','Seguro de saúde, seguros e resseguros do ramo vida',0),
(14,'M20','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','As transmissões de produtos petrolíferos conforme anexo II do CIVA',0),
(15,'M21','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','Serviços de ensino prestados por estabelecimentos integrados no sistema de educação reconhecido',0),
(16,'M22','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','Serviços médico-sanitários prestados por hospitais, clínicas, dispensários e similares',0),
(17,'M23','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','Transporte de doentes ou feridos em ambulâncias ou veículos apropriados',0),
(18,'M24','Isento nos termos da alínea c) do nº 1 do artigo 12.º do CIVA','Artigo 12.º do CIVA','Equipamentos médicos para atividade de estabelecimentos de saúde',0),
(19,'M30','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Bens expedidos ou transportados com destino ao estrangeiro pelo vendedor ou terceiro por conta deste',0),
(20,'M31','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Abastecimentos postos a bordo de embarcações em alto mar que assegurem transporte remunerado de passageiros ou atividade comercial',0),
(21,'M32','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Abastecimentos postos a bordo de aeronaves de tráfego internacional',0),
(22,'M33','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Abastecimentos postos a bordo de embarcações de salvamento, assistência marítima, pesca costeira ou de guerra, com destino ao estrangeiro',0),
(23,'M34','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Transmissões, transformações, reparações, manutenção, frete e aluguer de embarcações/aeronaves do tráfego internacional',0),
(24,'M35','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Transmissões de bens no âmbito de relações diplomáticas e consulares com isenção por acordos internacionais',0),
(25,'M36','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Transmissões de bens destinados a organismos internacionais reconhecidos por Angola',0),
(26,'M37','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Transmissões de bens em tratados e acordos internacionais da República de Angola',0),
(27,'M38','Isento nos termos da alínea h) do artigo 15.º do CIVA','Artigo 15.º do CIVA','Transporte de pessoas provenientes ou com destino ao estrangeiro',0),
(28,'M80','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Importações definitivas de bens cuja transmissão seja isenta de imposto',0),
(29,'M81','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Importação de ouro, moedas ou notas de banco efetuadas pelo Banco Nacional de Angola',0),
(30,'M82','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Importação de bens destinados a ofertas para atenuar calamidades naturais, autorizadas pelo Executivo',0),
(31,'M83','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Importação de mercadorias ou equipamentos destinados exclusivamente a operações petrolíferas ou mineiras',0),
(32,'M84','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Importação de moeda estrangeira por instituições financeiras bancárias',0),
(33,'M85','Isento nos termos da alínea b) do nº 2 do artigo 14.º do CIVA','Artigo 14.º do CIVA','Transmissões de bens no âmbito de tratados internacionais celebrados por Angola',0),
(34,'M86','Isento nos termos da alínea b) do artigo 14.º do CIVA','Artigo 14.º do CIVA','Transmissões de bens no âmbito de relações diplomáticas e consulares com isenção prevista em tratados',0),
(35,'M90','Isento nos termos da alínea a) do nº 1 do artigo 16.º do CIVA','Artigo 16.º do CIVA','Importações de bens em regimes aduaneiros especiais (zona franca, armazéns aduaneiros, lojas francas)',0),
(36,'M91','Isento nos termos da alínea a) do nº 1 do artigo 16.º do CIVA','Artigo 16.º do CIVA','Transmissões de bens ou serviços associados a zonas francas ou depósitos aduaneiros',0),
(37,'M92','Isento nos termos da alínea a) do nº 1 do artigo 16.º do CIVA','Artigo 16.º do CIVA','Transmissões de bens e serviços em regimes de trânsito, drawback ou importação temporária',0),
(38,'M93','Isento nos termos da alínea a) do nº 1 do artigo 16.º do CIVA','Artigo 16.º do CIVA','Transmissões de bens abrangidos por regimes especiais de importação/exportação',0),
(39,'M94','Isento nos termos da alínea a) do nº 1 do artigo 16.º do CIVA','Artigo 16.º do CIVA','Reimportação de bens no mesmo estado em que foram exportados, com isenção de direitos',0),
(40,'Nenhum','Nenhum','Nenhum','Nenhum',0);

-- ============================================================================
-- 7) GROUPS_PRODUCT (antes de products)
-- ============================================================================
DROP TABLE IF EXISTS `groups_product`;
CREATE TABLE `groups_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `groups_product` VALUES (1,'Nenhum',''),(2,'Grupo 1','GP1');

-- ============================================================================
-- 8) WAREHOUSES (usado por stock_movements)
-- ============================================================================
DROP TABLE IF EXISTS `warehouses`;
CREATE TABLE `warehouses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `warehouses` (`id`,`name`,`location`,`description`) VALUES
(1,'Armazem 1','Desconhecido','Desconhecido');

-- ============================================================================
-- 9) PRODUCTS (depois de taxes, reason_taxes, groups_product, suppliers)
-- ============================================================================
DROP TABLE IF EXISTS `products`;
CREATE TABLE products (
  id INT NOT NULL AUTO_INCREMENT,
  type ENUM('product','service') NOT NULL DEFAULT 'product',
  code VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  purchase_price DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  tax_id INT NOT NULL,
  reason_tax_id INT NOT NULL,
  group_id INT NOT NULL,
  barcode VARCHAR(255) NOT NULL,
  status TINYINT(1) NOT NULL DEFAULT 0,
  min_stock INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_products_group (group_id),
  KEY idx_products_tax (tax_id),
  KEY idx_products_reason_tax (reason_tax_id),
  CONSTRAINT fk_products_group 
      FOREIGN KEY (group_id) REFERENCES groups_product(id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_products_tax 
      FOREIGN KEY (tax_id) REFERENCES taxes(id)
      ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_products_reason_tax 
      FOREIGN KEY (reason_tax_id) REFERENCES reason_taxes(id)
      ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `products` (
  type, code, description, price, purchase_price,
  tax_id, reason_tax_id, group_id, status, barcode,
  min_stock, created_at, updated_at, deleted_at
) VALUES
('product','P1001','Arroz Branco 5Kg',4500.00,3500.00,1,1,1,1,'789123456001',10,'2025-01-01 10:00:00','2025-01-01 10:00:00',NULL),
('product','P1002','Feijão Preto 1Kg',1200.00,900.00,1,1,1,1,'789123456002',20,'2025-01-02 11:00:00','2025-01-02 11:00:00',NULL),
('product','P1003','Óleo de Cozinha 1L',2500.00,2100.00,1,1,2,1,'789123456003',15,'2025-01-03 12:00:00','2025-01-03 12:00:00',NULL),
('product','P1004','Refrigerante Cola 1.5L',1000.00,700.00,1,1,2,1,'789123456004',30,'2025-01-04 13:00:00','2025-01-04 13:00:00',NULL),
('product','P1005','Água Mineral 500ml',500.00,300.00,1,1,2,1,'789123456005',50,'2025-01-05 14:00:00','2025-01-05 14:00:00',NULL),
('product','P1006','Detergente Líquido 1L',1800.00,1500.00,1,1,3,1,'789123456006',10,'2025-01-06 15:00:00','2025-01-06 15:00:00',NULL),
('product','P1007','Sabão em Barra 5un',700.00,500.00,1,1,3,1,'789123456007',25,'2025-01-07 16:00:00','2025-01-07 16:00:00',NULL),
('product','P1008','Queijo Mussarela 1Kg',9500.00,8000.00,1,1,1,1,'789123456008',5,'2025-01-08 17:00:00','2025-01-08 17:00:00',NULL),
('service','S1001','Serviço de Entrega Local',2000.00,0.00,1,1,3,1,'789123456009',0,'2025-01-09 18:00:00','2025-01-09 18:00:00',NULL),
('service','S1002','Serviço de Montagem',5000.00,0.00,1,1,3,1,'789123456010',0,'2025-01-10 19:00:00','2025-01-10 19:00:00',NULL);

-- ============================================================================
-- 10) CLIENTS
-- ============================================================================
DROP TABLE IF EXISTS `clients`;
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
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `group_id` int NOT NULL DEFAULT '0',
  `isdefault` TINYINT(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `clients` VALUES
(1,'Consumidor Final','XXXXXXXXXXX','XXXXXXXXXXX','XXXXXXXXXXX',7,'Desconecido','AO','Desconehcido','Desconhecido',1,0,1,'2024-07-19 19:55:45',NULL,NULL);

-- ============================================================================
-- 11) ORDERS
-- ============================================================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `datecreate` varchar(255) NOT NULL,
  `duedate` varchar(255) NOT NULL DEFAULT '0',
  `year` int NOT NULL,
  `number` int NOT NULL,
  `prefix` varchar(11) NOT NULL,
  `client_id` int NOT NULL,
  `user_id` int NOT NULL,
  `total` double DEFAULT NULL,
  `sub_total` double DEFAULT '0',
  `total_taxe` double DEFAULT '0',
  `pay_total` double NOT NULL DEFAULT '0',
  `amount_returned` decimal(15,2) NOT NULL DEFAULT '0.00',
  `hash` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `note` text,
  `key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Produtos do pedido
DROP TABLE IF EXISTS `products_order`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 12) PAYMENT MODES
-- ============================================================================
DROP TABLE IF EXISTS `payment_modes`;
CREATE TABLE `payment_modes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `code` VARCHAR(50) NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `isDefault` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_payment_modes_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `payment_modes` (`name`,`description`,`code`,`status`,`isDefault`) VALUES
('Numerário','Pagamento em dinheiro vivo','NU','1',1),
('Cartão de Crédito','Pagamento com cartão de crédito','CC','1',0),
('Cartão de Débito','Pagamento com cartão de débito','CD','1',0),
('Cartão de Serviço','Pagamento com cartão de serviço','CS','1',0),
('Multicaixa','Pagamento em terminal multicaixa','MB','1',0),
('Cheque','Pagamento por cheque','CH','1',0),
('Transferência Bancária','Pagamento por transferência bancária','TR','1',0),
('Débito em Conta','Débito direto em conta','DE','1',0),
('Crédito em Conta','Crédito direto em conta','CR','1',0),
('Compensação','Compensação de saldos em conta corrente','CO','1',0),
('Crédito Documentário','Pagamento via crédito documentário','CI','1',0),
('Outros','Outros meios de pagamento','OU','1',0);

-- ============================================================================
-- 13) PAYMENTS
-- ============================================================================
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL DEFAULT '',
  `total` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `prefix` VARCHAR(11) NOT NULL,
  `number` INT NOT NULL,
  `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateFinish` DATETIME NULL,
  `status` ENUM('SUCCESS','FAILED') NOT NULL DEFAULT 'SUCCESS',
  `mode` ENUM('NUMERARIO','MULTICAIXA','TRANSFERENCIA','OUTROS') NOT NULL,
  `reference` VARCHAR(100) NULL,
  `currency` VARCHAR(10) NOT NULL DEFAULT 'AOA',
  `clientId` INT NOT NULL,
  `userId` INT NOT NULL,
  `order_id` INT NOT NULL,
  `order_type` ENUM('ORDER','INVOICE','CREDIT_NOTE') NOT NULL DEFAULT 'ORDER',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `idx_payments_date` (`date`),
  KEY `idx_payments_order` (`order_id`, `order_type`),
  KEY `idx_payments_prefix_number` (`prefix`, `number`),
  KEY `idx_payments_client` (`clientId`),
  KEY `idx_payments_user` (`userId`),
  KEY `idx_payments_mode` (`mode`),
  KEY `idx_payments_status` (`status`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 14) PURCHASES (e itens/pagamentos)
-- ============================================================================
DROP TABLE IF EXISTS `purchase_payments`;
DROP TABLE IF EXISTS `purchase_items`;
DROP TABLE IF EXISTS `purchases`;

CREATE TABLE `purchases` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `supplier_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `invoice_number` VARCHAR(50) NOT NULL,
  `invoice_type` ENUM('FT','FR','NC','ND') DEFAULT 'FT',
  `descricao` VARCHAR(255) NULL,
  `total` DECIMAL(15,2) NOT NULL,
  `iva_total` DECIMAL(15,2) DEFAULT 0.00,
  `total_pago` DECIMAL(15,2) DEFAULT 0.00,
  `saldo_em_aberto` DECIMAL(15,2) DEFAULT 0.00,
  `data_compra` DATE NOT NULL,
  `data_vencimento` DATE NOT NULL,
  `status` ENUM('aberto','parcial','pago','atrasado') DEFAULT 'aberto',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_purchases_supplier` (`supplier_id`),
  CONSTRAINT `fk_purchases_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `purchase_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `purchase_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `quantidade` INT NOT NULL,
  `preco_custo` DECIMAL(15,2) NOT NULL,
  `iva` DECIMAL(15,2) DEFAULT 0.00,
  `subtotal` DECIMAL(15,2) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pi_purchase` (`purchase_id`),
  KEY `idx_pi_product` (`product_id`),
  CONSTRAINT `fk_pi_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchases`(`id`),
  CONSTRAINT `fk_pi_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `purchase_payments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `purchase_id` INT NOT NULL,
  `valor_pago` DECIMAL(15,2) NOT NULL,
  `data_pagamento` DATE NOT NULL,
  `metodo` ENUM('dinheiro','transferencia','cartao','cheque','outro') DEFAULT 'dinheiro',
  `referencia` VARCHAR(100) NULL,
  `observacao` TEXT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pp_purchase` (`purchase_id`),
  CONSTRAINT `fk_pp_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchases`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 15) SHIFT
-- ============================================================================
DROP TABLE IF EXISTS `shift`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 16) STOCK MOVEMENTS (referencia products/warehouses)
-- ============================================================================
DROP TABLE IF EXISTS `stock_movements`;
CREATE TABLE `stock_movements` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `warehouse_id` INT NOT NULL DEFAULT '0',
  `user_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `type` ENUM('ENTRADA','SAIDA','AJUSTE','TRANSFERENCIA') NOT NULL,
  `origin` ENUM('COMPRA','VENDA','DEVOLUCAO','MANUAL','AJUSTE','TRANSFERENCIA') NOT NULL,
  `reference_id` INT NULL,
  `notes` VARCHAR(255) NULL,
  `reason` VARCHAR(255) NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_product` (`product_id`),
  KEY `idx_sm_warehouse` (`warehouse_id`),
  CONSTRAINT `fk_sm_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`),
  CONSTRAINT `fk_sm_warehouse` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 17) EXPENSES (depois de users, suppliers, expense_categories)
-- ============================================================================
DROP TABLE IF EXISTS `expenses`;
CREATE TABLE `expenses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL DEFAULT '',
  `total` DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  `prefix` VARCHAR(11) NOT NULL,
  `number` INT NOT NULL,
  `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateFinish` DATETIME NULL,
  `status` ENUM('SUCCESS','FAILED') NOT NULL DEFAULT 'SUCCESS',
  `mode` ENUM('NUMERARIO','MULTICAIXA','TRANSFERENCIA','OUTROS') NOT NULL,
  `reference` VARCHAR(100) NULL,
  `notes` TEXT NULL,
  `currency` VARCHAR(10) NOT NULL DEFAULT 'AOA',
  `supplier_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `category_id` INT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `idx_expenses_date` (`date`),
  KEY `idx_expenses_supplier` (`supplier_id`),
  KEY `idx_expenses_user` (`user_id`),
  KEY `idx_expenses_mode` (`mode`),
  KEY `idx_expenses_status` (`status`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_expenses_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`),
  CONSTRAINT `fk_expenses_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
  CONSTRAINT `fk_expenses_category` FOREIGN KEY (`category_id`) REFERENCES `expense_categories`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 18) BOX
-- ============================================================================
DROP TABLE IF EXISTS `box`;
CREATE TABLE `box` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_box_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 19) OPTIONS
-- ============================================================================
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `value` VARCHAR(255) NOT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Recria o índice único de forma idempotente (compatível com MariaDB)
DROP INDEX IF EXISTS `uq_options_name` ON `options`;
CREATE UNIQUE INDEX `uq_options_name` ON `options` (`name`);

INSERT INTO `options` (`name`,`value`,`status`) VALUES
  ('softwareStatus','true','1'),
  ('softwareNumberValidate','','1'),
  ('softwareCompanyName','','1'),
  ('softwareCompanyNif','','1'),
  ('companyKeyLicence','','1'),
  ('entity','hash','1'),
  ('companyCity','','1'),
  ('companyAddress','','1'),
  ('companyPhone','','1'),
  ('companyName','SupePDV','1'),
  ('companyNif','123456789','1'),
  ('country','Angola','1'),
  ('country_short_name','Angola','1'),
  ('country_long_name','República de Angola','1'),
  ('country_iso2','AO','1'),
  ('country_iso3','AGO','1'),
  ('country_numcode','024','1'),
  ('country_calling_code','+244','1'),
  ('country_cctld','.ao','1'),
  ('install_complete','true','1'),
  ('schema_version','1.0.0','1'),
  ('last_migration_ts', NOW(), '1')
ON DUPLICATE KEY UPDATE
  `value`=VALUES(`value`),
  `status`=VALUES(`status`);

-- ============================================================================
-- 20) SAFT EXPORTS
-- ============================================================================
DROP TABLE IF EXISTS `saft_exports`;
CREATE TABLE `saft_exports` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `period_start` DATE NOT NULL,
  `period_end` DATE NOT NULL,
  `file_path` VARCHAR(512) NOT NULL,
  `status` VARCHAR(32) NOT NULL,
  `notes` TEXT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `exported_by` INT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- FIM
-- ============================================================================
SET FOREIGN_KEY_CHECKS = 1;
