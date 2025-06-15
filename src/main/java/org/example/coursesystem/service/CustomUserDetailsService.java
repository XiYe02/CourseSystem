package org.example.coursesystem.service;

import org.example.coursesystem.entity.User;
import org.example.coursesystem.mapper.UserMapper;
import org.example.coursesystem.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 自定义用户详情服务类，实现Spring Security的UserDetailsService接口
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查找用户
        User user = userMapper.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在: " + username);
        }
        
        // 检查用户状态
        if ("INACTIVE".equals(user.getStatus())) {
            throw new UsernameNotFoundException("账户未激活: " + username);
        }
        
        if ("LOCKED".equals(user.getStatus())) {
            throw new UsernameNotFoundException("账户已被锁定: " + username);
        }
        
        // 返回自定义的UserDetails实现
        return new CustomUserDetails(user);
    }
    
    /**
     * 更新用户最后登录时间
     * @param username 用户名
     */
    public void updateLastLoginTime(String username) {
        try {
            userMapper.updateLastLoginTime(username, LocalDateTime.now());
        } catch (Exception e) {
            // 记录日志，但不影响登录流程
            System.err.println("更新最后登录时间失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，否则返回false
     */
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }
    
    /**
     * 注册新用户
     * @param user 用户信息
     * @return 注册结果
     */
    public boolean registerUser(User user) {
        try {
            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            throw new RuntimeException("用户注册失败: " + e.getMessage(), e);
        }
    }
}