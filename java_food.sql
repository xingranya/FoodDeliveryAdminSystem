/*
 Navicat Premium Dump SQL

 Source Server         : java_food
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : java_food

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 21/06/2025 20:46:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of categories
-- ----------------------------
BEGIN;
INSERT INTO `categories` (`id`, `name`, `created_at`, `updated_at`) VALUES (1, '主食', '2025-06-10 10:12:04', '2025-06-16 21:33:47');
INSERT INTO `categories` (`id`, `name`, `created_at`, `updated_at`) VALUES (2, '炸物', '2025-06-16 20:37:17', '2025-06-16 20:37:20');
INSERT INTO `categories` (`id`, `name`, `created_at`, `updated_at`) VALUES (3, '小食', '2025-06-16 20:37:46', '2025-06-16 20:37:48');
INSERT INTO `categories` (`id`, `name`, `created_at`, `updated_at`) VALUES (4, '酒水', '2025-06-16 20:38:04', '2025-06-16 20:38:06');
INSERT INTO `categories` (`id`, `name`, `created_at`, `updated_at`) VALUES (7, '菜品', '2025-06-21 20:21:55', '2025-06-21 20:21:55');
COMMIT;

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `rating` int NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `comment_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of comments
-- ----------------------------
BEGIN;
INSERT INTO `comments` (`id`, `user_id`, `dish_id`, `rating`, `content`, `comment_time`, `created_at`, `updated_at`) VALUES (1, 1, 2, 5, '大白馒头真美味啊，这才是经典经久不衰，每天必点！', '2025-06-17 11:30:01', '2025-06-17 11:30:01', '2025-06-17 11:30:01');
INSERT INTO `comments` (`id`, `user_id`, `dish_id`, `rating`, `content`, `comment_time`, `created_at`, `updated_at`) VALUES (2, 2, 3, 5, '这个也很美味', '2025-06-21 17:52:43', '2025-06-21 17:52:43', '2025-06-21 17:53:02');
COMMIT;

-- ----------------------------
-- Table structure for dish_tags
-- ----------------------------
DROP TABLE IF EXISTS `dish_tags`;
CREATE TABLE `dish_tags` (
  `dish_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`dish_id`,`tag_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `dish_tags_ibfk_1` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `dish_tags_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of dish_tags
-- ----------------------------
BEGIN;
INSERT INTO `dish_tags` (`dish_id`, `tag_id`) VALUES (4, 1);
INSERT INTO `dish_tags` (`dish_id`, `tag_id`) VALUES (5, 1);
INSERT INTO `dish_tags` (`dish_id`, `tag_id`) VALUES (3, 2);
COMMIT;

-- ----------------------------
-- Table structure for dishes
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` double NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `category_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of dishes
-- ----------------------------
BEGIN;
INSERT INTO `dishes` (`id`, `name`, `price`, `status`, `description`, `category_id`, `created_at`, `updated_at`) VALUES (1, '米饭', 2, '上架', '香香软软大白米饭', 1, '2025-06-10 10:12:41', '2025-06-10 10:12:41');
INSERT INTO `dishes` (`id`, `name`, `price`, `status`, `description`, `category_id`, `created_at`, `updated_at`) VALUES (2, '大白馒头', 2, '上架', '香香软软的大白馒头', 1, '2025-06-10 10:52:59', '2025-06-10 10:52:59');
INSERT INTO `dishes` (`id`, `name`, `price`, `status`, `description`, `category_id`, `created_at`, `updated_at`) VALUES (3, '川香水煮肉片米线', 10, '上架', '地道川渝味', 1, '2025-06-16 17:38:29', '2025-06-16 17:38:29');
INSERT INTO `dishes` (`id`, `name`, `price`, `status`, `description`, `category_id`, `created_at`, `updated_at`) VALUES (4, '炸酱面/米线', 6, '上架', '经典炸酱', 1, '2025-06-16 17:39:09', '2025-06-16 17:39:09');
INSERT INTO `dishes` (`id`, `name`, `price`, `status`, `description`, `category_id`, `created_at`, `updated_at`) VALUES (5, '热干面', 4, '上架', '地道武汉热干面', 1, '2025-06-16 19:38:05', '2025-06-16 19:38:05');
COMMIT;

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operator_id` int DEFAULT NULL,
  `operation` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of logs
-- ----------------------------
BEGIN;
INSERT INTO `logs` (`id`, `operator_id`, `operation`, `operation_time`, `created_at`, `updated_at`) VALUES (1, 1, '添加菜品', '2025-06-16 20:35:33', '2025-06-16 20:35:43', '2025-06-16 20:35:40');
COMMIT;

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price` double NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of order_items
-- ----------------------------
BEGIN;
INSERT INTO `order_items` (`id`, `order_id`, `dish_id`, `quantity`, `price`, `created_at`, `updated_at`) VALUES (5, 8, 1, 1, 2, '2025-06-21 20:39:47', '2025-06-21 20:39:47');
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `order_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_amount` double NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO `orders` (`id`, `user_id`, `order_time`, `total_amount`, `status`, `created_at`, `updated_at`) VALUES (8, 4, '2025-06-21 20:39:48', 2, '待支付', '2025-06-21 20:39:47', '2025-06-21 20:39:47');
COMMIT;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of tags
-- ----------------------------
BEGIN;
INSERT INTO `tags` (`id`, `name`, `created_at`, `updated_at`) VALUES (1, '推荐', '2025-06-10 11:08:10', '2025-06-10 11:08:10');
INSERT INTO `tags` (`id`, `name`, `created_at`, `updated_at`) VALUES (2, '热门', '2025-06-10 11:08:17', '2025-06-10 11:08:17');
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`, `updated_at`) VALUES (1, 'admin', 'admin', 'admin', '2025-06-10 10:42:46', '2025-06-10 10:42:52');
INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`, `updated_at`) VALUES (2, 'defualtUser', 'defualtUser', 'user', '2025-06-17 14:31:26', '2025-06-17 14:31:26');
INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`, `updated_at`) VALUES (3, '小明', 'xiaoming', 'user', '2025-06-17 14:31:51', '2025-06-17 16:57:48');
INSERT INTO `users` (`id`, `username`, `password`, `role`, `created_at`, `updated_at`) VALUES (4, '小李', 'xiaoli ', 'admin', '2025-06-17 14:32:41', '2025-06-17 14:32:41');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
