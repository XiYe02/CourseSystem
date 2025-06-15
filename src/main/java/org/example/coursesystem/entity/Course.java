package org.example.coursesystem.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体类
 */
public class Course {
    private Long id;
    
    @NotBlank(message = "课程代码不能为空")
    @Size(max = 20, message = "课程代码长度不能超过20个字符")
    private String courseCode;
    
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称长度不能超过100个字符")
    private String courseName;
    
    @NotNull(message = "学分不能为空")
    @DecimalMin(value = "0.5", message = "学分不能少于0.5")
    private BigDecimal credits;
    
    @NotNull(message = "学时不能为空")
    @Min(value = 1, message = "学时不能少于1")
    private Integer hours;
    
    @NotBlank(message = "课程类型不能为空")
    @Pattern(regexp = "REQUIRED|ELECTIVE|PUBLIC", message = "课程类型只能是REQUIRED、ELECTIVE或PUBLIC")
    private String courseType;
    
    @Size(max = 100, message = "开课院系长度不能超过100个字符")
    private String department;
    
    @Size(max = 50, message = "授课教师长度不能超过50个字符")
    private String teacherName;
    
    @Size(max = 50, message = "教师职称长度不能超过50个字符")
    private String teacherTitle;
    
    @Min(value = 1, message = "最大选课人数不能少于1")
    private Integer maxStudents;
    
    private String description;
    
    @Size(max = 500, message = "先修课程长度不能超过500个字符")
    private String prerequisites;
    
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "课程状态只能是ACTIVE或INACTIVE")
    private String status;
    
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    // 构造函数
    public Course() {
        this.status = "ACTIVE";
        this.maxStudents = 100;
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
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public BigDecimal getCredits() {
        return credits;
    }
    
    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }
    
    public Integer getHours() {
        return hours;
    }
    
    public void setHours(Integer hours) {
        this.hours = hours;
    }
    
    public String getCourseType() {
        return courseType;
    }
    
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    public String getTeacherTitle() {
        return teacherTitle;
    }
    
    public void setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
    }
    
    public Integer getMaxStudents() {
        return maxStudents;
    }
    
    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPrerequisites() {
        return prerequisites;
    }
    
    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
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
    
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", hours=" + hours +
                ", courseType='" + courseType + '\'' +
                ", department='" + department + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", teacherTitle='" + teacherTitle + '\'' +
                ", maxStudents=" + maxStudents +
                ", status='" + status + '\'' +
                '}';
    }
}