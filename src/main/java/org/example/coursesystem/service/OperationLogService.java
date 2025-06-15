package org.example.coursesystem.service;

import org.example.coursesystem.entity.OperationLog;
import org.example.coursesystem.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务类
 */
@Service
@Transactional
public class OperationLogService {
    
    @Autowired
    private OperationLogMapper operationLogMapper;
    
    /**
     * 保存操作日志
     * @param operationLog 操作日志对象
     * @return 是否保存成功
     */
    public boolean saveOperationLog(OperationLog operationLog) {
        try {
            if (operationLog.getCreateTime() == null) {
                operationLog.setCreateTime(LocalDateTime.now());
            }
            if (operationLog.getOperationTime() == null) {
                operationLog.setOperationTime(LocalDateTime.now());
            }
            return operationLogMapper.insert(operationLog) > 0;
        } catch (Exception e) {
            // 记录日志失败不应该影响主业务，只记录错误
            System.err.println("保存操作日志失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 异步保存操作日志（避免影响主业务性能）
     * @param operationLog 操作日志对象
     */
    public void saveOperationLogAsync(OperationLog operationLog) {
        // 在实际项目中可以使用@Async注解或消息队列来实现异步处理
        // 这里先使用同步方式，后续可以优化
        saveOperationLog(operationLog);
    }
    
    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志对象
     */
    @Transactional(readOnly = true)
    public OperationLog getOperationLogById(Long id) {
        return operationLogMapper.findById(id);
    }
    
    /**
     * 查询所有操作日志
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getAllOperationLogs() {
        return operationLogMapper.findAll();
    }
    
    /**
     * 根据用户ID查询操作日志
     * @param userId 用户ID
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByUserId(Long userId) {
        return operationLogMapper.findByUserId(userId);
    }
    
    /**
     * 根据用户名查询操作日志
     * @param username 用户名
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByUsername(String username) {
        return operationLogMapper.findByUsername(username);
    }
    
    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByType(String operationType) {
        return operationLogMapper.findByOperationType(operationType);
    }
    
    /**
     * 根据模块查询操作日志
     * @param module 模块名称
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByModule(String module) {
        return operationLogMapper.findByModule(module);
    }
    
    /**
     * 根据操作结果查询操作日志
     * @param result 操作结果
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByResult(String result) {
        return operationLogMapper.findByResult(result);
    }
    
    /**
     * 根据时间范围查询操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return operationLogMapper.findByTimeRange(startTime, endTime);
    }
    
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
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsByConditions(Long userId, String username, 
                                                          String operationType, String module, 
                                                          String result, LocalDateTime startTime, 
                                                          LocalDateTime endTime) {
        return operationLogMapper.findByConditions(userId, username, operationType, 
                                                  module, result, startTime, endTime);
    }
    
    /**
     * 分页查询操作日志
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 操作日志列表
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getOperationLogsWithPagination(int page, int size) {
        int offset = (page - 1) * size;
        return operationLogMapper.findWithPagination(offset, size);
    }
    
    /**
     * 统计操作日志总数
     * @return 总数
     */
    @Transactional(readOnly = true)
    public int getTotalCount() {
        return operationLogMapper.count();
    }
    
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
    @Transactional(readOnly = true)
    public int getCountByConditions(Long userId, String username, String operationType, 
                                   String module, String result, LocalDateTime startTime, 
                                   LocalDateTime endTime) {
        return operationLogMapper.countByConditions(userId, username, operationType, 
                                                   module, result, startTime, endTime);
    }
    
    /**
     * 清理指定时间之前的操作日志
     * @param beforeTime 指定时间
     * @return 删除的记录数
     */
    public int cleanupLogsBefore(LocalDateTime beforeTime) {
        return operationLogMapper.deleteBeforeTime(beforeTime);
    }
    
    /**
     * 清理30天前的操作日志
     * @return 删除的记录数
     */
    public int cleanupOldLogs() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return cleanupLogsBefore(thirtyDaysAgo);
    }
}