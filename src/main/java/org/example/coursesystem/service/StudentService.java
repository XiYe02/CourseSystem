package org.example.coursesystem.service;

import org.example.coursesystem.entity.Student;
import org.example.coursesystem.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生服务层
 */
@Service// 注解，表示这是一个服务层组件
@Transactional// 注解，表示该类中的方法都需要开启事务管理
public class StudentService {
    
    @Autowired// 注解，表示自动装配学生数据访问层接口的实现类
    private StudentMapper studentMapper;
    
    /**
     * 根据ID查找学生
     */
    @Cacheable(value = "students", key = "#id")// 注解，表示根据学生ID缓存学生对象
    public Student findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        return studentMapper.findById(id);// 注解，表示根据学生ID从数据库中查询学生对象
    }
    
    /**
     * 根据学号查找学生
     */
    @Cacheable(value = "students", key = "'number:' + #studentNumber")// 注解，表示根据学生学号缓存学生对象
    public Student findByStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        return studentMapper.findByStudentNumber(studentNumber.trim());// 注解，表示根据学生学号从数据库中查询学生对象
    }
    
    /**
     * 查询所有学生
     */
    @Cacheable(value = "students", key = "'all'")
    public List<Student> findAll() {
        return studentMapper.findAll();
    }
    
    /**
     * 根据专业查询学生
     */
    @Cacheable(value = "students", key = "'major:' + #major")
    public List<Student> findByMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            return findAll();
        }
        return studentMapper.findByMajor(major.trim());
    }
    
    /**
     * 根据年级查询学生
     */
    @Cacheable(value = "students", key = "'grade:' + #grade")
    public List<Student> findByGrade(Integer grade) {
        if (grade == null) {
            return findAll();
        }
        return studentMapper.findByGrade(grade);
    }
    
    /**
     * 根据状态查询学生
     */
    public List<Student> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return findAll();
        }
        return studentMapper.findByStatus(status.trim());
    }
    
    /**
     * 模糊查询学生
     */
    public List<Student> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return studentMapper.searchByKeyword(keyword.trim());
    }
    
    /**
     * 添加学生
     */
    @CacheEvict(value = "students", allEntries = true)
    public boolean addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("学生信息不能为空");
        }
        
        // 检查学号是否已存在
        if (existsByStudentNumber(student.getStudentNumber())) {
            throw new RuntimeException("学号已存在: " + student.getStudentNumber());
        }
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedTime(now);
        student.setUpdatedTime(now);
        
        // 设置默认状态
        if (student.getStatus() == null || student.getStatus().trim().isEmpty()) {
            student.setStatus("ACTIVE");
        }
        
        try {
            int result = studentMapper.insert(student);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("添加学生失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新学生信息
     */
    @CacheEvict(value = "students", allEntries = true)
    public boolean updateStudent(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("学生信息或ID不能为空");
        }
        
        // 检查学生是否存在
        Student existingStudent = findById(student.getId());
        if (existingStudent == null) {
            throw new RuntimeException("学生不存在，ID: " + student.getId());
        }
        
        // 设置更新时间
        student.setUpdatedTime(LocalDateTime.now());
        
        try {
            int result = studentMapper.update(student);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("更新学生信息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据ID删除学生
     */
    @CacheEvict(value = "students", allEntries = true)
    public boolean deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        
        try {
            int result = studentMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除学生失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 批量删除学生
     */
    @CacheEvict(value = "students", allEntries = true)
    public boolean deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("学生ID列表不能为空");
        }
        
        try {
            int result = studentMapper.deleteByIds(ids);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("批量删除学生失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查学号是否存在
     */
    public boolean existsByStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        try {
            return studentMapper.existsByStudentNumber(studentNumber.trim());
        } catch (Exception e) {
            throw new RuntimeException("检查学号是否存在失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据状态统计学生数量
     */
    public int countByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return 0;
        }
        try {
            return studentMapper.countByStatus(status.trim());
        } catch (Exception e) {
            throw new RuntimeException("统计学生数量失败: " + e.getMessage(), e);
        }
    }
}