# Create Category Table
CREATE TABLE `Category` (
                            `category_id` varchar(255) NOT NULL,
                            `name` varchar(255) NOT NULL,
                            `parent` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# Create Post Table
CREATE TABLE `Post` (
                        `post_id` varchar(255) NOT NULL,
                        `author_id` varchar(255) NOT NULL,
                        `title` varchar(255) NOT NULL,
                        `created_timestamp` datetime NOT NULL,
                        `last_modified_timestamp` varchar(255) NOT NULL,
                        `categories` json DEFAULT NULL,
                        `custom_categories` json DEFAULT NULL,
                        `media` json DEFAULT NULL,
                        `description` varchar(255) DEFAULT NULL,
                        `likes` json DEFAULT NULL,
                        PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# Create User Table
CREATE TABLE `User` (
                        `user_id` varchar(255) NOT NULL,
                        `name` varchar(255) NOT NULL,
                        `email_id` varchar(255) DEFAULT NULL,
                        `created_timestamp` datetime NOT NULL,
                        `contact_number` varchar(255) DEFAULT NULL,
                        `profile_picture` varchar(255) DEFAULT NULL,
                        `bio` varchar(255) DEFAULT NULL,
                        `birthday` datetime DEFAULT NULL,
                        `gender` varchar(255) DEFAULT NULL,
                        `liked_posts` json DEFAULT NULL,
                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

