/*
 Navicat Premium Data Transfer

 Source Server         : MySQL Localhost
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : am-server

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 18/08/2020 20:37:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS admin_user;
CREATE TABLE admin_user  (
  id bigint(0) NOT NULL COMMENT '主键',
  username varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  avatar bigint(0) NULL DEFAULT NULL COMMENT '头像',
  email varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  gender tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  salt varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盐',
  name varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  password varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  created_by bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  revision bigint(0) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户  ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO admin_user VALUES (940823560740409344, 'admin', 1266942450736435200, 'admin@am.com', 0, 'iiPfy5J/nhOGL/T7CDTpRoNioUrguqH0', 'admin', 'xtnsaE1hdTA=', NULL, NULL, '940823560740409344', '2020-08-18 12:22:50', 55);

-- ----------------------------
-- Table structure for bulletin
-- ----------------------------
DROP TABLE IF EXISTS bulletin;
CREATE TABLE bulletin  (
  id bigint(0) NOT NULL COMMENT '主键',
  title varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  content varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  date datetime(0) NULL DEFAULT NULL COMMENT '发布日期',
  days int(0) NULL DEFAULT NULL COMMENT '发布天数',
  status tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  created_by bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  revision bigint(0) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告  ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bulletin
-- ----------------------------

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS log;
CREATE TABLE log  (
  id bigint(0) NOT NULL COMMENT '主键',
  name varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人',
  menu varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单',
  operate varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作',
  ip varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ip地址',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  created_by bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  revision bigint(0) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS role;
CREATE TABLE role  (
  id bigint(0) NOT NULL COMMENT '主键',
  memo varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  name varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  created_by bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  revision bigint(0) NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色  ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO role VALUES (940823091829805056, '超级管理员', '超级管理员', NULL, '2020-04-05 05:10:47', '940823560740409344', '2020-06-09 13:46:01', 26);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS role_permission;
CREATE TABLE role_permission  (
  role bigint(0) NULL DEFAULT NULL COMMENT '角色主键',
  permission varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO role_permission VALUES (940823091829805056, 'system');
INSERT INTO role_permission VALUES (940823091829805056, 'system-user');
INSERT INTO role_permission VALUES (940823091829805056, 'system-user-add');
INSERT INTO role_permission VALUES (940823091829805056, 'system-user-list');
INSERT INTO role_permission VALUES (940823091829805056, 'system-user-update');
INSERT INTO role_permission VALUES (940823091829805056, 'system-role');
INSERT INTO role_permission VALUES (940823091829805056, 'system-scheduled-task');
INSERT INTO role_permission VALUES (940823091829805056, 'log');
INSERT INTO role_permission VALUES (940823091829805056, 'log-operate');
INSERT INTO role_permission VALUES (940823091829805056, 'bulletin');

-- ----------------------------
-- Table structure for scheduled_task
-- ----------------------------
DROP TABLE IF EXISTS scheduled_task;
CREATE TABLE scheduled_task  (
  id bigint(0) NOT NULL COMMENT '主键',
  name varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  corn varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  bean varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实现标识',
  status varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  memo varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  latest_date datetime(0) NULL DEFAULT NULL COMMENT '上次执行日期',
  execute_status varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上次执行状态',
  time_consuming bigint(0) NULL DEFAULT NULL COMMENT '执行耗时(秒)',
  revision int(0) NULL DEFAULT NULL COMMENT '乐观锁',
  created_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduled_task
-- ----------------------------
INSERT INTO scheduled_task VALUES (948915072833425408, 'test', '0/3 * * * * ? ', 'TestScheduledTask', '1', 'asdfasdfas', '2020-05-17 11:40:32', '0', 0, 1019, '940823560740409344', '2020-04-27 21:51:18', '940823560740409344', '2020-05-17 11:14:45');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS sys_file;
CREATE TABLE sys_file  (
  id bigint(0) NOT NULL COMMENT '主键',
  type varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  url varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网络请求路径',
  dir varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '本地资源访问路径',
  revision bigint(0) NULL DEFAULT NULL COMMENT '乐观锁',
  created_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  created_time datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  updated_by varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  updated_time datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role  (
  role bigint(0) NULL DEFAULT NULL COMMENT '角色主键',
  user bigint(0) NULL DEFAULT NULL COMMENT '用户主键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表  ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO user_role VALUES (940823091829805056, 940823560740409344);

SET FOREIGN_KEY_CHECKS = 1;
