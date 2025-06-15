package org.example.coursesystem.controller;

import org.example.coursesystem.entity.Course;
import org.example.coursesystem.entity.CourseSelection;
import org.example.coursesystem.entity.Semester;
import org.example.coursesystem.entity.Student;
import org.example.coursesystem.service.CourseSelectionService;
import org.example.coursesystem.service.CourseService;
import org.example.coursesystem.service.SemesterService;
import org.example.coursesystem.service.StudentService;
import org.example.coursesystem.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选课管理控制器
 */
@Controller
public class CourseSelectionController {
    
    @Autowired
    private CourseSelectionService courseSelectionService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private SemesterService semesterService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private ExportService exportService;
    
    // ==================== 学生选课功能 ====================
    
    /**
     * 学生选课主页
     */
    @GetMapping("/student/course-selection")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentCourseSelection(Model model, Authentication authentication) {
        // 获取当前登录学生信息
        String username = authentication.getName();
        Student student = studentService.findByStudentNumber(username);
        if (student == null) {
            model.addAttribute("error", "学生信息不存在！");
            return "error";
        }
        
        // 获取当前学期
        Semester currentSemester = semesterService.findCurrentSemester();
        if (currentSemester == null) {
            model.addAttribute("error", "当前没有可选课的学期！");
            return "student/course-selection/index";
        }
        
        // 获取可选课程
        List<Course> availableCourses = courseService.findAvailableCourses(currentSemester.getId(), student.getId());
        
        // 获取已选课程
        List<CourseSelection> selectedCourses = courseSelectionService.findStudentCoursesWithDetails(
                student.getId(), currentSemester.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("currentSemester", currentSemester);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("selectedCourses", selectedCourses);
        model.addAttribute("selectedCount", selectedCourses.size());
        
        return "student/course-selection/index";
    }
    
    /**
     * 学生选课
     */
    @PostMapping("/student/course-selection/select")
    @ResponseBody
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> selectCourse(
            @RequestParam Long courseId,
            Authentication authentication) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取当前登录学生信息
            String username = authentication.getName();
            Student student = studentService.findByStudentNumber(username);
            if (student == null) {
                result.put("success", false);
                result.put("message", "学生信息不存在！");
                return ResponseEntity.ok(result);
            }
            
            // 获取当前学期
            Semester currentSemester = semesterService.findCurrentSemester();
            if (currentSemester == null) {
                result.put("success", false);
                result.put("message", "当前没有可选课的学期！");
                return ResponseEntity.ok(result);
            }
            
            // 执行选课
            String message = courseSelectionService.selectCourse(student.getId(), courseId, currentSemester.getId());
            
            if ("选课成功".equals(message)) {
                result.put("success", true);
                result.put("message", message);
            } else {
                result.put("success", false);
                result.put("message", message);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "选课失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 学生退课
     */
    @PostMapping("/student/course-selection/drop")
    @ResponseBody
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Object>> dropCourse(
            @RequestParam Long courseId,
            Authentication authentication) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取当前登录学生信息
            String username = authentication.getName();
            Student student = studentService.findByStudentNumber(username);
            if (student == null) {
                result.put("success", false);
                result.put("message", "学生信息不存在！");
                return ResponseEntity.ok(result);
            }
            
            // 获取当前学期
            Semester currentSemester = semesterService.findCurrentSemester();
            if (currentSemester == null) {
                result.put("success", false);
                result.put("message", "当前没有可选课的学期！");
                return ResponseEntity.ok(result);
            }
            
            // 执行退课
            String message = courseSelectionService.dropCourse(student.getId(), courseId, currentSemester.getId());
            
            if ("退课成功".equals(message)) {
                result.put("success", true);
                result.put("message", message);
            } else {
                result.put("success", false);
                result.put("message", message);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "退课失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 学生选课情况查看
     */
    @GetMapping("/student/course-selection/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public String myCourses(Model model, Authentication authentication,
                           @RequestParam(value = "semesterId", required = false) Long semesterId) {
        
        // 获取当前登录学生信息
        String username = authentication.getName();
        System.out.println("[DEBUG] 当前登录用户名: " + username);
        
        Student student = studentService.findByStudentNumber(username);
        if (student == null) {
            System.out.println("[ERROR] 找不到学生信息，用户名: " + username);
            model.addAttribute("error", "学生信息不存在！");
            return "error";
        }
        System.out.println("[DEBUG] 找到学生: ID=" + student.getId() + ", 姓名=" + student.getName());
        
        // 获取所有学期
        List<Semester> semesters = semesterService.findAll();
        System.out.println("[DEBUG] 总学期数: " + (semesters != null ? semesters.size() : 0));
        
        // 如果没有指定学期，使用当前学期
        if (semesterId == null) {
            Semester currentSemester = semesterService.findCurrentSemester();
            if (currentSemester != null) {
                semesterId = currentSemester.getId();
                System.out.println("[DEBUG] 使用当前学期: ID=" + semesterId + ", 名称=" + currentSemester.getSemesterName());
            } else {
                System.out.println("[WARNING] 没有找到当前学期");
            }
        } else {
            System.out.println("[DEBUG] 指定学期ID: " + semesterId);
        }
        
        List<CourseSelection> selectedCourses = null;
        Semester selectedSemester = null;
        if (semesterId != null) {
            selectedCourses = courseSelectionService.findStudentCoursesWithDetails(student.getId(), semesterId);
            selectedSemester = semesterService.findById(semesterId);
            
            System.out.println("[DEBUG] 查询选课记录 - 学生ID: " + student.getId() + ", 学期ID: " + semesterId);
            System.out.println("[DEBUG] 找到选课记录数: " + (selectedCourses != null ? selectedCourses.size() : 0));
            
            if (selectedCourses != null && !selectedCourses.isEmpty()) {
                for (CourseSelection cs : selectedCourses) {
                    System.out.println("[DEBUG] 选课记录: ID=" + cs.getId() + ", 课程ID=" + cs.getCourseId() + 
                                     ", 课程名=" + (cs.getCourse() != null ? cs.getCourse().getCourseName() : "NULL"));
                }
            } else {
                System.out.println("[WARNING] 没有找到选课记录");
            }
        }
        
        // 获取学生选课汇总信息
        Map<String, Object> courseSummary = courseSelectionService.findStudentCourseSummary(student.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("semesters", semesters);
        model.addAttribute("selectedSemester", selectedSemester);
        model.addAttribute("selectedCourses", selectedCourses);
        model.addAttribute("courseSummary", courseSummary);
        
        return "student/course-selection/my-courses";
    }
    
    // ==================== 管理员选课管理功能 ====================
    
    /**
     * 选课管理主页
     */
    @GetMapping("/admin/course-selections")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminIndex(Model model,
                            @RequestParam(value = "studentId", required = false) Long studentId,
                            @RequestParam(value = "courseId", required = false) Long courseId,
                            @RequestParam(value = "semesterId", required = false) Long semesterId,
                            @RequestParam(value = "status", required = false) String status) {
        
        List<CourseSelection> courseSelections;
        
        // 根据查询条件获取选课记录
        if (studentId != null && semesterId != null) {
            courseSelections = courseSelectionService.findStudentCoursesWithDetails(studentId, semesterId);
        } else if (courseId != null && semesterId != null) {
            courseSelections = courseSelectionService.findCourseStudentsWithDetails(courseId, semesterId);
        } else if (studentId != null) {
            courseSelections = courseSelectionService.findByStudentIdWithDetails(studentId);
        } else if (courseId != null) {
            courseSelections = courseSelectionService.findByCourseIdWithDetails(courseId);
        } else if (semesterId != null) {
            courseSelections = courseSelectionService.findBySemesterIdWithDetails(semesterId);
        } else if (status != null && !status.trim().isEmpty()) {
            courseSelections = courseSelectionService.findByStatusWithDetails(status);
        } else {
            courseSelections = courseSelectionService.findAllWithDetails();
        }
        
        // 获取所有学生、课程、学期用于筛选
        List<Student> students = studentService.findAll();
        List<Course> courses = courseService.findAll();
        List<Semester> semesters = semesterService.findAll();
        
        model.addAttribute("courseSelections", courseSelections);
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        model.addAttribute("semesters", semesters);
        model.addAttribute("totalCount", courseSelections.size());
        
        // 查询条件回显
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedSemesterId", semesterId);
        model.addAttribute("selectedStatus", status);
        
        return "admin/course-selections/index";
    }
    
    /**
     * 更新选课成绩
     */
    @PostMapping("/admin/course-selections/{id}/grade")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateGrade(@PathVariable Long id,
                             @RequestParam("grade") BigDecimal score,
                             @RequestParam("status") String status,
                             RedirectAttributes redirectAttributes) {
        try {
            CourseSelection courseSelection = courseSelectionService.findById(id);
            if (courseSelection == null) {
                redirectAttributes.addFlashAttribute("error", "选课记录不存在！");
                return "redirect:/admin/course-selections";
            }
            
            courseSelection.setScore(score);
            courseSelection.setStatus(status);
            
            // 根据成绩计算绩点
            if (score != null) {
                BigDecimal gradePoint;
                if (score.compareTo(new BigDecimal("90")) >= 0) {
                    gradePoint = new BigDecimal("4.0");
                } else if (score.compareTo(new BigDecimal("80")) >= 0) {
                    gradePoint = new BigDecimal("3.0");
                } else if (score.compareTo(new BigDecimal("70")) >= 0) {
                    gradePoint = new BigDecimal("2.0");
                } else if (score.compareTo(new BigDecimal("60")) >= 0) {
                    gradePoint = new BigDecimal("1.0");
                } else {
                    gradePoint = new BigDecimal("0.0");
                }
                courseSelection.setGradePoint(gradePoint);
            }
            
            if (courseSelectionService.update(courseSelection)) {
                redirectAttributes.addFlashAttribute("success", "成绩更新成功！");
            } else {
                redirectAttributes.addFlashAttribute("error", "成绩更新失败！");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        
        return "redirect:/admin/course-selections";
    }
    
    /**
     * 删除选课记录
     */
    @PostMapping("/admin/course-selections/delete/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            CourseSelection courseSelection = courseSelectionService.findById(id);
            if (courseSelection == null) {
                result.put("success", false);
                result.put("message", "选课记录不存在！");
                return ResponseEntity.ok(result);
            }
            
            if (courseSelectionService.deleteById(id)) {
                result.put("success", true);
                result.put("message", "选课记录删除成功！");
            } else {
                result.put("success", false);
                result.put("message", "选课记录删除失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量删除选课记录
     */
    @PostMapping("/admin/course-selections/batch-delete")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestParam("ids") List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (ids == null || ids.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要删除的选课记录！");
                return ResponseEntity.ok(result);
            }
            
            if (courseSelectionService.deleteByIds(ids)) {
                result.put("success", true);
                result.put("message", "批量删除成功！");
            } else {
                result.put("success", false);
                result.put("message", "批量删除失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取选课统计信息
     */
    @GetMapping("/admin/course-selections/statistics")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("totalCount", courseSelectionService.count());
        
        // 获取当前学期统计
        Semester currentSemester = semesterService.findCurrentSemester();
        if (currentSemester != null) {
            long currentSemesterCount = courseSelectionService.countByCondition(
                    null, null, currentSemester.getId(), null);
            result.put("currentSemesterCount", currentSemesterCount);
        }
        
        return ResponseEntity.ok(result);
    }
    
    // ==================== 导出功能 ====================
    
    /**
     * 导出Excel格式的选课数据
     */
    @GetMapping("/admin/course-selections/export/excel")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportExcel(HttpServletResponse response,
                           @RequestParam(value = "studentKeyword", required = false) String studentKeyword,
                           @RequestParam(value = "courseKeyword", required = false) String courseKeyword,
                           @RequestParam(value = "semesterId", required = false) Long semesterId,
                           @RequestParam(value = "status", required = false) String status) {
        try {
            List<CourseSelection> courseSelections = getFilteredCourseSelections(studentKeyword, courseKeyword, semesterId, status);
            exportService.exportToExcel(courseSelections, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 导出PDF格式的选课数据
     */
    @GetMapping("/admin/course-selections/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportPdf(HttpServletResponse response,
                         @RequestParam(value = "studentKeyword", required = false) String studentKeyword,
                         @RequestParam(value = "courseKeyword", required = false) String courseKeyword,
                         @RequestParam(value = "semesterId", required = false) Long semesterId,
                         @RequestParam(value = "status", required = false) String status) {
        try {
            List<CourseSelection> courseSelections = getFilteredCourseSelections(studentKeyword, courseKeyword, semesterId, status);
            exportService.exportToPdf(courseSelections, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 导出Word格式的选课数据
     */
    @GetMapping("/admin/course-selections/export/word")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportWord(HttpServletResponse response,
                          @RequestParam(value = "studentKeyword", required = false) String studentKeyword,
                          @RequestParam(value = "courseKeyword", required = false) String courseKeyword,
                          @RequestParam(value = "semesterId", required = false) Long semesterId,
                          @RequestParam(value = "status", required = false) String status) {
        try {
            List<CourseSelection> courseSelections = getFilteredCourseSelections(studentKeyword, courseKeyword, semesterId, status);
            exportService.exportToWord(courseSelections, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据筛选条件获取选课数据
     */
    private List<CourseSelection> getFilteredCourseSelections(String studentKeyword, String courseKeyword, Long semesterId, String status) {
        List<CourseSelection> courseSelections;
        
        // 根据查询条件获取选课记录（复用现有逻辑）
        if (studentKeyword != null && !studentKeyword.trim().isEmpty()) {
            courseSelections = courseSelectionService.findByStudentKeywordWithDetails(studentKeyword.trim());
        } else if (courseKeyword != null && !courseKeyword.trim().isEmpty()) {
            courseSelections = courseSelectionService.findByCourseKeywordWithDetails(courseKeyword.trim());
        } else if (semesterId != null) {
            courseSelections = courseSelectionService.findBySemesterIdWithDetails(semesterId);
        } else if (status != null && !status.trim().isEmpty()) {
            courseSelections = courseSelectionService.findByStatusWithDetails(status);
        } else {
            courseSelections = courseSelectionService.findAllWithDetails();
        }
        
        return courseSelections;
    }
}