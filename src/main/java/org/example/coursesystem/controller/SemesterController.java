package org.example.coursesystem.controller;

import org.example.coursesystem.entity.Semester;
import org.example.coursesystem.service.SemesterService;
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
 * 学期管理控制器
 */
@Controller
@RequestMapping("/admin/semesters")
@PreAuthorize("hasRole('ADMIN')")
public class SemesterController {
    
    @Autowired
    private SemesterService semesterService;
    
    /**
     * 学期管理主页
     */
    @GetMapping
    public String index(Model model,
                       @RequestParam(value = "academicYear", required = false) String academicYear,
                       @RequestParam(value = "semesterType", required = false) String semesterType,
                       @RequestParam(value = "status", required = false) String status) {
        
        List<Semester> semesters;
        
        // 根据查询条件获取学期列表
        if (academicYear != null && !academicYear.trim().isEmpty()) {
            semesters = semesterService.findByAcademicYear(academicYear);
            model.addAttribute("academicYear", academicYear);
        } else if (semesterType != null && !semesterType.trim().isEmpty()) {
            semesters = semesterService.findBySemesterType(semesterType);
            model.addAttribute("semesterType", semesterType);
        } else if (status != null && !status.trim().isEmpty()) {
            semesters = semesterService.findByStatus(status);
            model.addAttribute("status", status);
        } else {
            semesters = semesterService.findAll();
        }
        
        model.addAttribute("semesters", semesters);
        model.addAttribute("totalCount", semesters.size());
        
        // 当前学期
        Semester currentSemester = semesterService.findCurrentSemester();
        model.addAttribute("currentSemester", currentSemester);
        
        return "admin/semesters/index";
    }
    
    /**
     * 显示添加学期页面
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("semester", new Semester());
        model.addAttribute("action", "add");
        return "admin/semesters/form";
    }
    
    /**
     * 处理添加学期
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Semester semester,
                     BindingResult bindingResult,
                     RedirectAttributes redirectAttributes,
                     Model model) {
        
        // 验证表单数据
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add");
            return "admin/semesters/form";
        }
        
        // 业务验证
        String validationError = semesterService.validateSemester(semester);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("action", "add");
            return "admin/semesters/form";
        }
        
        // 保存学期
        if (semesterService.save(semester)) {
            redirectAttributes.addFlashAttribute("success", "学期添加成功！");
            return "redirect:/admin/semesters";
        } else {
            model.addAttribute("error", "学期添加失败，请重试！");
            model.addAttribute("action", "add");
            return "admin/semesters/form";
        }
    }
    
    /**
     * 显示编辑学期页面
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Semester semester = semesterService.findById(id);
        if (semester == null) {
            redirectAttributes.addFlashAttribute("error", "学期不存在！");
            return "redirect:/admin/semesters";
        }
        
        model.addAttribute("semester", semester);
        model.addAttribute("action", "edit");
        return "admin/semesters/form";
    }
    
    /**
     * 处理编辑学期
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                      @Valid @ModelAttribute Semester semester,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        
        // 设置ID
        semester.setId(id);
        
        // 验证表单数据
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "edit");
            return "admin/semesters/form";
        }
        
        // 业务验证
        String validationError = semesterService.validateSemester(semester);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("action", "edit");
            return "admin/semesters/form";
        }
        
        // 更新学期
        if (semesterService.update(semester)) {
            redirectAttributes.addFlashAttribute("success", "学期更新成功！");
            return "redirect:/admin/semesters";
        } else {
            model.addAttribute("error", "学期更新失败，请重试！");
            model.addAttribute("action", "edit");
            return "admin/semesters/form";
        }
    }
    
    /**
     * 查看学期详情
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Semester semester = semesterService.findById(id);
        if (semester == null) {
            redirectAttributes.addFlashAttribute("error", "学期不存在！");
            return "redirect:/admin/semesters";
        }
        
        model.addAttribute("semester", semester);
        return "admin/semesters/view";
    }
    
    /**
     * 删除学期
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Semester semester = semesterService.findById(id);
            if (semester == null) {
                result.put("success", false);
                result.put("message", "学期不存在！");
                return ResponseEntity.ok(result);
            }
            
            if (semesterService.deleteById(id)) {
                result.put("success", true);
                result.put("message", "学期删除成功！");
            } else {
                result.put("success", false);
                result.put("message", "学期删除失败！");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量删除学期
     */
    @PostMapping("/batch-delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestParam("ids") List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (ids == null || ids.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要删除的学期！");
                return ResponseEntity.ok(result);
            }
            
            if (semesterService.deleteByIds(ids)) {
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
     * 检查学期代码是否存在
     */
    @GetMapping("/check-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkSemesterCode(
            @RequestParam String semesterCode,
            @RequestParam(required = false) Long excludeId) {
        
        Map<String, Object> result = new HashMap<>();
        boolean exists = semesterService.existsBySemesterCode(semesterCode, excludeId);
        result.put("exists", exists);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取当前学期
     */
    @GetMapping("/current")
    @ResponseBody
    public ResponseEntity<Semester> getCurrentSemester() {
        Semester currentSemester = semesterService.findCurrentSemester();
        return ResponseEntity.ok(currentSemester);
    }
    
    /**
     * 获取可选课学期列表
     */
    @GetMapping("/registration-available")
    @ResponseBody
    public ResponseEntity<List<Semester>> getRegistrationAvailableSemesters() {
        List<Semester> semesters = semesterService.findRegistrationAvailableSemesters();
        return ResponseEntity.ok(semesters);
    }
    
    /**
     * 获取学期统计信息
     */
    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("totalCount", semesterService.count());
        result.put("currentSemester", semesterService.findCurrentSemester());
        result.put("registrationAvailableCount", semesterService.findRegistrationAvailableSemesters().size());
        
        return ResponseEntity.ok(result);
    }
}