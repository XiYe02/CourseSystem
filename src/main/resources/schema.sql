-- 学生选课信息管理系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS course_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE course_system;

-- 1. 学生信息表
CREATE TABLE IF NOT EXISTS student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '学生ID',
    student_number VARCHAR(20) UNIQUE NOT NULL COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('MALE', 'FEMALE') COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    major VARCHAR(100) COMMENT '专业',
    grade INT COMMENT '年级',
    class_name VARCHAR(50) COMMENT '班级',
    enrollment_date DATE COMMENT '入学日期',
    status ENUM('ACTIVE', 'INACTIVE', 'GRADUATED') DEFAULT 'ACTIVE' COMMENT '学生状态',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_student_number (student_number),
    INDEX idx_major_grade (major, grade)
) ENGINE=InnoDB COMMENT='学生信息表';

-- 2. 课程信息表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '课程ID',
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT '课程代码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credits DECIMAL(3,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '学时',
    course_type ENUM('REQUIRED', 'ELECTIVE', 'PUBLIC') NOT NULL COMMENT '课程类型',
    department VARCHAR(100) COMMENT '开课院系',
    teacher_name VARCHAR(50) COMMENT '授课教师',
    teacher_title VARCHAR(50) COMMENT '教师职称',
    max_students INT DEFAULT 100 COMMENT '最大选课人数',
    description TEXT COMMENT '课程描述',
    prerequisites VARCHAR(500) COMMENT '先修课程',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '课程状态',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_course_code (course_code),
    INDEX idx_course_type (course_type),
    INDEX idx_department (department)
) ENGINE=InnoDB COMMENT='课程信息表';

-- 3. 学期信息表
CREATE TABLE IF NOT EXISTS semester (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '学期ID',
    semester_code VARCHAR(20) UNIQUE NOT NULL COMMENT '学期代码',
    semester_name VARCHAR(50) NOT NULL COMMENT '学期名称',
    academic_year VARCHAR(20) NOT NULL COMMENT '学年',
    semester_type ENUM('SPRING', 'SUMMER', 'AUTUMN', 'WINTER') NOT NULL COMMENT '学期类型',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    registration_start_date DATE COMMENT '选课开始日期',
    registration_end_date DATE COMMENT '选课结束日期',
    status ENUM('UPCOMING', 'CURRENT', 'FINISHED') DEFAULT 'UPCOMING' COMMENT '学期状态',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_semester_code (semester_code),
    INDEX idx_academic_year (academic_year)
) ENGINE=InnoDB COMMENT='学期信息表';

-- 4. 选课信息表
CREATE TABLE IF NOT EXISTS course_selection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '选课ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    selection_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status ENUM('SELECTED', 'DROPPED', 'COMPLETED') DEFAULT 'SELECTED' COMMENT '选课状态',
    score DECIMAL(5,2) COMMENT '成绩',
    grade_point DECIMAL(3,2) COMMENT '绩点',
    exam_time TIMESTAMP COMMENT '考试时间',
    remarks TEXT COMMENT '备注',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    FOREIGN KEY (semester_id) REFERENCES semester(id) ON DELETE CASCADE,
    UNIQUE KEY uk_student_course_semester (student_id, course_id, semester_id),
    INDEX idx_student_semester (student_id, semester_id),
    INDEX idx_course_semester (course_id, semester_id)
) ENGINE=InnoDB COMMENT='选课信息表';

-- 5. 用户表（用于权限管理）
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL COMMENT '用户角色',
    student_id BIGINT COMMENT '关联学生ID（如果是学生用户）',
    status ENUM('ACTIVE', 'INACTIVE', 'LOCKED') DEFAULT 'ACTIVE' COMMENT '用户状态',
    last_login_time TIMESTAMP COMMENT '最后登录时间',
    login_count INT DEFAULT 0 COMMENT '登录次数',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE SET NULL,
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB COMMENT='用户表';

-- 6. 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation_type ENUM('CREATE', 'UPDATE', 'DELETE', 'SELECT', 'LOGIN', 'LOGOUT') NOT NULL COMMENT '操作类型',
    operation_module VARCHAR(50) NOT NULL COMMENT '操作模块',
    operation_description TEXT COMMENT '操作描述',
    target_type VARCHAR(50) COMMENT '操作对象类型',
    target_id BIGINT COMMENT '操作对象ID',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_result TEXT COMMENT '响应结果',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    execution_time BIGINT COMMENT '执行时间（毫秒）',
    status ENUM('SUCCESS', 'FAILURE', 'ERROR') DEFAULT 'SUCCESS' COMMENT '操作状态',
    error_message TEXT COMMENT '错误信息',
    operation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_operation_time (operation_time),
    INDEX idx_operation_module (operation_module)
) ENGINE=InnoDB COMMENT='操作日志表';

-- 7. 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_description VARCHAR(500) COMMENT '配置描述',
    config_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') DEFAULT 'STRING' COMMENT '配置类型',
    is_system BOOLEAN DEFAULT FALSE COMMENT '是否系统配置',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB COMMENT='系统配置表';

-- 插入初始数据

-- 插入学期数据
INSERT INTO semester (semester_code, semester_name, academic_year, semester_type, start_date, end_date, registration_start_date, registration_end_date, status) VALUES
('2024-2025-1', '2024-2025学年第一学期', '2024-2025', 'AUTUMN', '2024-09-01', '2025-01-15', '2024-08-20', '2024-09-10', 'CURRENT'),
('2024-2025-2', '2024-2025学年第二学期', '2024-2025', 'SPRING', '2025-02-20', '2025-06-30', '2025-02-01', '2025-02-28', 'UPCOMING');

-- 插入学生数据
INSERT INTO student (student_number, name, gender, birth_date, phone, email, major, grade, class_name, enrollment_date) VALUES
('2021001001', '张三', 'MALE', '2003-05-15', '13800138001', 'zhangsan@example.com', '计算机科学与技术', 2021, '计科2101', '2021-09-01'),
('2021001002', '李四', 'FEMALE', '2003-08-20', '13800138002', 'lisi@example.com', '计算机科学与技术', 2021, '计科2101', '2021-09-01'),
('2022001001', '王五', 'MALE', '2004-03-10', '13800138003', 'wangwu@example.com', '软件工程', 2022, '软工2201', '2022-09-01'),
('2022001002', '赵六', 'FEMALE', '2004-07-25', '13800138004', 'zhaoliu@example.com', '软件工程', 2022, '软工2201', '2022-09-01');

-- 插入课程数据
INSERT INTO course (course_code, course_name, credits, hours, course_type, department, teacher_name, teacher_title, max_students, description) VALUES
('CS101', '数据结构与算法', 4.0, 64, 'REQUIRED', '计算机学院', '张教授', '教授', 120, '介绍基本的数据结构和算法设计与分析'),
('CS102', '操作系统原理', 3.5, 56, 'REQUIRED', '计算机学院', '李教授', '副教授', 100, '操作系统的基本概念、原理和实现技术'),
('CS103', '数据库系统原理', 3.0, 48, 'REQUIRED', '计算机学院', '王教授', '教授', 80, '数据库系统的基本理论和应用技术'),
('CS201', 'Java程序设计', 3.0, 48, 'ELECTIVE', '计算机学院', '陈教授', '副教授', 60, 'Java语言程序设计基础与应用'),
('MATH101', '高等数学A', 5.0, 80, 'REQUIRED', '数学学院', '刘教授', '教授', 200, '微积分基础理论与应用');

-- 插入用户数据（密码需要在应用中加密）
INSERT INTO user (username, password, real_name, email, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', 'admin@example.com', 'ADMIN', 'ACTIVE'),
('teacher001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '张教授', 'zhang@example.com', 'TEACHER', 'ACTIVE');

-- 为学生创建用户账号
INSERT INTO user (username, password, real_name, email, role, student_id, status) VALUES
('2021001001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '张三', 'zhangsan@example.com', 'STUDENT', 1, 'ACTIVE'),
('2021001002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '李四', 'lisi@example.com', 'STUDENT', 2, 'ACTIVE'),
('2022001001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '王五', 'wangwu@example.com', 'STUDENT', 3, 'ACTIVE'),
('2022001002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '赵六', 'zhaoliu@example.com', 'STUDENT', 4, 'ACTIVE');

-- 插入选课数据
INSERT INTO course_selection (student_id, course_id, semester_id, status, score) VALUES
(1, 1, 1, 'COMPLETED', 85.5),
(1, 2, 1, 'SELECTED', NULL),
(1, 5, 1, 'COMPLETED', 92.0),
(2, 1, 1, 'COMPLETED', 78.0),
(2, 3, 1, 'SELECTED', NULL),
(3, 4, 1, 'SELECTED', NULL),
(4, 1, 1, 'SELECTED', NULL),
(4, 4, 1, 'SELECTED', NULL);

-- 插入系统配置
INSERT INTO system_config (config_key, config_value, config_description, config_type, is_system) VALUES
('system.name', '学生选课信息管理系统', '系统名称', 'STRING', TRUE),
('system.version', '1.0.0', '系统版本', 'STRING', TRUE),
('course.max_selections_per_semester', '8', '每学期最大选课数量', 'NUMBER', FALSE),
('course.min_credits_per_semester', '12', '每学期最少学分要求', 'NUMBER', FALSE),
('course.max_credits_per_semester', '25', '每学期最多学分限制', 'NUMBER', FALSE),
('system.maintenance_mode', 'false', '系统维护模式', 'BOOLEAN', FALSE);

-- 创建视图：学生选课统计
CREATE VIEW v_student_course_summary AS
SELECT 
    s.id as student_id,
    s.student_number,
    s.name as student_name,
    s.major,
    s.grade,
    sem.semester_name,
    COUNT(cs.id) as total_courses,
    SUM(CASE WHEN cs.status = 'COMPLETED' THEN c.credits ELSE 0 END) as completed_credits,
    SUM(c.credits) as total_credits,
    AVG(CASE WHEN cs.score IS NOT NULL THEN cs.score ELSE NULL END) as average_score
FROM student s
LEFT JOIN course_selection cs ON s.id = cs.student_id
LEFT JOIN course c ON cs.course_id = c.id
LEFT JOIN semester sem ON cs.semester_id = sem.id
GROUP BY s.id, s.student_number, s.name, s.major, s.grade, sem.id, sem.semester_name;

-- 创建视图：课程选课统计
CREATE VIEW v_course_selection_summary AS
SELECT 
    c.id as course_id,
    c.course_code,
    c.course_name,
    c.credits,
    c.teacher_name,
    sem.semester_name,
    COUNT(cs.id) as enrolled_students,
    c.max_students,
    (c.max_students - COUNT(cs.id)) as available_slots,
    AVG(CASE WHEN cs.score IS NOT NULL THEN cs.score ELSE NULL END) as average_score
FROM course c
LEFT JOIN course_selection cs ON c.id = cs.course_id AND cs.status != 'DROPPED'
LEFT JOIN semester sem ON cs.semester_id = sem.id
GROUP BY c.id, c.course_code, c.course_name, c.credits, c.teacher_name, sem.id, sem.semester_name;