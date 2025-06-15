package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.CourseSelection;

import java.util.List;
import java.util.Map;

/**
 * 选课信息数据访问层接口
 */
@Mapper
public interface CourseSelectionMapper {
    
    /**
     * 查询所有选课记录
     */
    List<CourseSelection> findAll();
    
    /**
     * 根据ID查询选课记录
     */
    CourseSelection findById(Long id);
    
    /**
     * 根据学生ID查询选课记录
     */
    List<CourseSelection> findByStudentId(Long studentId);
    
    /**
     * 根据课程ID查询选课记录
     */
    List<CourseSelection> findByCourseId(Long courseId);
    
    /**
     * 根据学期ID查询选课记录
     */
    List<CourseSelection> findBySemesterId(Long semesterId);
    
    /**
     * 根据学生ID和学期ID查询选课记录
     */
    List<CourseSelection> findByStudentAndSemester(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 根据课程ID和学期ID查询选课记录
     */
    List<CourseSelection> findByCourseAndSemester(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 查询学生在指定学期的选课记录（包含课程和学期信息）
     */
    List<CourseSelection> findStudentCoursesWithDetails(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 查询课程在指定学期的选课记录（包含学生信息）
     */
    List<CourseSelection> findCourseStudentsWithDetails(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 查询所有选课记录（包含学生、课程、学期信息）
     */
    List<CourseSelection> findAllWithDetails();
    
    /**
     * 根据状态查询选课记录
     */
    List<CourseSelection> findByStatus(String status);
    
    /**
     * 根据状态查询选课记录（包含学生、课程、学期信息）
     */
    List<CourseSelection> findByStatusWithDetails(String status);
    
    /**
     * 根据学生关键词查询选课记录（包含学生、课程、学期信息）
     */
    List<CourseSelection> findByStudentKeywordWithDetails(@Param("keyword") String keyword);
    
    /**
     * 根据课程关键词查询选课记录（包含学生、课程、学期信息）
     */
    List<CourseSelection> findByCourseKeywordWithDetails(@Param("keyword") String keyword);
    
    /**
     * 根据学生ID查询选课记录（包含课程、学期信息）
     */
    List<CourseSelection> findByStudentIdWithDetails(Long studentId);
    
    /**
     * 根据课程ID查询选课记录（包含学生、学期信息）
     */
    List<CourseSelection> findByCourseIdWithDetails(Long courseId);
    
    /**
     * 根据学期ID查询选课记录（包含学生、课程信息）
     */
    List<CourseSelection> findBySemesterIdWithDetails(Long semesterId);
    
    /**
     * 查询学生选课汇总信息
     */
    Map<String, Object> findStudentCourseSummary(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 查询学生选课汇总信息（所有学期）
     */
    Map<String, Object> findStudentCourseSummaryAll(@Param("studentId") Long studentId);
    
    /**
     * 插入选课记录
     */
    int insert(CourseSelection courseSelection);
    
    /**
     * 更新选课记录
     */
    int update(CourseSelection courseSelection);
    
    /**
     * 删除选课记录
     */
    int deleteById(Long id);
    
    /**
     * 批量删除选课记录
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 根据学生ID删除选课记录
     */
    int deleteByStudentId(Long studentId);
    
    /**
     * 根据课程ID删除选课记录
     */
    int deleteByCourseId(Long courseId);
    
    /**
     * 检查学生是否已选择指定课程
     */
    boolean existsByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 检查学生是否已选择指定课程（Service中使用的方法名）
     */
    boolean isStudentSelectedCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 统计学生在指定学期的选课数量
     */
    long countByStudentAndSemester(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 统计学生选课数量（Service中使用的方法名）
     */
    long countStudentSelections(@Param("studentId") Long studentId, @Param("semesterId") Long semesterId);
    
    /**
     * 统计课程在指定学期的选课人数
     */
    long countByCourseAndSemester(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 统计课程选课人数（Service中使用的方法名）
     */
    long countCourseSelections(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    /**
     * 统计选课总数
     */
    long count();
    
    /**
     * 根据条件统计选课数量
     */
    long countByCondition(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semesterId") Long semesterId, @Param("status") String status);
}