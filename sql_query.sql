create database blog_post_db;
 
create table users(
	`user_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_name` VARCHAR(100) NULL,
	`email` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`user_id`),
	UNIQUE KEY `UK_user_email` (`email`)
);

create table blogs(
	`blog_id` BIGINT NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(255) NOT NULL, 
	`blog_text` TEXT NULL,
	`creator_id` BIGINT NOT NULL,
	`created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`rec_status` VARCHAR(50) DEFAULT 'CREATED',
	PRIMARY KEY(`blog_id`),
	KEY `fk_blogs_to_users`(`creator_id`),
	CONSTRAINT `fk_blogs_to_users` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table comments(
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`comment_text` TEXT NULL,
	`blog_id` BIGINT NOT NULL,
	`creator_id` BIGINT NOT NULL,
	`created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	`rec_status` VARCHAR(50) DEFAULT 'CREATED',
	PRIMARY KEY(`id`),
	KEY `fk_comments_to_blogs`(`blog_id`),
	KEY `fk_comments_to_users`(`creator_id`),
	CONSTRAINT `fk_comments_to_blogs` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`blog_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT `fk_comments_to_users` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
);
