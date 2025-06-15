package org.example.coursesystem.service;

import org.example.coursesystem.entity.Course;
import org.example.coursesystem.entity.CourseSelection;
import org.example.coursesystem.entity.Semester;
import org.example.coursesystem.entity.Student;
import org.example.coursesystem.mapper.CourseSelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 选课服务类
 */
@Service
@Transactional
public class CourseSelectionService {
    
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private SemesterService semesterService;
    
    @Autowired
    private StudentService studentService;
    
    /**
     * 查询所有选课记录
     */
    public List<CourseSelection> findAll() {
        return courseSelectionMapper.findAll();
    }
    
    /**
     * 查询所有选课记录（包含学生、课程、学期信息）
     */
    public List<CourseSelection> findAllWithDetails() {
        return courseSelectionMapper.findAllWithDetails();
    }
    
    /**
     * 根据ID查询选课记录
     */
    public CourseSelection findById(Long id) {
        return courseSelectionMapper.findById(id);
    }
    
    /**
     * 根据学生ID查询选课记录
     */
    public List<CourseSelection> findByStudentId(Long studentId) {
        return courseSelectionMapper.findByStudentId(studentId);
    }
    
    /**
     * 根据学生ID查询选课记录（包含课程、学期信息）
     */
    public List<CourseSelection> findByStudentIdWithDetails(Long studentId) {
        return courseSelectionMapper.findByStudentIdWithDetails(studentId);
    }

    /**
     * 根据课程ID查询选课记录
     */
    public List<CourseSelection> findByCourseId(Long courseId) {
        return courseSelectionMapper.findByCourseId(courseId);
    }
    
    /**
     * 根据课程ID查询选课记录（包含学生、学期信息）
     */
    public List<CourseSelection> findByCourseIdWithDetails(Long courseId) {
        return courseSelectionMapper.findByCourseIdWithDetails(courseId);
    }

    /**
     * 根据学期ID查询选课记录
     */
    public List<CourseSelection> findBySemesterId(Long semesterId) {
        return courseSelectionMapper.findBySemesterId(semesterId);
    }
    
    /**
     * 根据学期ID查询选课记录（包含学生、课程信息）
     */
    public List<CourseSelection> findBySemesterIdWithDetails(Long semesterId) {
        return courseSelectionMapper.findBySemesterIdWithDetails(semesterId);
    }
    
    /**
     * 根据学生ID和学期ID查询选课记录
     */
    public List<CourseSelection> findByStudentAndSemester(Long studentId, Long semesterId) {
        return courseSelectionMapper.findByStudentAndSemester(studentId, semesterId);
    }
    
    /**
     * 根据课程ID和学期ID查询选课记录
     */
    public List<CourseSelection> findByCourseAndSemester(Long courseId, Long semesterId) {
        return courseSelectionMapper.findByCourseAndSemester(courseId, semesterId);
    }
    
    /**
     * 查询学生在指定学期的选课记录（包含课程和学期信息）
     */
    public List<CourseSelection> findStudentCoursesWithDetails(Long studentId, Long semesterId) {
        return courseSelectionMapper.findStudentCoursesWithDetails(studentId, semesterId);
    }
    
    /**
     * 查询课程在指定学期的选课记录（包含学生信息）
     */
    public List<CourseSelection> findCourseStudentsWithDetails(Long courseId, Long semesterId) {
        return courseSelectionMapper.findCourseStudentsWithDetails(courseId, semesterId);
    }
    
    /**
     * 根据状态查询选课记录
     */
    public List<CourseSelection> findByStatus(String status) {
        return courseSelectionMapper.findByStatus(status);
    }
    
    /**
     * 根据状态查询选课记录（包含学生、课程、学期信息）
     */
    public List<CourseSelection> findByStatusWithDetails(String status) {
        return courseSelectionMapper.findByStatusWithDetails(status);
    }
    
    /**
     * 根据学生关键词查询选课记录（包含学生、课程、学期信息）
     */
    public List<CourseSelection> findByStudentKeywordWithDetails(String keyword) {
        return courseSelectionMapper.findByStudentKeywordWithDetails(keyword);
    }
    
    /**
     * 根据课程关键词查询选课记录（包含学生、课程、学期信息）
     */
    public List<CourseSelection> findByCourseKeywordWithDetails(String keyword) {
        return courseSelectionMapper.findByCourseKeywordWithDetails(keyword);
    }
    
    /**
     * 查询学生选课汇总信息（所有学期）
     */
    public Map<String, Object> findStudentCourseSummary(Long studentId) {
        return courseSelectionMapper.findStudentCourseSummaryAll(studentId);
    }
    
    /**
     * 学生选课
     */
    public String selectCourse(Long studentId, Long courseId, Long semesterId) {
        try {
            // 验证学生是否存在
            Student student = studentService.findById(studentId);
            if (student == null) {
                return "学生不存在";
            }
            
            // 验证课程是否存在
            Course course = courseService.findById(courseId);
            if (course == null) {
                return "课程不存在";
            }
            
            // 验证学期是否存在
            Semester semester = semesterService.findById(semesterId);
            if (semester == null) {
                return "学期不存在";
            }
            
            // 检查是否已经选过该课程
            if (isStudentSelectedCourse(studentId, courseId, semesterId)) {
                return "您已经选择了该课程";
            }
            
            // 检查课程是否可选
            if (!"ACTIVE".equals(course.getStatus())) {
                return "该课程当前不可选";
            }
            
            // 检查是否在选课时间内
            LocalDateTime now = LocalDateTime.now();
            if (semester.getRegistrationStartDate() != null && now.toLocalDate().isBefore(semester.getRegistrationStartDate())) {
                return "选课尚未开始";
            }
            if (semester.getRegistrationEndDate() != null && now.toLocalDate().isAfter(semester.getRegistrationEndDate())) {
                return "选课已结束";
            }
            
            // 检查课程容量
            long currentCount = countCourseSelections(courseId, semesterId);
            if (currentCount >= course.getMaxStudents()) {
                return "该课程已满员";
            }
            
            // 创建选课记录
            CourseSelection courseSelection = new CourseSelection();
            courseSelection.setStudentId(studentId);
            courseSelection.setCourseId(courseId);
            courseSelection.setSemesterId(semesterId);
            courseSelection.setSelectionTime(LocalDateTime.now());
            courseSelection.setStatus("SELECTED");
            courseSelection.setCreatedTime(LocalDateTime.now());
            courseSelection.setUpdatedTime(LocalDateTime.now());
            
            if (courseSelectionMapper.insert(courseSelection) > 0) {
                return "选课成功";
            } else {
                return "选课失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "选课失败：" + e.getMessage();
        }
    }
    
    /**
     * 学生退课
     */
    public String dropCourse(Long studentId, Long courseId, Long semesterId) {
        try {
            // 查找选课记录
            List<CourseSelection> selections = courseSelectionMapper.findByStudentAndSemester(studentId, semesterId);
            CourseSelection targetSelection = null;
            for (CourseSelection selection : selections) {
                if (selection.getCourseId().equals(courseId)) {
                    targetSelection = selection;
                    break;
                }
            }
            
            if (targetSelection == null) {
                return "未找到选课记录";
            }
            
            // 检查课程状态是否允许退课
            if ("COMPLETED".equals(targetSelection.getStatus())) {
                return "已完成的课程不能退课";
            }
            
            // 检查是否可以退课（放宽时间限制，允许在学期结束前退课）
            Semester semester = semesterService.findById(semesterId);
            if (semester != null && semester.getEndDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                if (now.toLocalDate().isAfter(semester.getEndDate())) {
                    return "学期已结束，无法退课";
                }
            }
            
            // 删除选课记录
            if (courseSelectionMapper.deleteById(targetSelection.getId()) > 0) {
                return "退课成功";
            } else {
                return "退课失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "退课失败：" + e.getMessage();
        }
    }
    
    /**
     * 添加或更新选课记录
     */
    public boolean save(CourseSelection courseSelection) {
        try {
            if (courseSelection.getId() == null) {
                // 新增
                courseSelection.setCreatedTime(LocalDateTime.now());
                courseSelection.setUpdatedTime(LocalDateTime.now());
                return courseSelectionMapper.insert(courseSelection) > 0;
            } else {
                // 更新
                courseSelection.setUpdatedTime(LocalDateTime.now());
                return courseSelectionMapper.update(courseSelection) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 更新选课记录
     */
    public boolean update(CourseSelection courseSelection) {
        try {
            courseSelection.setUpdatedTime(LocalDateTime.now());
            return courseSelectionMapper.update(courseSelection) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除选课记录
     */
    public boolean deleteById(Long id) {
        try {
            return courseSelectionMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 批量删除选课记录
     */
    public boolean deleteByIds(List<Long> ids) {
        try {
            return courseSelectionMapper.deleteByIds(ids) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据学生ID删除选课记录
     */
    public boolean deleteByStudentId(Long studentId) {
        try {
            return courseSelectionMapper.deleteByStudentId(studentId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据课程ID删除选课记录
     */
    public boolean deleteByCourseId(Long courseId) {
        try {
            return courseSelectionMapper.deleteByCourseId(courseId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查学生是否已选择指定课程
     */
    public boolean isStudentSelectedCourse(Long studentId, Long courseId, Long semesterId) {
        return courseSelectionMapper.isStudentSelectedCourse(studentId, courseId, semesterId);
    }
    
    /**
     * 统计学生在指定学期的选课数量
     */
    public long countStudentSelections(Long studentId, Long semesterId) {
        return courseSelectionMapper.countStudentSelections(studentId, semesterId);
    }
    
    /**
     * 统计课程在指定学期的选课人数
     */
    public long countCourseSelections(Long courseId, Long semesterId) {
        return courseSelectionMapper.countCourseSelections(courseId, semesterId);
    }
    
    /**
     * 统计选课总数
     */
    public long count() {
        return courseSelectionMapper.count();
    }
    
    /**
     * 根据条件统计选课数量
     */
    public long countByCondition(Long studentId, Long courseId, Long semesterId, String status) {
        return courseSelectionMapper.countByCondition(studentId, courseId, semesterId, status);
    }
}