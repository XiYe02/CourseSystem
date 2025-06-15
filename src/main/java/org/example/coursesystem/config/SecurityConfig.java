package org.example.coursesystem.config;

import org.example.coursesystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 认证提供者
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * 登录成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // 更新最后登录时间
            String username = authentication.getName();
            userDetailsService.updateLastLoginTime(username);
            
            // 根据用户角色重定向到不同页面
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect("/admin/dashboard");
            } else if ("ROLE_STUDENT".equals(role)) {
                response.sendRedirect("/student/dashboard");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }
    
    /**
     * 登录失败处理器
     */
    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            String errorMessage = "登录失败";
            if (exception.getMessage().contains("用户名不存在")) {
                errorMessage = "用户名不存在";
            } else if (exception.getMessage().contains("密码错误")) {
                errorMessage = "密码错误";
            } else if (exception.getMessage().contains("账户未激活")) {
                errorMessage = "账户未激活";
            } else if (exception.getMessage().contains("账户已被锁定")) {
                errorMessage = "账户已被锁定";
            }
            
            request.getSession().setAttribute("loginError", errorMessage);
            response.sendRedirect("/login?error");
        };
    }
    
    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（在开发阶段，生产环境建议启用）
            .csrf(csrf -> csrf.disable())
            
            // 配置授权规则
            .authorizeHttpRequests(authz -> authz
                // 公开访问的路径
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // 管理员专用路径
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 学生专用路径
                .requestMatchers("/student/**").hasRole("STUDENT")
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            
            // 配置登录
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .permitAll()
            )
            
            // 配置登出
            .logout(logout -> logout
                .logoutRequestMatcher(AntPathRequestMatcher.antMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            
            // 配置会话管理
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            
            // 配置认证提供者
            .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}