LOCK TABLES `test_points` WRITE, `test_trajectories` WRITE;
ALTER TABLE `test_points` ADD `source` varchar(255) NOT NULL DEFAULT 'User' AFTER `activity`;
UNLOCK TABLES;