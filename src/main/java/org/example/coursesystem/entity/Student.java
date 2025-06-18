package org.example.coursesystem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
public class Student {
    private Long id;
    
    @NotBlank(message = "学号不能为空")// 非空校验注解，用于验证学号不能为空
    @Size(max = 20, message = "学号长度不能超过20个字符")// 长度校验注解，用于验证学号长度不能超过20个字符
    private String studentNumber;
    
    @NotBlank(message = "姓名不能为空")// 非空校验注解，用于验证姓名不能为空
    @Size(max = 50, message = "姓名长度不能超过50个字符")// 长度校验注解，用于验证姓名长度不能超过50个字符
    private String name;
    
    @Pattern(regexp = "MALE|FEMALE", message = "性别只能是MALE或FEMALE")// 正则表达式校验注解，用于验证性别只能是MALE或FEMALE
    private String gender;
    
    private LocalDate birthDate;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;
    
    @Size(max = 100, message = "专业长度不能超过100个字符")
    private String major;
    
    private Integer grade;
    
    @Size(max = 50, message = "班级名称长度不能超过50个字符")
    private String className;
    
    private LocalDate enrollmentDate;
    
    @Pattern(regexp = "ACTIVE|INACTIVE|GRADUATED", message = "学生状态只能是ACTIVE、INACTIVE或GRADUATED")
    private String status;
    
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    // 构造函数
    public Student() {
        this.status = "ACTIVE";
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public Integer getGrade() {
        return grade;
    }
    
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}