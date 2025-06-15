/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : course_system

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 14/06/2025 23:22:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `course_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程代码',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程名称',
  `credits` decimal(3, 1) NOT NULL COMMENT '学分',
  `hours` int NOT NULL COMMENT '学时',
  `course_type` enum('REQUIRED','ELECTIVE','PUBLIC') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '课程类型',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开课院系',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '授课教师',
  `teacher_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教师职称',
  `max_students` int NULL DEFAULT 100 COMMENT '最大选课人数',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '课程描述',
  `prerequisites` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '先修课程',
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '课程状态',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_code`(`course_code` ASC) USING BTREE,
  INDEX `idx_course_code`(`course_code` ASC) USING BTREE,
  INDEX `idx_course_type`(`course_type` ASC) USING BTREE,
  INDEX `idx_department`(`department` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 239 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '课程信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'CS101', '数据结构与算法', 4.0, 64, 'REQUIRED', '计算机学院', '张教授', '教授', 120, '介绍基本的数据结构和算法设计与分析', NULL, 'ACTIVE', '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `course` VALUES (2, 'CS102', '操作系统原理', 3.5, 56, 'REQUIRED', '计算机学院', '李教授', '副教授', 100, '操作系统的基本概念、原理和实现技术', NULL, 'ACTIVE', '2024-06-13 20:51:43', '2024-07-14 13:17:43');
INSERT INTO `course` VALUES (3, 'CS103', '数据库系统原理', 3.0, 48, 'REQUIRED', '计算机学院', '王教授', '教授', 80, '数据库系统的基本理论和应用技术', NULL, 'ACTIVE', '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `course` VALUES (4, 'CS201', 'Java程序设计', 3.0, 48, 'ELECTIVE', '计算机学院', '陈教授', '副教授', 60, 'Java语言程序设计基础与应用', NULL, 'ACTIVE', '2024-06-13 20:51:43', '2024-06-30 20:51:43');
INSERT INTO `course` VALUES (89, 'CS105', '软件测试', 4.0, 64, 'REQUIRED', '人工智能学院', '刘翔', '副教授', 100, '', '', 'ACTIVE', '2025-06-14 00:22:30', '2025-06-14 12:56:37');
INSERT INTO `course` VALUES (115, 'CS206', '设计模式', 4.0, 48, 'REQUIRED', '人工智能学院', '张泽明', '副教授', 100, '', '', 'ACTIVE', '2025-06-14 13:41:40', '2025-06-14 13:41:40');
INSERT INTO `course` VALUES (116, 'CS208', '中国近代史', 2.0, 16, 'PUBLIC', '应用技术学院', '王英梅', '讲师', 100, '', '', 'ACTIVE', '2025-06-14 13:42:59', '2025-06-14 13:42:59');
INSERT INTO `course` VALUES (117, 'CS108', '马克思主义原理', 2.0, 32, 'PUBLIC', '马克思学院', '陈化光', '副教授', 100, '', '', 'ACTIVE', '2025-06-14 13:44:13', '2025-06-14 13:44:13');
INSERT INTO `course` VALUES (118, 'CS205', 'python', 4.0, 64, 'REQUIRED', '人工智能学院', '赵理鸣', '副教授', 100, '', '', 'ACTIVE', '2025-06-14 13:46:43', '2025-06-14 13:46:43');

-- ----------------------------
-- Table structure for course_selection
-- ----------------------------
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '选课ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `semester_id` bigint NOT NULL COMMENT '学期ID',
  `selection_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `status` enum('SELECTED','DROPPED','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'SELECTED' COMMENT '选课状态',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '成绩',
  `grade_point` decimal(3, 2) NULL DEFAULT NULL COMMENT '绩点',
  `exam_time` timestamp NULL DEFAULT NULL COMMENT '考试时间',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_course_semester`(`student_id` ASC, `course_id` ASC, `semester_id` ASC) USING BTREE,
  INDEX `semester_id`(`semester_id` ASC) USING BTREE,
  INDEX `idx_student_semester`(`student_id` ASC, `semester_id` ASC) USING BTREE,
  INDEX `idx_course_semester`(`course_id` ASC, `semester_id` ASC) USING BTREE,
  CONSTRAINT `course_selection_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `course_selection_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `course_selection_ibfk_3` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 382 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '选课信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_selection
-- ----------------------------
INSERT INTO `course_selection` VALUES (1, 1, 1, 3, '2025-06-13 20:51:43', 'COMPLETED', 85.50, NULL, NULL, NULL, '2025-06-13 20:51:43', '2025-06-14 18:26:56');
INSERT INTO `course_selection` VALUES (4, 2, 1, 1, '2025-06-13 20:51:43', 'COMPLETED', 78.00, NULL, NULL, NULL, '2025-06-13 20:51:43', '2025-06-14 18:29:13');
INSERT INTO `course_selection` VALUES (5, 2, 3, 1, '2025-06-13 20:51:43', 'SELECTED', 78.00, NULL, NULL, NULL, '2025-06-13 20:51:43', '2025-06-14 18:29:29');
INSERT INTO `course_selection` VALUES (6, 3, 4, 2, '2025-06-13 20:51:43', 'SELECTED', 88.50, NULL, NULL, NULL, '2025-06-13 20:51:43', '2025-06-14 11:44:49');
INSERT INTO `course_selection` VALUES (380, 1, 2, 1, '2025-06-14 22:57:41', 'SELECTED', NULL, NULL, NULL, NULL, '2025-06-14 22:57:41', '2025-06-14 22:57:41');
INSERT INTO `course_selection` VALUES (381, 1, 115, 1, '2025-06-14 22:57:48', 'SELECTED', NULL, NULL, NULL, NULL, '2025-06-14 22:57:48', '2025-06-14 22:57:48');

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作用户名',
  `operation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型：CREATE, UPDATE, DELETE, SELECT, LOGIN, LOGOUT',
  `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作模块：USER, COURSE, ENROLLMENT等',
  `operation_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '操作描述',
  `target_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标类型',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标ID',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法：GET, POST, PUT, DELETE',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `execution_time` bigint NULL DEFAULT NULL COMMENT '执行时间（毫秒）',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUCCESS' COMMENT '操作状态：SUCCESS, FAILURE',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_operation_type`(`operation_type` ASC) USING BTREE,
  INDEX `idx_operation_module`(`operation_module` ASC) USING BTREE,
  INDEX `idx_operation_time`(`operation_time` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (1, 1, 'admin', 'LOGIN', 'USER', '管理员登录系统', 'USER', 1, 'POST', '/login', '127.0.0.1', '2025-06-14 23:20:15', 150, 'SUCCESS', NULL, '2025-06-14 23:20:15', '2025-06-14 23:20:15');
INSERT INTO `operation_log` VALUES (2, 1, 'admin', 'CREATE', 'COURSE', '创建新课程', 'COURSE', 1, 'POST', '/admin/courses', '127.0.0.1', '2025-06-14 23:20:15', 200, 'SUCCESS', NULL, '2025-06-14 23:20:15', '2025-06-14 23:20:15');
INSERT INTO `operation_log` VALUES (3, 2, 'teacher1', 'LOGIN', 'USER', '教师登录系统', 'USER', 2, 'POST', '/login', '192.168.1.100', '2025-06-14 23:20:15', 120, 'SUCCESS', NULL, '2025-06-14 23:20:15', '2025-06-14 23:20:15');
INSERT INTO `operation_log` VALUES (4, 3, 'student1', 'CREATE', 'ENROLLMENT', '学生选课', 'ENROLLMENT', 1, 'POST', '/student/enroll', '192.168.1.101', '2025-06-14 23:20:15', 180, 'SUCCESS', NULL, '2025-06-14 23:20:15', '2025-06-14 23:20:15');
INSERT INTO `operation_log` VALUES (5, 3, 'student1', 'SELECT', 'COURSE', '查看课程列表', 'COURSE', NULL, 'GET', '/student/courses', '192.168.1.101', '2025-06-14 23:20:15', 50, 'SUCCESS', NULL, '2025-06-14 23:20:15', '2025-06-14 23:20:15');

-- ----------------------------
-- Table structure for semester
-- ----------------------------
DROP TABLE IF EXISTS `semester`;
CREATE TABLE `semester`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学期ID',
  `semester_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学期代码',
  `semester_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学期名称',
  `academic_year` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学年',
  `semester_type` enum('SPRING','SUMMER','AUTUMN','WINTER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学期类型',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `registration_start_date` date NULL DEFAULT NULL COMMENT '选课开始日期',
  `registration_end_date` date NULL DEFAULT NULL COMMENT '选课结束日期',
  `status` enum('UPCOMING','CURRENT','FINISHED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'UPCOMING' COMMENT '学期状态',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `semester_code`(`semester_code` ASC) USING BTREE,
  INDEX `idx_semester_code`(`semester_code` ASC) USING BTREE,
  INDEX `idx_academic_year`(`academic_year` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学期信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of semester
-- ----------------------------
INSERT INTO `semester` VALUES (1, '2024-2025-1', '2024-2025学年第一学期', '2024-2025', 'AUTUMN', '2024-09-01', '2025-06-30', '2025-06-01', '2025-12-31', 'CURRENT', '2025-06-13 20:51:43', '2025-06-14 18:53:34');
INSERT INTO `semester` VALUES (2, '2024-2025-2', '2024-2025学年第二学期', '2024-2025', 'SPRING', '2025-02-20', '2025-12-01', '2025-06-01', '2025-12-31', 'UPCOMING', '2025-06-13 20:51:43', '2025-06-14 18:53:24');
INSERT INTO `semester` VALUES (3, '2025-2026-1', '2025-2026学年第一学期', '2025-2026', 'AUTUMN', '2025-09-01', '2026-01-15', '2025-06-01', '2025-12-31', 'UPCOMING', '2025-06-14 12:00:20', '2025-06-14 12:00:20');
INSERT INTO `semester` VALUES (4, '2025-2026-2', '2025-2026学年第二学期', '2025-2026', 'SPRING', '2026-02-20', '2026-06-30', '2025-06-01', '2025-12-31', 'UPCOMING', '2025-06-14 12:00:20', '2025-06-14 12:00:20');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
  `student_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '姓名',
  `gender` enum('MALE','FEMALE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业',
  `grade` int NULL DEFAULT NULL COMMENT '年级',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级',
  `enrollment_date` date NULL DEFAULT NULL COMMENT '入学日期',
  `status` enum('ACTIVE','INACTIVE','GRADUATED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '学生状态',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_number`(`student_number` ASC) USING BTREE,
  INDEX `idx_student_number`(`student_number` ASC) USING BTREE,
  INDEX `idx_major_grade`(`major` ASC, `grade` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '2021001001', '张三', 'MALE', NULL, '13800138001', 'zhangsan@example.com', '计算机科学与技术', 2021, '计科2101', NULL, 'ACTIVE', '2025-06-13 20:51:43', '2025-06-13 22:08:04');
INSERT INTO `student` VALUES (2, '2021001002', '李四', 'FEMALE', '2003-08-20', '13800138002', 'lisi@example.com', '计算机科学与技术', 2021, '计科2101', '2021-09-01', 'ACTIVE', '2024-12-01 20:51:43', '2025-06-14 13:05:16');
INSERT INTO `student` VALUES (3, '2022001001', '王五', 'MALE', '2004-03-10', '13800138003', 'wangwu@example.com', '软件工程', 2022, '软工2201', '2022-09-01', 'ACTIVE', '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `student` VALUES (4, '2024001004', '小谢', 'FEMALE', NULL, '18789812007', '2307480483@qq.com', '物联网', 2024, '', NULL, 'ACTIVE', '2024-11-13 22:07:42', '2025-06-14 13:06:00');
INSERT INTO `student` VALUES (92, 'test', 'Xiye', 'FEMALE', NULL, '18789812007', '1321153456454@qq.com', '计算机科学与技术', 2024, '计科2401', NULL, 'ACTIVE', '2025-06-14 14:29:36', '2025-06-14 14:29:36');
INSERT INTO `student` VALUES (93, '2022001002', '赵六', 'MALE', NULL, NULL, 'zhaoliu@example.com', '软件工程', 2022, '软工2202', NULL, 'ACTIVE', '2025-06-14 14:29:36', '2025-06-14 14:29:36');

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '配置值',
  `config_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置描述',
  `config_type` enum('STRING','NUMBER','BOOLEAN','JSON') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'STRING' COMMENT '配置类型',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统配置',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_key`(`config_key` ASC) USING BTREE,
  INDEX `idx_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 277 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, 'system.name', '学生选课信息管理系统', '系统名称', 'STRING', 1, '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `system_config` VALUES (2, 'system.version', '1.0.0', '系统版本', 'STRING', 1, '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `system_config` VALUES (3, 'course.max_selections_per_semester', '8', '每学期最大选课数量', 'NUMBER', 0, '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `system_config` VALUES (4, 'course.min_credits_per_semester', '12', '每学期最少学分要求', 'NUMBER', 0, '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `system_config` VALUES (5, 'course.max_credits_per_semester', '25', '每学期最多学分限制', 'NUMBER', 0, '2025-06-13 20:51:43', '2025-06-13 20:51:43');
INSERT INTO `system_config` VALUES (6, 'system.maintenance_mode', 'false', '系统维护模式', 'BOOLEAN', 0, '2025-06-13 20:51:43', '2025-06-13 20:51:43');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密后）',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `role` enum('ADMIN','TEACHER','STUDENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户角色',
  `student_id` bigint NULL DEFAULT NULL COMMENT '关联学生ID（如果是学生用户）',
  `status` enum('ACTIVE','INACTIVE','LOCKED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'ACTIVE' COMMENT '用户状态',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_count` int NULL DEFAULT 0 COMMENT '登录次数',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_role`(`role` ASC) USING BTREE,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 278 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '系统管理员', 'admin@example.com', NULL, 'ADMIN', NULL, 'ACTIVE', '2025-06-14 22:58:07', 0, '2025-06-13 20:51:43', '2025-06-14 22:58:07');
INSERT INTO `user` VALUES (2, 'teacher001', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '张教授', 'zhang@example.com', NULL, 'TEACHER', NULL, 'ACTIVE', NULL, 0, '2025-06-13 20:51:43', '2025-06-13 22:13:23');
INSERT INTO `user` VALUES (3, '2021001001', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '张三', 'zhangsan@example.com', NULL, 'STUDENT', 1, 'ACTIVE', '2025-06-14 22:58:24', 0, '2025-06-13 20:51:43', '2025-06-14 22:58:24');
INSERT INTO `user` VALUES (4, '2021001002', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '李四', 'lisi@example.com', NULL, 'STUDENT', 2, 'ACTIVE', '2025-06-14 13:03:01', 0, '2025-06-13 20:51:43', '2025-06-14 13:03:00');
INSERT INTO `user` VALUES (5, '2022001001', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '王五', 'wangwu@example.com', NULL, 'STUDENT', 3, 'ACTIVE', NULL, 0, '2025-06-13 20:51:43', '2025-06-13 22:13:18');
INSERT INTO `user` VALUES (6, '2022001002', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', '赵六', 'zhaoliu@example.com', NULL, 'STUDENT', 93, 'ACTIVE', NULL, 0, '2025-06-13 20:51:43', '2025-06-14 14:29:36');
INSERT INTO `user` VALUES (73, 'test', '$2a$10$eBG/01bQ5DSAJ.McM0AV/.iFoSRNPBS0mElnZlBsycyJ2vfaPwGLy', 'Xiye', '1321153456454@qq.com', '18789812007', 'STUDENT', 92, 'ACTIVE', '2025-06-14 13:02:21', 0, '2025-06-13 21:27:01', '2025-06-14 14:29:36');

-- ----------------------------
-- View structure for v_course_selection_summary
-- ----------------------------
DROP VIEW IF EXISTS `v_course_selection_summary`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_course_selection_summary` AS select `c`.`id` AS `course_id`,`c`.`course_code` AS `course_code`,`c`.`course_name` AS `course_name`,`c`.`credits` AS `credits`,`c`.`teacher_name` AS `teacher_name`,`sem`.`semester_name` AS `semester_name`,count(`cs`.`id`) AS `enrolled_students`,`c`.`max_students` AS `max_students`,(`c`.`max_students` - count(`cs`.`id`)) AS `available_slots`,avg((case when (`cs`.`score` is not null) then `cs`.`score` else NULL end)) AS `average_score` from ((`course` `c` left join `course_selection` `cs` on(((`c`.`id` = `cs`.`course_id`) and (`cs`.`status` <> 'DROPPED')))) left join `semester` `sem` on((`cs`.`semester_id` = `sem`.`id`))) group by `c`.`id`,`c`.`course_code`,`c`.`course_name`,`c`.`credits`,`c`.`teacher_name`,`sem`.`id`,`sem`.`semester_name`;

-- ----------------------------
-- View structure for v_student_course_summary
-- ----------------------------
DROP VIEW IF EXISTS `v_student_course_summary`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_student_course_summary` AS select `s`.`id` AS `student_id`,`s`.`student_number` AS `student_number`,`s`.`name` AS `student_name`,`s`.`major` AS `major`,`s`.`grade` AS `grade`,`sem`.`semester_name` AS `semester_name`,count(`cs`.`id`) AS `total_courses`,sum((case when (`cs`.`status` = 'COMPLETED') then `c`.`credits` else 0 end)) AS `completed_credits`,sum(`c`.`credits`) AS `total_credits`,avg((case when (`cs`.`score` is not null) then `cs`.`score` else NULL end)) AS `average_score` from (((`student` `s` left join `course_selection` `cs` on((`s`.`id` = `cs`.`student_id`))) left join `course` `c` on((`cs`.`course_id` = `c`.`id`))) left join `semester` `sem` on((`cs`.`semester_id` = `sem`.`id`))) group by `s`.`id`,`s`.`student_number`,`s`.`name`,`s`.`major`,`s`.`grade`,`sem`.`id`,`sem`.`semester_name`;

SET FOREIGN_KEY_CHECKS = 1;
