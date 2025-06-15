package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.Semester;

import java.util.List;

/**
 * 学期数据访问层接口
 */
@Mapper
public interface SemesterMapper {
    
    /**
     * 查询所有学期
     */
    List<Semester> findAll();
    
    /**
     * 根据ID查询学期
     */
    Semester findById(Long id);
    
    /**
     * 根据学期代码查询学期
     */
    Semester findBySemesterCode(String semesterCode);
    
    /**
     * 根据学年查询学期
     */
    List<Semester> findByAcademicYear(String academicYear);
    
    /**
     * 根据学期类型查询
     */
    List<Semester> findBySemesterType(String semesterType);
    
    /**
     * 根据状态查询
     */
    List<Semester> findByStatus(String status);
    
    /**
     * 查询当前学期
     */
    Semester findCurrentSemester();
    
    /**
     * 查询可选课学期（选课时间内的学期）
     */
    List<Semester> findRegistrationAvailableSemesters();
    
    /**
     * 插入学期
     */
    int insert(Semester semester);
    
    /**
     * 更新学期
     */
    int update(Semester semester);
    
    /**
     * 删除学期
     */
    int deleteById(Long id);
    
    /**
     * 批量删除学期
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 检查学期代码是否存在
     */
    boolean existsBySemesterCode(@Param("semesterCode") String semesterCode, @Param("excludeId") Long excludeId);
    
    /**
     * 统计学期总数
     */
    long count();
}