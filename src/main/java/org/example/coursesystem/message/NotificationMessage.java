package org.example.coursesystem.message;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 通知消息实体类
 * 用于在消息队列中传递系统通知信息
 */
public class NotificationMessage {
    
    private String id;
    private String title;
    private String content;
    private String type; // INFO, WARNING, ERROR, SUCCESS
    private String targetUser; // 目标用户，null表示广播
    private String targetRole; // 目标角色，null表示所有角色
    private String module; // 来源模块
    private boolean urgent; // 是否紧急
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime; // 过期时间
    
    public NotificationMessage() {
        this.createTime = LocalDateTime.now();
    }
    
    public NotificationMessage(String title, String content, String type) {
        this();
        this.title = title;
        this.content = content;
        this.type = type;
    }
    
    public NotificationMessage(String title, String content, String type, String targetUser) {
        this(title, content, type);
        this.targetUser = targetUser;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTargetUser() {
        return targetUser;
    }
    
    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }
    
    public String getTargetRole() {
        return targetRole;
    }
    
    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public boolean isUrgent() {
        return urgent;
    }
    
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
    
    @Override
    public String toString() {
        return "NotificationMessage{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", targetUser='" + targetUser + '\'' +
                ", targetRole='" + targetRole + '\'' +
                ", module='" + module + '\'' +
                ", urgent=" + urgent +
                ", createTime=" + createTime +
                ", expireTime=" + expireTime +
                '}';
    }
}