package org.example.coursesystem.controller;

import org.example.coursesystem.aspect.LogOperation;
import org.example.coursesystem.entity.Student;
import org.example.coursesystem.service.StudentService;
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
 * 学生管理控制器
 */
@Controller// 注解，表示这是一个控制器组件
@RequestMapping("/admin/students")// 定义控制器的请求映射路径为/admin/students
@PreAuthorize("hasRole('ADMIN')")// 权限控制注解，只允许具有ADMIN角色的用户访问该控制器下的所有接口
public class StudentController {
    
    @Autowired// 自动装配学生服务接口的实现类
    private StudentService studentService;
    
    /**
     * 重定向到学生管理主页
     */
    @GetMapping
    public String redirectToIndex() {
        return "redirect:/admin/students/index";
    }
    
    /**
     * 学生管理主页
     */
    @GetMapping("/index")// 定义请求映射路径为/index
    @LogOperation(operationType = "SELECT", description = "查看学生列表", module = "学生管理")// 日志记录操作，操作类型为SELECT，描述为查看学生列表，模块为学生管理
    public String index(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "major", required = false) String major,
                       @RequestParam(value = "grade", required = false) Integer grade,
                       @RequestParam(value = "status", required = false) String status) {
        
        List<Student> students;
        
        // 根据查询条件获取学生列表
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentService.searchByKeyword(keyword);
            model.addAttribute("keyword", keyword);
        } else if (major != null && !major.trim().isEmpty()) {
            students = studentService.findByMajor(major);
            model.addAttribute("major", major);
        } else if (grade != null) {
            students = studentService.findByGrade(grade);
            model.addAttribute("grade", grade);
        } else if (status != null && !status.trim().isEmpty()) {
            students = studentService.findByStatus(status);
            model.addAttribute("status", status);
        } else {
            students = studentService.findAll();//
        }
        
        model.addAttribute("students", students);
        model.addAttribute("totalCount", students.size());
        
        // 统计信息
        model.addAttribute("activeCount", studentService.countByStatus("ACTIVE"));
        model.addAttribute("inactiveCount", studentService.countByStatus("INACTIVE"));
        model.addAttribute("graduatedCount", studentService.countByStatus("GRADUATED"));
        
        return "admin/students/index";
    }
    
    /**
     * 显示添加学生页面
     */
    @GetMapping("/add")
    @LogOperation(operationType = "SELECT", description = "显示添加学生页面", module = "学生管理")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("action", "add");
        return "admin/students/form";
    }
    
    /**
     * 处理添加学生请求
     */
    @PostMapping("/add")
    @LogOperation(operationType = "CREATE", description = "添加学生", module = "学生管理", logParams = true)
    public String addStudent(@Valid @ModelAttribute Student student,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");// 模型属性，用于在视图中显示操作类型为添加学生
            return "admin/students/form";// 返回添加学生表单视图
        }
        
        try {
            boolean success = studentService.addStudent(student);// 调用学生服务接口的添加学生方法
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage", "学生添加成功！");
                return "redirect:/admin/students";
            } else {
                model.addAttribute("errorMessage", "学生添加失败，请重试。");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        
        model.addAttribute("action", "add");
        return "admin/students/form";
    }
    
    /**
     * 显示编辑学生页面
     */
    @GetMapping("/edit/{id}")
    @LogOperation(operationType = "SELECT", description = "显示编辑学生页面", module = "学生管理")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.findById(id);
            if (student == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "学生不存在！");
                return "redirect:/admin/students";
            }
            
            model.addAttribute("student", student);
            model.addAttribute("action", "edit");
            return "admin/students/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "获取学生信息失败：" + e.getMessage());
            return "redirect:/admin/students";
        }
    }
    
    /**
     * 处理编辑学生请求
     */
    @PostMapping("/edit/{id}")
    @LogOperation(operationType = "UPDATE", description = "更新学生信息", module = "学生管理", logParams = true)
    public String editStudent(@PathVariable Long id,
                            @Valid @ModelAttribute Student student,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        
        student.setId(id);
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "admin/students/form";
        }
        
        try {
            boolean success = studentService.updateStudent(student);// 调用学生服务接口的更新学生方法
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage", "学生信息更新成功！");
                return "redirect:/admin/students";
            } else {
                model.addAttribute("errorMessage", "学生信息更新失败，请重试。");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        
        model.addAttribute("action", "edit");
        return "admin/students/form";
    }
    
    /**
     * 显示学生详情页面
     */
    @GetMapping("/view/{id}")
    @LogOperation(operationType = "SELECT", description = "查看学生详情", module = "学生管理")
    public String viewStudent(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.findById(id);
            if (student == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "学生不存在！");
                return "redirect:/admin/students";
            }
            
            model.addAttribute("student", student);
            return "admin/students/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "获取学生信息失败：" + e.getMessage());
            return "redirect:/admin/students";
        }
    }
    
    /**
     * 删除学生
     */
    @PostMapping("/delete/{id}")
    @ResponseBody// 响应体，用于返回JSON格式的数据
    @LogOperation(operationType = "DELETE", description = "删除学生", module = "学生管理", logParams = true)
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = studentService.deleteById(id);// 调用学生服务接口的删除学生方法
            if (success) {
                response.put("success", true);
                response.put("message", "学生删除成功！");
            } else {
                response.put("success", false);
                response.put("message", "学生删除失败，请重试。");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 批量删除学生
     */
    @PostMapping("/delete/batch")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> batchDeleteStudents(@RequestBody List<Long> ids) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = studentService.deleteByIds(ids);// 调用学生服务接口的批量删除学生方法
            if (success) {
                response.put("success", true);
                response.put("message", "批量删除成功！");
            } else {
                response.put("success", false);
                response.put("message", "批量删除失败，请重试。");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 检查学号是否存在
     */
    @GetMapping("/check-student-number")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkStudentNumber(@RequestParam String studentNumber) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean exists = studentService.existsByStudentNumber(studentNumber);// 调用学生服务接口的检查学号是否存在方法
            response.put("exists", exists);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}