package org.example.coursesystem.controller;

import org.example.coursesystem.entity.User;
import org.example.coursesystem.security.CustomUserDetails;
import org.example.coursesystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

/**
 * 认证控制器
 */
@Controller
public class AuthController {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // 用户已登录，根据角色重定向
            String role = auth.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/admin/dashboard";
            } else if ("ROLE_STUDENT".equals(role)) {
                return "redirect:/student/dashboard";
            }
        }
        return "redirect:/login";
    }
    
    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model, HttpSession session) {
        
        // 检查用户是否已登录
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }
        
        if (error != null) {
            String errorMessage = (String) session.getAttribute("loginError");
            if (errorMessage != null) {
                model.addAttribute("error", errorMessage);
                session.removeAttribute("loginError");
            } else {
                model.addAttribute("error", "用户名或密码错误");
            }
        }
        
        if (logout != null) {
            model.addAttribute("message", "您已成功退出登录");
        }
        
        return "login";
    }
    
    /**
     * 管理员仪表板
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("realName", userDetails.getRealName());
        
        return "admin/dashboard";
    }
    
    /**
     * 学生仪表板
     */
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("realName", userDetails.getRealName());
        
        return "student/dashboard";
    }
    
    /**
     * 通用仪表板（备用）
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("realName", userDetails.getRealName());
        
        // 根据角色重定向到对应的仪表板
        if (userDetails.isAdmin()) {
            return "redirect:/admin/dashboard";
        } else if (userDetails.isStudent()) {
            return "redirect:/student/dashboard";
        }
        
        return "dashboard";
    }
    
    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String register(Model model) {
        // 检查用户是否已登录
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }
        
        model.addAttribute("user", new User());
        return "register";
    }
    
    /**
     * 处理注册请求
     */
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") User user,
                                 BindingResult bindingResult,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        
        // 验证表单数据
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        // 验证密码确认
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }
        
        // 检查用户名是否已存在
        if (userDetailsService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "用户名已存在，请选择其他用户名");
            return "register";
        }
        
        try {
            // 设置默认值
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("STUDENT"); // 默认注册为学生角色
            user.setStatus("ACTIVE");
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            
            // 保存用户
            userDetailsService.registerUser(user);
            
            redirectAttributes.addFlashAttribute("success", "注册成功！请使用您的账号登录。");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("error", "注册失败：" + e.getMessage());
            return "register";
        }
    }
    
    /**
     * 访问拒绝页面
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}