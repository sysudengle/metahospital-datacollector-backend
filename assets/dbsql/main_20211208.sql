use main;

CREATE TABLE `wechat_account` (
    `open_id` VARCHAR(32) NOT NULL COMMENT '微信用户唯一标识' COLLATE 'utf8mb4_unicode_ci',
    `union_id` VARCHAR(32) NOT NULL COMMENT '微信用户在开放平台的唯一标识符' COLLATE 'utf8mb4_unicode_ci',
    `session_key` VARCHAR(32) NOT NULL COMMENT '微信会话密钥' COLLATE 'utf8mb4_unicode_ci',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
    PRIMARY KEY (`open_id`) USING BTREE,
    INDEX `user_id` (`user_id`)
)
COMMENT='微信用户信息表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `user` (
    `user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
    `name` VARCHAR(32) NOT NULL COMMENT '用户名' COLLATE 'utf8mb4_unicode_ci',
    PRIMARY KEY (`user_id`) USING BTREE
)
COMMENT='用户信息表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;
