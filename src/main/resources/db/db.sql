/*
 Navicat Premium Data Transfer

 Source Server         : 203.195.157.56
 Source Server Type    : MariaDB
 Source Server Version : 100313
 Source Host           : 203.195.157.56:3306
 Source Schema         : am-dev

 Target Server Type    : MariaDB
 Target Server Version : 100313
 File Encoding         : 65001

 Date: 11/04/2019 10:42:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`  (
  `id` bigint(20) NOT NULL,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `creator` bigint(20) NULL DEFAULT NULL,
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` int(11) NULL DEFAULT NULL,
  `key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES (719342979826847744, 'admin', 'http://static.ruanxuefeng.xyz/avatar/719342979826847744.jpg', '2019-01-17 16:15:09', NULL, 'admin@am.com', 0, 'NVFeoiEa9knTOfGbP4caXjtNd7xCb91oFPlheS6C6O7e3clPCXR2XfUJL8OAimIIM4xpwlwviIEQyOwCWqhZFSucdAIg0gTNc3vYQKyKi9tevyiF5wFqmoZbHSw2nJKUkAM6yN7O5P0L6JuEfSVHdPsvN2jeXev4GDf1biGYsjLmej3yZEgWkBuzHzOIrG7amZyMmS8LWas6KFN1kkamgtO0RVwymuFBSQx0hEC9kArL5ka5fCFkRvKUsTMIgQc6m2erSaeZqp0SWcMyaX3VBtcmAr2Qc02DOojJLJsDxN2WH2s0RdJTWLByRvIAa5zNkXzkWfIPAB5OGjJeHeIumNj3vqz78fhpTMh8vHm4Mc2ex0e9szlUO97hXzEFGGtKoGpyxNuqCnIuMOc4t9HFv7B7NTzUY9O5aXgkBbRiSW1APm5F6sP30GyHWrNEwVfDKICSEUbRXCLlCeXXLQYoOlyaLqnEeaAKGujukYiDMLK685maJEc7S5m8KcoBGUf2', 'admin', 'VwkmB6BhKU0=');
INSERT INTO `admin_user` VALUES (719342979826847745, 'zhangsan', 'http://static.ruanxuefeng.xyz/avatar/719342979826847745.jpg', '2019-01-17 16:15:09', 719342979826847744, 'zhangsan@am.com', 0, 'NVFeoiEa9knTOfGbP4caXjtNd7xCb91oFPlheS6C6O7e3clPCXR2XfUJL8OAimIIM4xpwlwviIEQyOwCWqhZFSucdAIg0gTNc3vYQKyKi9tevyiF5wFqmoZbHSw2nJKUkAM6yN7O5P0L6JuEfSVHdPsvN2jeXev4GDf1biGYsjLmej3yZEgWkBuzHzOIrG7amZyMmS8LWas6KFN1kkamgtO0RVwymuFBSQx0hEC9kArL5ka5fCFkRvKUsTMIgQc6m2erSaeZqp0SWcMyaX3VBtcmAr2Qc02DOojJLJsDxN2WH2s0RdJTWLByRvIAa5zNkXzkWfIPAB5OGjJeHeIumNj3vqz78fhpTMh8vHm4Mc2ex0e9szlUO97hXzEFGGtKoGpyxNuqCnIuMOc4t9HFv7B7NTzUY9O5aXgkBbRiSW1APm5F6sP30GyHWrNEwVfDKICSEUbRXCLlCeXXLQYoOlyaLqnEeaAKGujukYiDMLK685maJEc7S5m8KcoBGUf2', '张三', 'VwkmB6BhKU0=');

-- ----------------------------
-- Table structure for bulletin
-- ----------------------------
DROP TABLE IF EXISTS `bulletin`;
CREATE TABLE `bulletin`  (
  `id` bigint(18) NOT NULL,
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `date` datetime(0) NULL DEFAULT NULL,
  `days` int(6) NULL DEFAULT NULL,
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `creator` bigint(18) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of bulletin
-- ----------------------------
INSERT INTO `bulletin` VALUES (797984984185966592, '第一个公告', '2019-03-07 19:23:07', '2019-03-07 19:30:06', 13, '2', 719342979826847744);
INSERT INTO `bulletin` VALUES (797987847469535232, '第二个公告', '2019-03-07 19:34:30', '2019-03-07 00:00:00', 1, '2', 719342979826847744);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `creator` bigint(20) NULL DEFAULT NULL,
  `key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `level` int(11) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (718001869099634688, '2018-07-30 16:18:47', 719342979826847745, 'system', 1, '系统管理', NULL);
INSERT INTO `menu` VALUES (718001942487371776, '2018-07-30 16:19:04', 719342979826847745, 'system-user', 2, '用户管理', 718001869099634688);
INSERT INTO `menu` VALUES (718002643397513216, '2018-07-30 16:21:51', 719342979826847745, 'system-role', 2, '角色管理', 718001869099634688);
INSERT INTO `menu` VALUES (718003843480162304, '2018-07-30 16:26:37', 719342979826847744, 'system-menu', 2, '菜单管理', 718001869099634688);
INSERT INTO `menu` VALUES (718635136832245760, '2018-08-01 10:15:10', 719342979826847744, 'demo', 1, 'Demo', NULL);
INSERT INTO `menu` VALUES (718989957087105024, '2018-08-02 09:45:05', 719342979826847744, 'log', 1, '日志', NULL);
INSERT INTO `menu` VALUES (756396238332497920, '2018-11-13 01:04:18', 719342979826847744, 'bulletin', 1, '公告管理', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `creator` bigint(20) NULL DEFAULT NULL,
  `describe` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (716817127041929217, '2019-01-17 10:17:51', 719342979826847744, NULL, '管理员');
INSERT INTO `role` VALUES (717974409125564416, '2019-01-17 10:17:51', 719342979826847745, 'asdfasdfasdfdfsfasdfsadfasdfas', '普通管理员1');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` bigint(18) NOT NULL,
  `role` bigint(18) NULL DEFAULT NULL,
  `menu` bigint(18) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (790019039886839808, 717974409125564416, 756396238332497920);
INSERT INTO `role_menu` VALUES (790019039886839809, 717974409125564416, 718989957087105024);
INSERT INTO `role_menu` VALUES (790019039886839810, 717974409125564416, 718001869099634688);
INSERT INTO `role_menu` VALUES (790019039886839811, 717974409125564416, 718003843480162304);
INSERT INTO `role_menu` VALUES (790019039886839812, 717974409125564416, 718002643397513216);
INSERT INTO `role_menu` VALUES (790019039886839813, 717974409125564416, 718001942487371776);
INSERT INTO `role_menu` VALUES (809662043241058304, 716817127041929217, 756396238332497920);
INSERT INTO `role_menu` VALUES (809662043241058305, 716817127041929217, 718989957087105024);
INSERT INTO `role_menu` VALUES (809662043241058306, 716817127041929217, 718001869099634688);
INSERT INTO `role_menu` VALUES (809662043241058307, 716817127041929217, 718003843480162304);
INSERT INTO `role_menu` VALUES (809662043241058308, 716817127041929217, 718002643397513216);
INSERT INTO `role_menu` VALUES (809662043241058309, 716817127041929217, 718001942487371776);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) NOT NULL,
  `role` bigint(20) NULL DEFAULT NULL,
  `user` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 716817127041929217, 719342979826847744);
INSERT INTO `user_role` VALUES (810037515896492032, 717974409125564416, 719342979826847745);

SET FOREIGN_KEY_CHECKS = 1;
