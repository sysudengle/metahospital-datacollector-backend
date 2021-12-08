use main;

CREATE TABLE `user` (
	`id` VARCHAR(64) NOT NULL COMMENT '用户唯一Id' COLLATE 'utf8mb4_unicode_ci',
	`user_key` VARCHAR(64) NOT NULL COMMENT '用户唯一key' COLLATE 'utf8mb4_unicode_ci',
	`user_secret` VARCHAR(64) NOT NULL COMMENT '用户密码' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `user_key` (`user_key`)
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;
