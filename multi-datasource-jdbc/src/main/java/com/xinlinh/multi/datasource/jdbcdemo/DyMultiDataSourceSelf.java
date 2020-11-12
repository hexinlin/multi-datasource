package com.xinlinh.multi.datasource.jdbcdemo;

import com.alibaba.druid.pool.DruidDataSource;
import com.xinlinh.multi.datasource.core.DyMultiDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DyMultiDataSourceSelf
 * @Description: 继承DyMultiDataSource，使用编码方式定义动态多数据源
 * @Author:xinlinh
 * @Date: 2020/10/27 10:14
 * @Version: 1.0
 **/
public class DyMultiDataSourceSelf extends DyMultiDataSource {

    public DyMultiDataSourceSelf(boolean initByClass) {
        super(initByClass);
    }

    @Override
    protected void initDataSources() {
        String jdbcUrl = "jdbc:mysql://172.20.4.206:3306/dbtest1?useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull";
        String userName = "jm_monitor";
        String password = "jm_monitor";
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(userName);
        hikariDataSource.setPassword(password);
        hikariDataSource.setConnectionTimeout(60000);
        hikariDataSource.setMaximumPoolSize(10);
        hikariDataSource.setPoolName("datasource4");

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxWait(30000);
        druidDataSource.setMaxActive(10);

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(jdbcUrl);
        basicDataSource.setUsername(userName);
        basicDataSource.setPassword(password);
        basicDataSource.setMaxTotal(10);

        Map<String,DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("datasource4",hikariDataSource);
        dataSourceMap.put("datasource5",druidDataSource);
        dataSourceMap.put("datasource6",basicDataSource);

        List<DataSource> defaultDataSourceList = new ArrayList<>();
        defaultDataSourceList.add(hikariDataSource);
        defaultDataSourceList.add(druidDataSource);
        defaultDataSourceList.add(basicDataSource);

        setDataSourceMap(dataSourceMap);
        setDefaultDataSources(defaultDataSourceList);
        setLenientFallback(false);//允许找不到指定数据源，从而使用默认数据源的情况
    }
}
