package org.example.coursesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseSystemApplication.class, args);
        System.out.println("\n" +
                "========================================\n" +
                "  学生选课信息管理系统启动成功！\n" +
                "  访问地址: http://localhost:8080");

    }

}
