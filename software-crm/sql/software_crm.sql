/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : software_crm

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 14/08/2022 00:11:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `level` int(11) NULL DEFAULT NULL COMMENT '等级',
  `credit_level` int(11) NULL DEFAULT NULL COMMENT '信用度',
  `manager` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经理',
  `legal_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人',
  `lose_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流失原因',
  PRIMARY KEY (`id`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client` VALUES (4, 'wen5', '12345678911', 'test', 2, 50, 'wen', 'wen', 'g');
INSERT INTO `client` VALUES (5, '3', '3', '3', 3, 3, '3', '3', '3');
INSERT INTO `client` VALUES (6, '3', '3', '3', 3, 3, '3', '3', '3');
INSERT INTO `client` VALUES (7, '3', '3', '3', 3, 3, '3', '3', '3');
INSERT INTO `client` VALUES (8, '3', '3', '3', 3, 3, '3', '3', '3');
INSERT INTO `client` VALUES (9, '3', '3', '3', 3, 3, '3', '3', '3');
INSERT INTO `client` VALUES (10, '2', '2', '2', 2, 2, '2', '2', '2');
INSERT INTO `client` VALUES (11, '3', '3', '3', 3, 3, '3', '3', '3');

-- ----------------------------
-- Table structure for serving
-- ----------------------------
DROP TABLE IF EXISTS `serving`;
CREATE TABLE `serving`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of serving
-- ----------------------------
INSERT INTO `serving` VALUES (7, '3', '3', '3', '45', '3', '2022-08-13 22:04:57');
INSERT INTO `serving` VALUES (8, '3', '3', '3', '444', 'wen', '2022-08-13 22:04:59');
INSERT INTO `serving` VALUES (9, '4444', '3', '3', '45', '3', '2022-08-13 22:05:00');
INSERT INTO `serving` VALUES (10, '3', '3', '3', '3', '3', '2022-08-13 22:05:00');
INSERT INTO `serving` VALUES (11, '3', '3', '3', '3', '3', '2022-08-13 22:05:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `login_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `user_type` int(11) NOT NULL COMMENT '用户类型 30:超级管理员,20:管理员,10:普通用户',
  `phone_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name`(`login_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2101311780 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (21013, 'wen', 'wen', '1111', 6, '12331231313123', '2280400645@qq.com2222', '2022-07-19 20:11:43');
INSERT INTO `user` VALUES (21017, 'long', 'long', '123', 3, '', 'long', '2022-07-23 20:25:07');
INSERT INTO `user` VALUES (21043, 'hai', 'hai', '123', 10, NULL, 'hai', '2022-08-13 17:42:51');

SET FOREIGN_KEY_CHECKS = 1;
