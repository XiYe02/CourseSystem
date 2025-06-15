package org.example.coursesystem.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    
    /**
     * 操作类型
     * @return 操作类型
     */
    String operationType() default "";
    
    /**
     * 操作描述
     * @return 操作描述
     */
    String description() default "";
    
    /**
     * 模块名称
     * @return 模块名称
     */
    String module() default "";
    
    /**
     * 是否记录请求参数
     * @return 是否记录请求参数
     */
    boolean logParams() default true;
    
    /**
     * 是否记录返回结果
     * @return 是否记录返回结果
     */
    boolean logResult() default false;
}