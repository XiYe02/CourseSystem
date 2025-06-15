package org.example.coursesystem.service;

import org.example.coursesystem.entity.Semester;
import org.example.coursesystem.mapper.SemesterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学期服务类
 */
@Service
@Transactional
public class SemesterService {
    
    @Autowired
    private SemesterMapper semesterMapper;
    
    /**
     * 查询所有学期
     */
    @Cacheable(value = "semesters", key = "'all'")
    public List<Semester> findAll() {
        return semesterMapper.findAll();
    }
    
    /**
     * 根据ID查询学期
     */
    @Cacheable(value = "semesters", key = "#id")
    public Semester findById(Long id) {
        return semesterMapper.findById(id);
    }
    
    /**
     * 根据学期代码查询学期
     */
    @Cacheable(value = "semesters", key = "'code:' + #semesterCode")
    public Semester findBySemesterCode(String semesterCode) {
        return semesterMapper.findBySemesterCode(semesterCode);
    }
    
    /**
     * 根据学年查询学期
     */
    public List<Semester> findByAcademicYear(String academicYear) {
        return semesterMapper.findByAcademicYear(academicYear);
    }
    
    /**
     * 根据学期类型查询
     */
    public List<Semester> findBySemesterType(String semesterType) {
        return semesterMapper.findBySemesterType(semesterType);
    }
    
    /**
     * 根据状态查询
     */
    public List<Semester> findByStatus(String status) {
        return semesterMapper.findByStatus(status);
    }
    
    /**
     * 查询当前学期
     */
    @Cacheable(value = "semesters", key = "'current'")
    public Semester findCurrentSemester() {
        return semesterMapper.findCurrentSemester();
    }
    
    /**
     * 查询可选课学期
     */
    public List<Semester> findRegistrationAvailableSemesters() {
        return semesterMapper.findRegistrationAvailableSemesters();
    }
    
    /**
     * 添加或更新学期
     */
    @CacheEvict(value = "semesters", allEntries = true)
    public boolean save(Semester semester) {
        try {
            if (semester.getId() == null) {
                // 新增
                semester.setCreatedTime(LocalDateTime.now());
                semester.setUpdatedTime(LocalDateTime.now());
                return semesterMapper.insert(semester) > 0;
            } else {
                // 更新
                semester.setUpdatedTime(LocalDateTime.now());
                return semesterMapper.update(semester) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 更新学期
     */
    @CacheEvict(value = "semesters", allEntries = true)
    public boolean update(Semester semester) {
        try {
            semester.setUpdatedTime(LocalDateTime.now());
            return semesterMapper.update(semester) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除学期
     */
    @CacheEvict(value = "semesters", allEntries = true)
    public boolean deleteById(Long id) {
        try {
            return semesterMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 批量删除学期
     */
    @CacheEvict(value = "semesters", allEntries = true)
    public boolean deleteByIds(List<Long> ids) {
        try {
            return semesterMapper.deleteByIds(ids) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查学期代码是否存在
     */
    public boolean existsBySemesterCode(String semesterCode, Long excludeId) {
        return semesterMapper.existsBySemesterCode(semesterCode, excludeId);
    }
    
    /**
     * 统计学期总数
     */
    public long count() {
        return semesterMapper.count();
    }
    
    /**
     * 验证学期数据
     */
    public String validateSemester(Semester semester) {
        if (semester.getSemesterCode() == null || semester.getSemesterCode().trim().isEmpty()) {
            return "学期代码不能为空";
        }
        if (semester.getSemesterName() == null || semester.getSemesterName().trim().isEmpty()) {
            return "学期名称不能为空";
        }
        if (semester.getAcademicYear() == null || semester.getAcademicYear().trim().isEmpty()) {
            return "学年不能为空";
        }
        if (semester.getStartDate() == null) {
            return "开始日期不能为空";
        }
        if (semester.getEndDate() == null) {
            return "结束日期不能为空";
        }
        if (semester.getStartDate().isAfter(semester.getEndDate())) {
            return "开始日期不能晚于结束日期";
        }
        if (semester.getRegistrationStartDate() != null && semester.getRegistrationEndDate() != null) {
            if (semester.getRegistrationStartDate().isAfter(semester.getRegistrationEndDate())) {
                return "选课开始日期不能晚于选课结束日期";
            }
        }
        
        // 检查学期代码是否重复
        if (existsBySemesterCode(semester.getSemesterCode(), semester.getId())) {
            return "学期代码已存在";
        }
        
        return null; // 验证通过
    }
}