BEGIN TRANSACTION;
CREATE TABLE `testarn2` (
	`_id`	INTEGER
);
CREATE TABLE `testarn` (
	`_id`	INTEGER,
	`texten`	TEXT
);
INSERT INTO `testarn` VALUES (1,'testar');
COMMIT;
