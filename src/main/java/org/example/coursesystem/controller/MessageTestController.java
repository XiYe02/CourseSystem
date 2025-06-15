package org.example.coursesystem.controller;

import org.example.coursesystem.message.LogMessage;
import org.example.coursesystem.message.MessageProducerService;
import org.example.coursesystem.message.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 消息队列测试控制器
 * 用于测试ActiveMQ消息发送和接收功能
 */
@Controller
@RequestMapping("/admin/message-test")
@PreAuthorize("hasRole('ADMIN')")
public class MessageTestController {
    
    @Autowired
    private MessageProducerService messageProducerService;
    
    /**
     * 显示消息测试页面
     */
    @GetMapping
    public String showTestPage() {
        return "admin/message-test";
    }
    
    /**
     * 测试发送日志消息
     */
    @PostMapping("/send-log")
    @ResponseBody
    public String sendLogMessage(@RequestParam String username,
                                @RequestParam String operation,
                                @RequestParam String module) {
        try {
            // 创建LogMessage对象并发送
            LogMessage logMessage = new LogMessage();
            logMessage.setUsername(username);
            logMessage.setOperationType(operation);
            logMessage.setModule(module);
            logMessage.setDescription("测试日志消息");
            logMessage.setResult("SUCCESS");
            logMessage.setOperationTime(LocalDateTime.now());
            
            messageProducerService.sendLogMessage(logMessage);
            return "日志消息发送成功";
        } catch (Exception e) {
            return "日志消息发送失败: " + e.getMessage();
        }
    }
    
    /**
     * 测试发送通知消息
     */
    @PostMapping("/send-notification")
    @ResponseBody
    public String sendNotificationMessage(@RequestParam String message) {
        try {
            messageProducerService.sendNotificationMessage(message);
            return "通知消息发送成功";
        } catch (Exception e) {
            return "通知消息发送失败: " + e.getMessage();
        }
    }
    
    /**
     * 测试发送结构化通知消息
     */
    @PostMapping("/send-structured-notification")
    @ResponseBody
    public String sendStructuredNotification(@RequestParam String title,
                                           @RequestParam String content,
                                           @RequestParam String type,
                                           @RequestParam String targetUser,
                                           @RequestParam(defaultValue = "false") boolean urgent) {
        try {
            NotificationMessage notification = new NotificationMessage();
            notification.setId(UUID.randomUUID().toString());
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notification.setTargetUser(targetUser);
            notification.setModule("TEST");
            notification.setUrgent(urgent);
            notification.setCreateTime(LocalDateTime.now());
            notification.setExpireTime(LocalDateTime.now().plusDays(7));
            
            messageProducerService.sendNotificationMessage(notification);
            return "结构化通知消息发送成功";
        } catch (Exception e) {
            return "结构化通知消息发送失败: " + e.getMessage();
        }
    }
    
    /**
     * 测试发送选课成功通知
     */
    @PostMapping("/send-course-success")
    @ResponseBody
    public String sendCourseSuccessNotification(@RequestParam String username,
                                              @RequestParam String courseName) {
        try {
            messageProducerService.sendCourseSelectionSuccessNotification(username, courseName);
            return "选课成功通知发送成功";
        } catch (Exception e) {
            return "选课成功通知发送失败: " + e.getMessage();
        }
    }
    
    /**
     * 测试发送选课失败通知
     */
    @PostMapping("/send-course-failure")
    @ResponseBody
    public String sendCourseFailureNotification(@RequestParam String username,
                                               @RequestParam String courseName,
                                               @RequestParam String reason) {
        try {
            messageProducerService.sendCourseSelectionFailureNotification(username, courseName, reason);
            return "选课失败通知发送成功";
        } catch (Exception e) {
            return "选课失败通知发送失败: " + e.getMessage();
        }
    }
    
    /**
     * 测试发送自定义队列消息
     */
    @PostMapping("/send-custom")
    @ResponseBody
    public String sendCustomMessage(@RequestParam String queueName,
                                   @RequestParam String message) {
        try {
            messageProducerService.sendMessage(queueName, message);
            return "自定义消息发送成功";
        } catch (Exception e) {
            return "自定义消息发送失败: " + e.getMessage();
        }
    }
}