package org.example.coursesystem.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学期实体类
 */
public class Semester {
    private Long id;
    
    @NotBlank(message = "学期代码不能为空")
    @Size(max = 20, message = "学期代码长度不能超过20个字符")
    private String semesterCode;
    
    @NotBlank(message = "学期名称不能为空")
    @Size(max = 50, message = "学期名称长度不能超过50个字符")
    private String semesterName;
    
    @NotBlank(message = "学年不能为空")
    @Size(max = 20, message = "学年长度不能超过20个字符")
    private String academicYear;
    
    @NotBlank(message = "学期类型不能为空")
    @Pattern(regexp = "SPRING|SUMMER|AUTUMN|WINTER", message = "学期类型只能是SPRING、SUMMER、AUTUMN或WINTER")
    private String semesterType;
    
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;
    
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;
    
    private LocalDate registrationStartDate;
    private LocalDate registrationEndDate;
    
    @Pattern(regexp = "UPCOMING|CURRENT|FINISHED", message = "学期状态只能是UPCOMING、CURRENT或FINISHED")
    private String status;
    
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    // 构造函数
    public Semester() {
        this.status = "UPCOMING";
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
    
    public String getSemesterCode() {
        return semesterCode;
    }
    
    public void setSemesterCode(String semesterCode) {
        this.semesterCode = semesterCode;
    }
    
    public String getSemesterName() {
        return semesterName;
    }
    
    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    public String getSemesterType() {
        return semesterType;
    }
    
    public void setSemesterType(String semesterType) {
        this.semesterType = semesterType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public LocalDate getRegistrationStartDate() {
        return registrationStartDate;
    }
    
    public void setRegistrationStartDate(LocalDate registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }
    
    public LocalDate getRegistrationEndDate() {
        return registrationEndDate;
    }
    
    public void setRegistrationEndDate(LocalDate registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
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
        return "Semester{" +
                "id=" + id +
                ", semesterCode='" + semesterCode + '\'' +
                ", semesterName='" + semesterName + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", semesterType='" + semesterType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}