package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.Student;

import java.util.List;

/**
 * 学生数据访问层接口
 */
@Mapper// 注解，表示这是一个MyBatis的Mapper接口
public interface StudentMapper {
    
    /**
     * 根据ID查找学生
     */
    Student findById(@Param("id") Long id);
    
    /**
     * 根据学号查找学生
     */
    Student findByStudentNumber(@Param("studentNumber") String studentNumber);
    
    /**
     * 查询所有学生
     */
    List<Student> findAll();
    
    /**
     * 根据专业查询学生
     */
    List<Student> findByMajor(@Param("major") String major);
    
    /**
     * 根据年级查询学生
     */
    List<Student> findByGrade(@Param("grade") Integer grade);
    
    /**
     * 根据状态查询学生
     */
    List<Student> findByStatus(@Param("status") String status);
    
    /**
     * 模糊查询学生
     */
    List<Student> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 插入新学生
     */
    int insert(Student student);
    
    /**
     * 更新学生信息
     */
    int update(Student student);
    
    /**
     * 根据ID删除学生
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 批量删除学生
     */
    int deleteByIds(@Param("ids") List<Long> ids);
    
    /**
     * 检查学号是否存在
     */
    boolean existsByStudentNumber(@Param("studentNumber") String studentNumber);
    
    /**
     * 根据状态统计学生数量
     */
    int countByStatus(@Param("status") String status);
}