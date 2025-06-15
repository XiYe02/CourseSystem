package org.example.coursesystem.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.coursesystem.entity.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问层
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 插入操作日志
     * @param operationLog 操作日志对象
     * @return 影响行数
     */
    int insert(OperationLog operationLog);
    
    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志对象
     */
    OperationLog findById(@Param("id") Long id);
    
    /**
     * 查询所有操作日志
     * @return 操作日志列表
     */
    List<OperationLog> findAll();
    
    /**
     * 根据用户ID查询操作日志
     * @param userId 用户ID
     * @return 操作日志列表
     */
    List<OperationLog> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户名查询操作日志
     * @param username 用户名
     * @return 操作日志列表
     */
    List<OperationLog> findByUsername(@Param("username") String username);
    
    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLog> findByOperationType(@Param("operationType") String operationType);
    
    /**
     * 根据模块查询操作日志
     * @param module 模块名称
     * @return 操作日志列表
     */
    List<OperationLog> findByModule(@Param("module") String module);
    
    /**
     * 根据操作结果查询操作日志
     * @param result 操作结果
     * @return 操作日志列表
     */
    List<OperationLog> findByResult(@Param("result") String result);
    
    /**
     * 根据时间范围查询操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<OperationLog> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                      @Param("endTime") LocalDateTime endTime);
    
    /**
     * 多条件查询操作日志
     * @param userId 用户ID（可选）
     * @param username 用户名（可选）
     * @param operationType 操作类型（可选）
     * @param module 模块名称（可选）
     * @param result 操作结果（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 操作日志列表
     */
    List<OperationLog> findByConditions(@Param("userId") Long userId,
                                       @Param("username") String username,
                                       @Param("operationType") String operationType,
                                       @Param("module") String module,
                                       @Param("result") String result,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);
    
    /**
     * 分页查询操作日志
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 操作日志列表
     */
    List<OperationLog> findWithPagination(@Param("offset") int offset, 
                                         @Param("limit") int limit);
    
    /**
     * 统计操作日志总数
     * @return 总数
     */
    int count();
    
    /**
     * 根据条件统计操作日志数量
     * @param userId 用户ID（可选）
     * @param username 用户名（可选）
     * @param operationType 操作类型（可选）
     * @param module 模块名称（可选）
     * @param result 操作结果（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 数量
     */
    int countByConditions(@Param("userId") Long userId,
                         @Param("username") String username,
                         @Param("operationType") String operationType,
                         @Param("module") String module,
                         @Param("result") String result,
                         @Param("startTime") LocalDateTime startTime,
                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 删除指定时间之前的操作日志（用于日志清理）
     * @param beforeTime 指定时间
     * @return 删除的记录数
     */
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
}