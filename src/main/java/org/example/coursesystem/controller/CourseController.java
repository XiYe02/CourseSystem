package org.example.coursesystem.controller;

import org.example.coursesystem.aspect.LogOperation;
import org.example.coursesystem.entity.Course;
import org.example.coursesystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程管理控制器
 */
@Controller
@RequestMapping("/admin/courses")// 注解，表示该控制器处理的请求路径前缀为/admin/courses
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {
    
    @Autowired// 注解，表示自动装配课程业务层接口的实现类
    private CourseService courseService;
    
    /**
     * 课程管理主页
     */
    @GetMapping
    @LogOperation(operationType = "SELECT", description = "查看课程列表", module = "课程管理")
    public String index(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "courseType", required = false) String courseType,
                       @RequestParam(value = "department", required = false) String department,
                       @RequestParam(value = "status", required = false) String status) {
        
        List<Course> courses;
        
        // 根据查询条件获取课程列表
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseService.searchByKeyword(keyword);
            model.addAttribute("keyword", keyword);
        } else if (courseType != null && !courseType.trim().isEmpty()) {
            courses = courseService.findByCourseType(courseType);
            model.addAttribute("courseType", courseType);
        } else if (department != null && !department.trim().isEmpty()) {
            courses = courseService.findByDepartment(department);
            model.addAttribute("department", department);
        } else if (status != null && !status.trim().isEmpty()) {
            courses = courseService.findByStatus(status);
            model.addAttribute("status", status);
        } else {
            courses = courseService.findAll();
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("totalCount", courses.size());
        
        // 统计信息
        model.addAttribute("activeCount", courseService.countByCondition(null, null, "ACTIVE"));
        model.addAttribute("inactiveCount", courseService.countByCondition(null, null, "INACTIVE"));
        
        return "admin/courses/index";
    }
    
    /**
     * 显示添加课程页面
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("action", "add");
        return "admin/courses/form";
    }
    
    /**
     * 处理添加课程
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Course course,
                     BindingResult bindingResult,
                     RedirectAttributes redirectAttributes,
                     Model model) {
        
        // 验证表单数据
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");
            return "admin/courses/form";
        }
        
        // 业务验证
        String validationError = courseService.validateCourse(course);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("action", "add");
            return "admin/courses/form";
        }
        
        // 保存课程
        if (courseService.save(course)) {
            redirectAttributes.addFlashAttribute("success", "课程添加成功！");
            return "redirect:/admin/courses";
        } else {
            model.addAttribute("error", "课程添加失败，请重试！");
            model.addAttribute("action", "add");
            return "admin/courses/form";
        }
    }
    
    /**
     * 显示编辑课程页面
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Course course = courseService.findById(id);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "课程不存在！");
            return "redirect:/admin/courses";
        }
        
        model.addAttribute("course", course);
        model.addAttribute("action", "edit");
        return "admin/courses/form";
    }
    
    /**
     * 处理编辑课程
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute Course course,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        
        // 设置ID
        course.setId(id);
        
        // 验证表单数据
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "admin/courses/form";
        }
        
        // 业务验证
        String validationError = courseService.validateCourse(course);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("action", "edit");
            return "admin/courses/form";
        }
        
        // 更新课程
        if (courseService.update(course)) {
            redirectAttributes.addFlashAttribute("success", "课程更新成功！");
            return "redirect:/admin/courses";
        } else {
            model.addAttribute("error", "课程更新失败，请重试！");
            model.addAttribute("action", "edit");
            return "admin/courses/form";
        }
    }
    
    /**
     * 查看课程详情
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Course course = courseService.findById(id);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "课程不存在！");
            return "redirect:/admin/courses";
        }
        
        model.addAttribute("course", course);
        return "admin/courses/view";
    }
    
    /**
     * 删除课程
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Course course = courseService.findById(id);
            if (course == null) {
                result.put("success", false);
                result.put("message", "课程不存在！");
                return ResponseEntity.ok(result);
            }
            
            if (courseService.deleteById(id)) {
                result.put("success", true);
                result.put("message", "课程删除成功！");
            } else {
                result.put("success", false);
                result.put("message", "课程删除失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量删除课程
     */
    @PostMapping("/batch-delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestParam("ids") List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (ids == null || ids.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要删除的课程！");
                return ResponseEntity.ok(result);
            }
            
            if (courseService.deleteByIds(ids)) {
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
     * 检查课程代码是否存在
     */
    @GetMapping("/check-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkCourseCode(
            @RequestParam String courseCode,
            @RequestParam(required = false) Long excludeId) {
        
        Map<String, Object> result = new HashMap<>();
        boolean exists = courseService.existsByCourseCode(courseCode, excludeId);
        result.put("exists", exists);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取课程统计信息
     */
    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("totalCount", courseService.count());
        result.put("activeCount", courseService.countByCondition(null, null, "ACTIVE"));
        result.put("inactiveCount", courseService.countByCondition(null, null, "INACTIVE"));
        
        return ResponseEntity.ok(result);
    }
}