package org.example.coursesystem.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息生产者服务
 * 负责向ActiveMQ发送各种类型的消息
 */
@Service
public class MessageProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageProducerService.class);
    
    // 队列名称常量
    public static final String LOG_QUEUE = "course.system.log.queue";
    public static final String NOTIFICATION_QUEUE = "course.system.notification.queue";
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    /**
     * 发送日志消息到队列
     * @param logMessage 日志消息对象
     */
    public void sendLogMessage(LogMessage logMessage) {
        try {
            jmsTemplate.convertAndSend(LOG_QUEUE, logMessage);
            logger.debug("日志消息已发送到队列: {}", logMessage);
        } catch (Exception e) {
            logger.error("发送日志消息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送通知消息到队列
     * @param message 通知消息内容
     */
    public void sendNotificationMessage(String message) {
        try {
            jmsTemplate.convertAndSend(NOTIFICATION_QUEUE, message);
            logger.debug("通知消息已发送到队列: {}", message);
        } catch (Exception e) {
            logger.error("发送通知消息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送结构化通知消息到队列
     * @param notificationMessage 通知消息对象
     */
    public void sendNotificationMessage(NotificationMessage notificationMessage) {
        try {
            jmsTemplate.convertAndSend(NOTIFICATION_QUEUE, notificationMessage);
            logger.debug("结构化通知消息已发送到队列: {}", notificationMessage);
        } catch (Exception e) {
            logger.error("发送结构化通知消息失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 发送选课成功通知
     * @param username 学生用户名
     * @param courseName 课程名称
     */
    public void sendCourseSelectionSuccessNotification(String username, String courseName) {
        NotificationMessage notification = new NotificationMessage(
            "选课成功",
            String.format("您已成功选择课程：%s", courseName),
            "SUCCESS",
            username
        );
        notification.setModule("选课系统");
        sendNotificationMessage(notification);
    }
    
    /**
     * 发送选课失败通知
     * @param username 学生用户名
     * @param courseName 课程名称
     * @param reason 失败原因
     */
    public void sendCourseSelectionFailureNotification(String username, String courseName, String reason) {
        NotificationMessage notification = new NotificationMessage(
            "选课失败",
            String.format("选择课程 %s 失败：%s", courseName, reason),
            "ERROR",
            username
        );
        notification.setModule("选课系统");
        notification.setUrgent(true);
        sendNotificationMessage(notification);
    }
    
    /**
     * 发送消息到指定队列
     * @param queueName 队列名称
     * @param message 消息对象
     */
    public void sendMessage(String queueName, Object message) {
        try {
            jmsTemplate.convertAndSend(queueName, message);
            logger.debug("消息已发送到队列 {}: {}", queueName, message);
        } catch (Exception e) {
            logger.error("发送消息到队列 {} 失败: {}", queueName, e.getMessage(), e);
        }
    }
}