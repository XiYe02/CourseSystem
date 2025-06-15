package org.example.coursesystem.security;

import org.example.coursesystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义用户详情类，实现Spring Security的UserDetails接口
 */
public class CustomUserDetails implements UserDetails {
    
    private final User user;
    
    public CustomUserDetails(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 根据用户角色返回权限
        String role = "ROLE_" + user.getRole();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }
    
    @Override
    public boolean isAccountNonLocked() {
        // 根据用户状态判断账户是否被锁定
        return !"LOCKED".equals(user.getStatus());
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }
    
    @Override
    public boolean isEnabled() {
        // 根据用户状态判断账户是否启用
        return "ACTIVE".equals(user.getStatus());
    }
    
    /**
     * 获取用户实体
     * @return 用户实体
     */
    public User getUser() {
        return user;
    }
    
    /**
     * 获取用户真实姓名
     * @return 真实姓名
     */
    public String getRealName() {
        return user.getRealName();
    }
    
    /**
     * 获取用户角色
     * @return 用户角色
     */
    public String getRole() {
        return user.getRole();
    }
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Long getUserId() {
        return user.getId();
    }
    
    /**
     * 判断是否为管理员
     * @return 是否为管理员
     */
    public boolean isAdmin() {
        return "ADMIN".equals(user.getRole());
    }
    
    /**
     * 判断是否为学生
     * @return 是否为学生
     */
    public boolean isStudent() {
        return "STUDENT".equals(user.getRole());
    }
}