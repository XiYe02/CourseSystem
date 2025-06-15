package org.example.coursesystem.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 选课信息实体类
 */
public class CourseSelection {
    private Long id;
    
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    
    @NotNull(message = "学期ID不能为空")
    private Long semesterId;
    
    private LocalDateTime selectionTime;
    
    @Pattern(regexp = "SELECTED|DROPPED|COMPLETED", message = "选课状态只能是SELECTED、DROPPED或COMPLETED")
    private String status;
    
    @DecimalMin(value = "0.0", message = "成绩不能小于0")
    @DecimalMax(value = "100.0", message = "成绩不能大于100")
    private BigDecimal score;
    
    @DecimalMin(value = "0.0", message = "绩点不能小于0")
    @DecimalMax(value = "4.0", message = "绩点不能大于4.0")
    private BigDecimal gradePoint;
    
    private LocalDateTime examTime;
    private String remarks;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    // 关联对象（用于显示）
    private Student student;
    private Course course;
    private Semester semester;
    
    // 构造函数
    public CourseSelection() {
        this.status = "SELECTED";
        this.selectionTime = LocalDateTime.now();
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
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public Long getSemesterId() {
        return semesterId;
    }
    
    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }
    
    public LocalDateTime getSelectionTime() {
        return selectionTime;
    }
    
    public void setSelectionTime(LocalDateTime selectionTime) {
        this.selectionTime = selectionTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getScore() {
        return score;
    }
    
    public void setScore(BigDecimal score) {
        this.score = score;
    }
    
    public BigDecimal getGradePoint() {
        return gradePoint;
    }
    
    public void setGradePoint(BigDecimal gradePoint) {
        this.gradePoint = gradePoint;
    }
    
    public LocalDateTime getExamTime() {
        return examTime;
    }
    
    public void setExamTime(LocalDateTime examTime) {
        this.examTime = examTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Semester getSemester() {
        return semester;
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
    }
    
    @Override
    public String toString() {
        return "CourseSelection{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", semesterId=" + semesterId +
                ", selectionTime=" + selectionTime +
                ", status='" + status + '\'' +
                ", score=" + score +
                ", gradePoint=" + gradePoint +
                '}';
    }
}