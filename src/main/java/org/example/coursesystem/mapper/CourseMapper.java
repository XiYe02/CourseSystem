package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.Course;

import java.util.List;

/**
 * 课程数据访问层接口
 */
@Mapper
public interface CourseMapper {
    
    /**
     * 查询所有课程
     */
    List<Course> findAll();
    
    /**
     * 根据ID查询课程
     */
    Course findById(Long id);
    
    /**
     * 根据课程代码查询课程
     */
    Course findByCourseCode(String courseCode);
    
    /**
     * 根据课程名称模糊查询
     */
    List<Course> findByCourseName(String courseName);
    
    /**
     * 根据课程类型查询
     */
    List<Course> findByCourseType(String courseType);
    
    /**
     * 根据开课院系查询
     */
    List<Course> findByDepartment(String department);
    
    /**
     * 根据授课教师查询
     */
    List<Course> findByTeacherName(String teacherName);
    
    /**
     * 根据状态查询
     */
    List<Course> findByStatus(String status);
    
    /**
     * 关键词搜索（课程代码、课程名称、教师姓名）
     */
    List<Course> searchByKeyword(String keyword);
    
    /**
     * 查询可选课程（状态为ACTIVE且有剩余名额）
     */
    List<Course> findAvailableCourses(@Param("semesterId") Long semesterId, @Param("studentId") Long studentId);
    
    /**
     * 查询学生已选课程
     */
    List<Course> findSelectedCoursesByStudent(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 插入课程
     */
    int insert(Course course);
    
    /**
     * 更新课程
     */
    int update(Course course);
    
    /**
     * 删除课程
     */
    int deleteById(Long id);
    
    /**
     * 批量删除课程
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 检查课程代码是否存在
     */
    boolean existsByCourseCode(@Param("courseCode") String courseCode, @Param("excludeId") Long excludeId);
    
    /**
     * 统计课程总数
     */
    long count();
    
    /**
     * 根据条件统计课程数量
     */
    long countByCondition(@Param("courseType") String courseType, @Param("department") String department, @Param("status") String status);
}