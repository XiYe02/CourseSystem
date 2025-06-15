package org.example.coursesystem.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
public class OperationLog {
    
    private Long id;
    
    private Long userId;
    
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    private String username;
    
    @NotBlank(message = "操作类型不能为空")
    @Size(max = 20, message = "操作类型长度不能超过20个字符")
    private String operationType;
    
    @Size(max = 200, message = "操作描述长度不能超过200个字符")
    private String operationDescription;
    
    @Size(max = 50, message = "模块名称长度不能超过50个字符")
    private String module;
    
    @Size(max = 10, message = "请求方法长度不能超过10个字符")
    private String method;
    
    @Size(max = 500, message = "请求URL长度不能超过500个字符")
    private String requestUrl;
    
    private String requestParams;
    
    @Size(max = 50, message = "客户端IP长度不能超过50个字符")
    private String clientIp;
    
    @NotNull(message = "操作时间不能为空")
    private LocalDateTime operationTime;
    
    @NotBlank(message = "操作结果不能为空")
    @Size(max = 10, message = "操作结果长度不能超过10个字符")
    private String result;
    
    private String errorMessage;
    
    private Long executionTime;
    
    @NotNull(message = "创建时间不能为空")
    private LocalDateTime createTime;
    
    // 构造函数
    public OperationLog() {
    }
    
    public OperationLog(Long userId, String username, String operationType, 
                       String operationDescription, String module, String method, 
                       String requestUrl, String requestParams, String clientIp, 
                       LocalDateTime operationTime, String result, 
                       String errorMessage, Long executionTime) {
        this.userId = userId;
        this.username = username;
        this.operationType = operationType;
        this.operationDescription = operationDescription;
        this.module = module;
        this.method = method;
        this.requestUrl = requestUrl;
        this.requestParams = requestParams;
        this.clientIp = clientIp;
        this.operationTime = operationTime;
        this.result = result;
        this.errorMessage = errorMessage;
        this.executionTime = executionTime;
        this.createTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getOperationDescription() {
        return operationDescription;
    }
    
    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
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
    
    public String getRequestUrl() {
        return requestUrl;
    }
    
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    
    public String getRequestParams() {
        return requestParams;
    }
    
    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public LocalDateTime getOperationTime() {
        return operationTime;
    }
    
    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
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
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "OperationLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", operationType='" + operationType + '\'' +
                ", operationDescription='" + operationDescription + '\'' +
                ", module='" + module + '\'' +
                ", method='" + method + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", operationTime=" + operationTime +
                ", result='" + result + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", executionTime=" + executionTime +
                ", createTime=" + createTime +
                '}';
    }
}