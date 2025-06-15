package org.example.coursesystem.message;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 日志消息实体类
 * 用于在消息队列中传递操作日志信息
 */
public class LogMessage {
    
    private Long userId;
    private String username;
    private String operationType;
    private String description;
    private String module;
    private String method;
    private String url;
    private String ip;
    private String params;
    private String result;
    private String errorMessage;
    private Long executionTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
    
    public LogMessage() {}
    
    public LogMessage(Long userId, String username, String operationType, String description, 
                     String module, String method, String url, String ip, String params, 
                     String result, String errorMessage, Long executionTime, LocalDateTime operationTime) {
        this.userId = userId;
        this.username = username;
        this.operationType = operationType;
        this.description = description;
        this.module = module;
        this.method = method;
        this.url = url;
        this.ip = ip;
        this.params = params;
        this.result = result;
        this.errorMessage = errorMessage;
        this.executionTime = executionTime;
        this.operationTime = operationTime;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getParams() {
        return params;
    }
    
    public void setParams(String params) {
        this.params = params;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }
    
    public LocalDateTime getOperationTime() {
        return operationTime;
    }
    
    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }
    
    @Override
    public String toString() {
        return "LogMessage{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", operationType='" + operationType + '\'' +
                ", description='" + description + '\'' +
                ", module='" + module + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                ", result='" + result + '\'' +
                ", executionTime=" + executionTime +
                ", operationTime=" + operationTime +
                '}';
    }
}