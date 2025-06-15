package org.example.coursesystem.service;

import org.example.coursesystem.entity.Course;
import org.example.coursesystem.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程服务类
 */
@Service
@Transactional
public class CourseService {
    
    @Autowired
    private CourseMapper courseMapper;
    
    /**
     * 查询所有课程
     */
    public List<Course> findAll() {
        return courseMapper.findAll();
    }
    
    /**
     * 根据ID查询课程
     */
    public Course findById(Long id) {
        return courseMapper.findById(id);
    }
    
    /**
     * 根据课程代码查询课程
     */
    public Course findByCourseCode(String courseCode) {
        return courseMapper.findByCourseCode(courseCode);
    }
    
    /**
     * 根据课程名称模糊查询
     */
    public List<Course> findByCourseName(String courseName) {
        return courseMapper.findByCourseName(courseName);
    }
    
    /**
     * 根据课程类型查询
     */
    public List<Course> findByCourseType(String courseType) {
        return courseMapper.findByCourseType(courseType);
    }
    
    /**
     * 根据开课院系查询
     */
    public List<Course> findByDepartment(String department) {
        return courseMapper.findByDepartment(department);
    }
    
    /**
     * 根据授课教师查询
     */
    public List<Course> findByTeacherName(String teacherName) {
        return courseMapper.findByTeacherName(teacherName);
    }
    
    /**
     * 根据状态查询
     */
    public List<Course> findByStatus(String status) {
        return courseMapper.findByStatus(status);
    }
    
    /**
     * 关键词搜索
     */
    public List<Course> searchByKeyword(String keyword) {
        return courseMapper.searchByKeyword(keyword);
    }
    
    /**
     * 查询可选课程
     */
    public List<Course> findAvailableCourses(Long semesterId, Long studentId) {
        return courseMapper.findAvailableCourses(semesterId, studentId);
    }
    
    /**
     * 查询学生已选课程
     */
    public List<Course> findSelectedCoursesByStudent(Long studentId, Long semesterId) {
        return courseMapper.findSelectedCoursesByStudent(studentId, semesterId);
    }
    
    /**
     * 添加课程
     */
    public boolean save(Course course) {
        try {
            if (course.getId() == null) {
                // 新增
                course.setCreatedTime(LocalDateTime.now());
                course.setUpdatedTime(LocalDateTime.now());
                return courseMapper.insert(course) > 0;
            } else {
                // 更新
                course.setUpdatedTime(LocalDateTime.now());
                return courseMapper.update(course) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 更新课程
     */
    public boolean update(Course course) {
        try {
            course.setUpdatedTime(LocalDateTime.now());
            return courseMapper.update(course) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除课程
     */
    public boolean deleteById(Long id) {
        try {
            return courseMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 批量删除课程
     */
    public boolean deleteByIds(List<Long> ids) {
        try {
            return courseMapper.deleteByIds(ids) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查课程代码是否存在
     */
    public boolean existsByCourseCode(String courseCode, Long excludeId) {
        return courseMapper.existsByCourseCode(courseCode, excludeId);
    }
    
    /**
     * 统计课程总数
     */
    public long count() {
        return courseMapper.count();
    }
    
    /**
     * 根据条件统计课程数量
     */
    public long countByCondition(String courseType, String department, String status) {
        return courseMapper.countByCondition(courseType, department, status);
    }
    
    /**
     * 验证课程数据
     */
    public String validateCourse(Course course) {
        if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            return "课程代码不能为空";
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            return "课程名称不能为空";
        }
        if (course.getCredits() == null || course.getCredits().doubleValue() <= 0) {
            return "学分必须大于0";
        }
        if (course.getHours() == null || course.getHours() <= 0) {
            return "学时必须大于0";
        }
        if (course.getMaxStudents() == null || course.getMaxStudents() <= 0) {
            return "最大选课人数必须大于0";
        }
        
        // 检查课程代码是否重复
        if (existsByCourseCode(course.getCourseCode(), course.getId())) {
            return "课程代码已存在";
        }
        
        return null; // 验证通过
    }
}