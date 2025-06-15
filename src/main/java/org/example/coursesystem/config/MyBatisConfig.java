package org.example.coursesystem.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis配置类
 * 解决数据库表名前缀问题
 */
@Configuration
@MapperScan("org.example.coursesystem.mapper")
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 设置mapper文件位置
        sessionFactory.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml")
        );
        
        // 设置类型别名包
        sessionFactory.setTypeAliasesPackage("org.example.coursesystem.entity");
        
        // 获取Configuration对象并进行配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        
        // 开启驼峰命名转换
        configuration.setMapUnderscoreToCamelCase(true);
        
        // 设置日志实现
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        
        // 关键配置：设置数据库ID，避免自动添加数据库名前缀
        configuration.setDatabaseId("mysql");
        
        // 设置其他配置
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(org.apache.ibatis.type.JdbcType.NULL);
        
        sessionFactory.setConfiguration(configuration);
        
        return sessionFactory.getObject();
    }
}