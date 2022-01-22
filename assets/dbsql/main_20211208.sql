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

CREATE TABLE `inner_account` (
	`account_name` VARCHAR(32) NOT NULL COMMENT '内部账号' COLLATE 'utf8mb4_unicode_ci',
	`password` VARCHAR(32) NOT NULL COMMENT '账号密码' COLLATE 'utf8mb4_unicode_ci',
	`user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
	PRIMARY KEY (`account_name`) USING BTREE
)
COMMENT='医院用户账号信息'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `user` (
	`user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
	`name` VARCHAR(32) NOT NULL COMMENT '用户名' COLLATE 'utf8mb4_unicode_ci',
	`user_type` TINYINT(10) NOT NULL COMMENT '用户类型0:Unknown,1:Patient,2:Doctor',
	PRIMARY KEY (`user_id`) USING BTREE
)
COMMENT='用户信息表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `user_doctor` (
	`user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`staff_id` VARCHAR(32) NOT NULL COMMENT '医生工号' COLLATE 'utf8mb4_unicode_ci',
	`status` TINYINT(10) NOT NULL COMMENT '授权状态0:Unknown,1:UnderApply,2:Valid,3:Invalid',
	`department_ids` VARCHAR(255) NOT NULL COMMENT '授权科室Id数组' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`user_id`) USING BTREE
)
COMMENT='医院工号信息'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `user_profile` (
	`user_id` BIGINT(20) NOT NULL COMMENT '用户唯一Id',
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`profile_id` BIGINT(20) NOT NULL COMMENT '档案Id',
	PRIMARY KEY (`user_id`, `hospital_id`, `profile_id`) USING BTREE
)
COMMENT='用户档案表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `profile` (
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`profile_id` BIGINT(20) NOT NULL COMMENT '档案Id',
	`personal_id` VARCHAR(20) NOT NULL COMMENT '身份证号' COLLATE 'utf8mb4_unicode_ci',
	`gender` TINYINT(20) NOT NULL COMMENT '性别0:Male,1:Female',
	`pid_address` VARCHAR(255) NOT NULL COMMENT '身份证地址' COLLATE 'utf8mb4_unicode_ci',
	`home_address` VARCHAR(255) NOT NULL COMMENT '家庭住址' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`hospital_id`, `profile_id`) USING BTREE,
	INDEX `hospital_personal` (`hospital_id`, `personal_id`) USING BTREE
)
COMMENT='医院档案表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `booking` (
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`profile_id` BIGINT(20) NOT NULL COMMENT '档案Id',
	`booking_id` BIGINT(20) NOT NULL COMMENT '预约Id',
	`date_time` DATETIME(3) NOT NULL COMMENT '预约时间',
	`combo_ids` VARCHAR(255) NOT NULL COMMENT '套餐ids' COLLATE 'utf8mb4_unicode_ci',
	`booking_status` TINYINT(10) NOT NULL COMMENT '预约状态0:Processing,1:Completed',
	PRIMARY KEY (`hospital_id`, `profile_id`, `booking_id`) USING BTREE
)
COMMENT='医院预约表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `department_items` (
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`profile_id` BIGINT(20) NOT NULL COMMENT '档案Id',
	`booking_id` BIGINT(20) NOT NULL COMMENT '预约Id',
	`department_id` BIGINT(20) NOT NULL COMMENT '科室Id',
	`item_values` VARCHAR(1000) NOT NULL COMMENT '体检指标项目值,格式如{itemId:itemValue,itemId:itemValue}' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`hospital_id`, `profile_id`, `booking_id`, `department_id`) USING BTREE
)
COMMENT='科室体检指标项目结果表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;

CREATE TABLE `hospital` (
	`hospital_id` INT(10) NOT NULL COMMENT '医院id',
	`name` VARCHAR(255) NOT NULL COMMENT '医院名称' COLLATE 'utf8mb4_unicode_ci',
	PRIMARY KEY (`hospital_id`) USING BTREE
)
COMMENT='医院表'
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;
