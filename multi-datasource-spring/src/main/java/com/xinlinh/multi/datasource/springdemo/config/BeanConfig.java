package com.xinlinh.multi.datasource.springdemo.config;

import com.xinlinh.multi.datasource.core.DyMultiDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName: BeanConfig
 * @Description: 配置初始化bean
 * @Author:xinlinh
 * @Date: 2020/11/11 16:27
 * @Version: 1.0
 **/
@Configuration
public class BeanConfig {

    /**
     * @return javax.sql.DataSource
     * @Author xinlinh
     * @Description 创建数据源
     * @Date 2020/11/11 16:37
     * @Param []
     **/
    @Bean(name = "dyDatasource")
    public DataSource dyDatasource() {
        return new DyMultiDataSource();
    }

    /**
     * @return org.springframework.jdbc.core.JdbcTemplate
     * @Author xinlinh
     * @Description 指定JDBC使用的数据源
     * @Date 2020/11/11 16:37
     * @Param []
     **/
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dyDatasource());
    }

    /**
     * @return org.springframework.transaction.PlatformTransactionManager
     * @Author xinlinh
     * @Description 指定事务管理器关联的数据源
     * @Date 2020/11/11 16:38
     * @Param []
     **/
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dyDatasource());
    }
}
