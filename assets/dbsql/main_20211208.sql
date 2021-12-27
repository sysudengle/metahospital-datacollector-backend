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

CREATE TABLE `inner_account` (
    `inner_account` VARCHAR(32) NOT NULL COMMENT '内部账号' COLLATE 'utf8mb4_unicode_ci',
    `password` VARCHAR(32) NOT NULL COMMENT '账号密码' COLLATE 'utf8mb4_unicode_ci',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
    PRIMARY KEY (`inner_account`) USING BTREE
)
    COMMENT='医院用户账号信息'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;


CREATE TABLE `user_doctor_info` (
   `user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
   `hospital_id` INT(11) NOT NULL COMMENT '医院id',
   `staff_id` VARCHAR(32) NOT NULL COMMENT '医生工号' COLLATE 'utf8mb4_unicode_ci',
   `permission_state` INT(11) NOT NULL COMMENT '申请状态',
   `department_permission` VARCHAR(32) NOT NULL COMMENT '科室权限' COLLATE 'utf8mb4_unicode_ci',
   PRIMARY KEY (`user_id`) USING BTREE
)
    COMMENT='医院工号信息'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

