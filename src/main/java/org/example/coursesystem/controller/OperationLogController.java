package org.example.coursesystem.controller;

import org.example.coursesystem.aspect.LogOperation;
import org.example.coursesystem.entity.OperationLog;
import org.example.coursesystem.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志管理控制器
 */
@Controller
@RequestMapping("/admin/logs")
@PreAuthorize("hasRole('ADMIN')")
public class OperationLogController {
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 操作日志管理主页
     */
    @GetMapping
    @LogOperation(operationType = "SELECT", description = "查看操作日志列表", module = "日志管理")
    public String index(Model model,
                       @RequestParam(value = "username", required = false) String username,
                       @RequestParam(value = "operationType", required = false) String operationType,
                       @RequestParam(value = "module", required = false) String module,
                       @RequestParam(value = "result", required = false) String result,
                       @RequestParam(value = "startTime", required = false) 
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                       @RequestParam(value = "endTime", required = false) 
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "size", defaultValue = "20") int size) {
        
        List<OperationLog> logs;
        int totalCount;
        
        // 根据查询条件获取操作日志列表
        if (hasSearchConditions(username, operationType, module, result, startTime, endTime)) {
            logs = operationLogService.getOperationLogsByConditions(
                    null, username, operationType, module, result, startTime, endTime);
            totalCount = operationLogService.getCountByConditions(
                    null, username, operationType, module, result, startTime, endTime);
        } else {
            logs = operationLogService.getOperationLogsWithPagination(page, size);
            totalCount = operationLogService.getTotalCount();
        }
        
        // 计算分页信息
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        model.addAttribute("logs", logs);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        
        // 保持查询条件
        model.addAttribute("username", username);
        model.addAttribute("operationType", operationType);
        model.addAttribute("module", module);
        model.addAttribute("result", result);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        
        // 统计信息
        model.addAttribute("successCount", operationLogService.getCountByConditions(
                null, null, null, null, "SUCCESS", null, null));
        model.addAttribute("failureCount", operationLogService.getCountByConditions(
                null, null, null, null, "FAILURE", null, null));
        
        return "admin/logs/index";
    }
    
    /**
     * 操作日志详情页面
     */
    @GetMapping("/view")
    @LogOperation(operationType = "SELECT", description = "查看操作日志详情", module = "日志管理")
    public String viewLog(@RequestParam Long id, Model model) {
        OperationLog log = operationLogService.getOperationLogById(id);
        if (log == null) {
            model.addAttribute("error", "操作日志不存在！");
            return "admin/logs/index";
        }
        
        model.addAttribute("log", log);
        return "admin/logs/view";
    }
    
    /**
     * 清理操作日志页面
     */
    @GetMapping("/cleanup")
    @LogOperation(operationType = "SELECT", description = "查看日志清理页面", module = "日志管理")
    public String showCleanupPage(Model model) {
        // 统计信息
        int totalCount = operationLogService.getTotalCount();
        model.addAttribute("totalCount", totalCount);
        
        return "admin/logs/cleanup";
    }
    
    /**
     * 执行日志清理
     */
    @GetMapping("/cleanup/execute")
    @LogOperation(operationType = "DELETE", description = "清理历史操作日志", module = "日志管理")
    public String executeCleanup(@RequestParam(value = "days", defaultValue = "30") int days, 
                                Model model) {
        try {
            LocalDateTime beforeTime = LocalDateTime.now().minusDays(days);
            int deletedCount = operationLogService.cleanupLogsBefore(beforeTime);
            
            model.addAttribute("successMessage", 
                    String.format("成功清理了 %d 条 %d 天前的操作日志", deletedCount, days));
        } catch (Exception e) {
            model.addAttribute("errorMessage", "清理操作日志失败：" + e.getMessage());
        }
        
        // 重新获取统计信息
        int totalCount = operationLogService.getTotalCount();
        model.addAttribute("totalCount", totalCount);
        
        return "admin/logs/cleanup";
    }
    
    /**
     * 检查是否有搜索条件
     */
    private boolean hasSearchConditions(String username, String operationType, String module, 
                                       String result, LocalDateTime startTime, LocalDateTime endTime) {
        return (username != null && !username.trim().isEmpty()) ||
               (operationType != null && !operationType.trim().isEmpty()) ||
               (module != null && !module.trim().isEmpty()) ||
               (result != null && !result.trim().isEmpty()) ||
               startTime != null || endTime != null;
    }
}