package org.example.coursesystem.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.coursesystem.entity.OperationLog;
import org.example.coursesystem.entity.User;
import org.example.coursesystem.message.LogMessage;
import org.example.coursesystem.message.MessageProducerService;
import org.example.coursesystem.security.CustomUserDetails;
import org.example.coursesystem.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面类
 * 使用AOP技术自动记录用户操作日志
 */
@Aspect// 切面注解
@Component// 组件注解，将切面类加入IOC容器
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private MessageProducerService messageProducerService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 定义切点：拦截所有Controller层的方法
     */
    @Pointcut("execution(* org.example.coursesystem.controller..*.*(..))")
    public void controllerMethods() {}
    
    /**
     * 定义切点：拦截带有@LogOperation注解的方法
     */
    @Pointcut("@annotation(org.example.coursesystem.aspect.LogOperation)")
    public void logOperationMethods() {}
    
    /**
     * 环绕通知：记录操作日志
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常
     */
    @Around("controllerMethods() || logOperationMethods()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        LocalDateTime operationTime = LocalDateTime.now();
        
        // 获取请求信息
        HttpServletRequest request = getHttpServletRequest();
        
        // 获取当前用户信息
        User currentUser = getCurrentUser();
        
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取注解信息
        LogOperation logOperation = method.getAnnotation(LogOperation.class);
        
        // 构建操作日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationTime(operationTime);
        
        // 设置用户信息
        if (currentUser != null) {
            operationLog.setUserId(currentUser.getId());
            operationLog.setUsername(currentUser.getUsername());
        }
        
        // 设置请求信息
        if (request != null) {
            operationLog.setMethod(request.getMethod());
            operationLog.setRequestUrl(request.getRequestURL().toString());
            operationLog.setClientIp(getClientIpAddress(request));
            
            // 记录请求参数
            if (logOperation == null || logOperation.logParams()) {
                operationLog.setRequestParams(getRequestParams(request, joinPoint));
            }
        }
        
        // 设置操作信息
        if (logOperation != null) {
            operationLog.setOperationType(logOperation.operationType());
            operationLog.setOperationDescription(logOperation.description());
            operationLog.setModule(logOperation.module());
        } else {
            // 如果没有注解，根据方法名和类名自动生成
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            operationLog.setOperationType(getOperationTypeByMethodName(methodName));
            operationLog.setOperationDescription(className + "." + methodName);
            operationLog.setModule(getModuleByClassName(className));
        }
        
        Object result = null;
        String errorMessage = null;
        
        try {
            // 执行目标方法
            result = joinPoint.proceed();
            operationLog.setResult("SUCCESS");
            
            // 记录返回结果（如果需要）
            if (logOperation != null && logOperation.logResult() && result != null) {
                try {
                    String resultJson = objectMapper.writeValueAsString(result);
                    // 限制结果长度，避免过长
                    if (resultJson.length() > 1000) {
                        resultJson = resultJson.substring(0, 1000) + "...";
                    }
                    operationLog.setErrorMessage("Result: " + resultJson);
                } catch (Exception e) {
                    logger.warn("序列化返回结果失败: {}", e.getMessage());
                }
            }
            
        } catch (Throwable throwable) {
            operationLog.setResult("FAILURE");
            errorMessage = throwable.getMessage();
            if (errorMessage != null && errorMessage.length() > 500) {
                errorMessage = errorMessage.substring(0, 500) + "...";
            }
            operationLog.setErrorMessage(errorMessage);
            throw throwable;
        } finally {
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            operationLog.setExecutionTime(endTime - startTime);
            operationLog.setCreateTime(LocalDateTime.now());
            
            // 通过消息队列异步保存操作日志
            try {
                // 创建日志消息对象
                LogMessage logMessage = new LogMessage(
                    operationLog.getUserId(),
                    operationLog.getUsername(),
                    operationLog.getOperationType(),
                    operationLog.getOperationDescription(),
                    operationLog.getModule(),
                    operationLog.getMethod(),
                    operationLog.getRequestUrl(),
                    operationLog.getClientIp(),
                    operationLog.getRequestParams(),
                    operationLog.getResult(),
                    operationLog.getErrorMessage(),
                    operationLog.getExecutionTime(),
                    operationLog.getOperationTime()
                );
                
                // 发送日志消息到队列
                messageProducerService.sendLogMessage(logMessage);
                
            } catch (Exception e) {
                logger.error("发送日志消息失败，回退到直接保存: {}", e.getMessage(), e);
                // 如果消息队列发送失败，回退到直接保存
                try {
                    operationLogService.saveOperationLogAsync(operationLog);
                } catch (Exception ex) {
                    logger.error("直接保存操作日志也失败: {}", ex.getMessage(), ex);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 获取HttpServletRequest对象
     * @return HttpServletRequest对象
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            logger.warn("获取HttpServletRequest失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 获取当前登录用户
     * @return 当前用户
     */
    private User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() 
                && !"anonymousUser".equals(authentication.getPrincipal())) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomUserDetails) {
                    return ((CustomUserDetails) principal).getUser();
                } else if (principal instanceof User) {
                    return (User) principal;
                }
            }
        } catch (Exception e) {
            logger.warn("获取当前用户失败: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", 
                           "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 多级代理的情况，第一个IP为客户端真实IP
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 获取请求参数
     * @param request HTTP请求
     * @param joinPoint 连接点
     * @return 请求参数JSON字符串
     */
    private String getRequestParams(HttpServletRequest request, JoinPoint joinPoint) {
        try {
            Map<String, Object> params = new HashMap<>();
            
            // 获取URL参数
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    params.put(paramName, paramValues[0]);
                } else {
                    params.put(paramName, Arrays.asList(paramValues));
                }
            }
            
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            
            for (int i = 0; i < args.length && i < paramNames.length; i++) {
                Object arg = args[i];
                // 过滤掉HttpServletRequest、HttpServletResponse等对象
                if (arg != null && !isFilteredType(arg.getClass())) {
                    params.put(paramNames[i], arg);
                }
            }
            
            String paramsJson = objectMapper.writeValueAsString(params);
            // 限制参数长度
            if (paramsJson.length() > 2000) {
                paramsJson = paramsJson.substring(0, 2000) + "...";
            }
            return paramsJson;
            
        } catch (Exception e) {
            logger.warn("获取请求参数失败: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * 判断是否为需要过滤的类型
     * @param clazz 类型
     * @return 是否需要过滤
     */
    private boolean isFilteredType(Class<?> clazz) {
        String className = clazz.getName();
        return className.startsWith("jakarta.servlet") || 
               className.startsWith("javax.servlet") ||
               className.startsWith("org.springframework.web") ||
               className.startsWith("org.springframework.ui");
    }
    
    /**
     * 根据方法名获取操作类型
     * @param methodName 方法名
     * @return 操作类型
     */
    private String getOperationTypeByMethodName(String methodName) {
        String lowerMethodName = methodName.toLowerCase();
        
        if (lowerMethodName.contains("add") || lowerMethodName.contains("create") || 
            lowerMethodName.contains("insert") || lowerMethodName.contains("save")) {
            return "CREATE";
        } else if (lowerMethodName.contains("update") || lowerMethodName.contains("edit") || 
                   lowerMethodName.contains("modify")) {
            return "UPDATE";
        } else if (lowerMethodName.contains("delete") || lowerMethodName.contains("remove")) {
            return "DELETE";
        } else if (lowerMethodName.contains("select") || lowerMethodName.contains("get") || 
                   lowerMethodName.contains("find") || lowerMethodName.contains("list") ||
                   lowerMethodName.contains("query") || lowerMethodName.contains("search")) {
            return "SELECT";
        } else if (lowerMethodName.contains("login")) {
            return "LOGIN";
        } else if (lowerMethodName.contains("logout")) {
            return "LOGOUT";
        } else if (lowerMethodName.contains("export")) {
            return "EXPORT";
        } else if (lowerMethodName.contains("import")) {
            return "IMPORT";
        } else {
            return "OTHER";
        }
    }
    
    /**
     * 根据类名获取模块名称
     * @param className 类名
     * @return 模块名称
     */
    private String getModuleByClassName(String className) {
        String lowerClassName = className.toLowerCase();
        
        if (lowerClassName.contains("student")) {
            return "学生管理";
        } else if (lowerClassName.contains("course")) {
            return "课程管理";
        } else if (lowerClassName.contains("selection")) {
            return "选课管理";
        } else if (lowerClassName.contains("user")) {
            return "用户管理";
        } else if (lowerClassName.contains("auth") || lowerClassName.contains("login")) {
            return "认证授权";
        } else if (lowerClassName.contains("log")) {
            return "日志管理";
        } else if (lowerClassName.contains("export")) {
            return "数据导出";
        } else {
            return "系统管理";
        }
    }
}