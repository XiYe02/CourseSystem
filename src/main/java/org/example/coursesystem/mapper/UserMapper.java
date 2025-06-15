package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问层接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(@Param("id") Long id);
    
    /**
     * 插入新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);
    
    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();
    
    /**
     * 根据角色查询用户
     * @param role 用户角色
     * @return 用户列表
     */
    List<User> findByRole(@Param("role") String role);
    
    /**
     * 根据状态查询用户
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> findByStatus(@Param("status") String status);
    
    /**
     * 更新用户最后登录时间
     * @param username 用户名
     * @param lastLoginTime 最后登录时间
     * @return 影响行数
     */
    int updateLastLoginTime(@Param("username") String username, @Param("lastLoginTime") LocalDateTime lastLoginTime);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，否则返回false
     */
    boolean existsByUsername(@Param("username") String username);
    
    /**
     * 更新用户密码
     * @param username 用户名
     * @param newPassword 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);
    
    /**
     * 更新用户状态
     * @param username 用户名
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("username") String username, @Param("status") String status);
}