package org.example.coursesystem.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 简单缓存配置类 - 当Redis不可用时的备用方案
 */
@Configuration
@EnableCaching
public class SimpleCacheConfig {

    /**
     * 配置简单的内存缓存管理器
     * 只有在没有其他CacheManager Bean时才会创建
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // 设置缓存名称
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "students", "courses", "semesters", "courseSelections"
        ));
        return cacheManager;
    }
}