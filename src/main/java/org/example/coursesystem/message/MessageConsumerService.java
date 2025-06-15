package org.example.coursesystem.message;

import org.example.coursesystem.entity.OperationLog;
import org.example.coursesystem.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 消息消费者服务
 * 负责监听ActiveMQ队列并处理接收到的消息
 */
@Service
public class MessageConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);
    
    @Autowired
    private OperationLogService operationLogService;
    
    /**
     * 监听日志队列，处理日志消息
     * @param logMessage 接收到的日志消息
     */
    @JmsListener(destination = MessageProducerService.LOG_QUEUE)
    public void handleLogMessage(LogMessage logMessage) {
        try {
            logger.debug("接收到日志消息: {}", logMessage);
            
            // 将LogMessage转换为OperationLog
            OperationLog operationLog = new OperationLog();
            operationLog.setUserId(logMessage.getUserId());
            operationLog.setUsername(logMessage.getUsername());
            operationLog.setOperationType(logMessage.getOperationType());
            operationLog.setOperationDescription(logMessage.getDescription());
            operationLog.setModule(logMessage.getModule());
            operationLog.setMethod(logMessage.getMethod());
            operationLog.setRequestUrl(logMessage.getUrl());
            operationLog.setClientIp(logMessage.getIp());
            operationLog.setRequestParams(logMessage.getParams());
            operationLog.setResult(logMessage.getResult());
            operationLog.setErrorMessage(logMessage.getErrorMessage());
            operationLog.setExecutionTime(logMessage.getExecutionTime());
            operationLog.setOperationTime(logMessage.getOperationTime());
            operationLog.setCreateTime(LocalDateTime.now());
            
            // 保存日志到数据库
            if (operationLogService.saveOperationLog(operationLog)) {
                logger.info("日志消息处理成功: {}", logMessage.getOperationType());
            } else {
                logger.error("日志消息处理失败: {}", logMessage.getOperationType());
            }
            
        } catch (Exception e) {
            logger.error("处理日志消息失败: {}", e.getMessage(), e);
            // 这里可以实现重试机制或将失败的消息发送到死信队列
        }
    }
    
    /**
     * 监听通知队列，处理字符串通知消息
     * @param message 接收到的通知消息
     */
    @JmsListener(destination = MessageProducerService.NOTIFICATION_QUEUE, 
                 selector = "JMSType = 'java.lang.String'")
    public void handleNotificationMessage(String message) {
        try {
            logger.debug("接收到字符串通知消息: {}", message);
            
            // 这里可以实现各种通知处理逻辑
            // 例如：发送邮件、短信、推送通知等
            processNotification(message);
            
            logger.info("字符串通知消息处理完成: {}", message);
            
        } catch (Exception e) {
            logger.error("处理字符串通知消息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 监听通知队列，处理结构化通知消息
     * @param notificationMessage 接收到的结构化通知消息
     */
    @JmsListener(destination = MessageProducerService.NOTIFICATION_QUEUE,
                 containerFactory = "jmsListenerContainerFactory")
    public void handleStructuredNotificationMessage(NotificationMessage notificationMessage) {
        try {
            logger.debug("接收到结构化通知消息: {}", notificationMessage);
            
            // 处理结构化通知消息
            processStructuredNotification(notificationMessage);
            
            logger.info("结构化通知消息处理完成: 标题={}, 类型={}, 目标用户={}", 
                       notificationMessage.getTitle(), 
                       notificationMessage.getType(), 
                       notificationMessage.getTargetUser());
            
        } catch (Exception e) {
            logger.error("处理结构化通知消息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 处理通知消息的具体逻辑
     * @param message 通知消息内容
     */
    private void processNotification(String message) {
        // 实现具体的通知处理逻辑
        // 例如：根据消息内容判断通知类型，然后执行相应的操作
        
        if (message.contains("选课成功")) {
            // 处理选课成功通知
            logger.info("处理选课成功通知: {}", message);
        } else if (message.contains("选课失败")) {
            // 处理选课失败通知
            logger.info("处理选课失败通知: {}", message);
        } else if (message.contains("系统维护")) {
            // 处理系统维护通知
            logger.info("处理系统维护通知: {}", message);
        } else {
            // 处理其他类型通知
            logger.info("处理通用通知: {}", message);
        }
    }
    
    /**
     * 处理结构化通知消息的具体逻辑
     * @param notificationMessage 结构化通知消息
     */
    private void processStructuredNotification(NotificationMessage notificationMessage) {
        String type = notificationMessage.getType();
        String title = notificationMessage.getTitle();
        String content = notificationMessage.getContent();
        String targetUser = notificationMessage.getTargetUser();
        boolean urgent = notificationMessage.isUrgent();
        
        // 根据消息类型和紧急程度处理通知
        switch (type) {
            case "SUCCESS":
                handleSuccessNotification(title, content, targetUser, urgent);
                break;
            case "ERROR":
                handleErrorNotification(title, content, targetUser, urgent);
                break;
            case "WARNING":
                handleWarningNotification(title, content, targetUser, urgent);
                break;
            case "INFO":
                handleInfoNotification(title, content, targetUser, urgent);
                break;
            default:
                logger.warn("未知的通知类型: {}", type);
                handleInfoNotification(title, content, targetUser, urgent);
        }
        
        // 如果是紧急通知，可以触发额外的处理逻辑
        if (urgent) {
            handleUrgentNotification(notificationMessage);
        }
    }
    
    /**
     * 处理成功类型通知
     */
    private void handleSuccessNotification(String title, String content, String targetUser, boolean urgent) {
        logger.info("处理成功通知 - 标题: {}, 内容: {}, 目标用户: {}, 紧急: {}", title, content, targetUser, urgent);
        // 这里可以实现具体的成功通知处理逻辑
        // 例如：发送邮件、短信、推送通知等
    }
    
    /**
     * 处理错误类型通知
     */
    private void handleErrorNotification(String title, String content, String targetUser, boolean urgent) {
        logger.error("处理错误通知 - 标题: {}, 内容: {}, 目标用户: {}, 紧急: {}", title, content, targetUser, urgent);
        // 这里可以实现具体的错误通知处理逻辑
        // 例如：发送紧急邮件、短信通知等
    }
    
    /**
     * 处理警告类型通知
     */
    private void handleWarningNotification(String title, String content, String targetUser, boolean urgent) {
        logger.warn("处理警告通知 - 标题: {}, 内容: {}, 目标用户: {}, 紧急: {}", title, content, targetUser, urgent);
        // 这里可以实现具体的警告通知处理逻辑
    }
    
    /**
     * 处理信息类型通知
     */
    private void handleInfoNotification(String title, String content, String targetUser, boolean urgent) {
        logger.info("处理信息通知 - 标题: {}, 内容: {}, 目标用户: {}, 紧急: {}", title, content, targetUser, urgent);
        // 这里可以实现具体的信息通知处理逻辑
    }
    
    /**
     * 处理紧急通知的额外逻辑
     */
    private void handleUrgentNotification(NotificationMessage notificationMessage) {
        logger.warn("处理紧急通知: {}", notificationMessage.getTitle());
        // 这里可以实现紧急通知的特殊处理逻辑
        // 例如：立即发送短信、邮件，或者触发其他紧急响应机制
    }
}