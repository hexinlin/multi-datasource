package com.xinlinh.multi.datasource.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName: DyMultiAbstractRoutingDataSource
 * @Description: 具有路由功能的数据源，主要是从多数据源中选择一个默认或者用户指定的数据源
 * @Author:xinlinh
 * @Date: 2020/10/20 15:07
 * @Version: 1.0
 **/
public abstract class DyMultiAbstractRoutingDataSource extends DyMultiAbstractDataSource {
    //存储多数据源的集合
    private Map<String,DataSource> dataSourceMap;
    //默认数据源
    private List<DataSource> defaultDataSources;
    //是否允许找不到key对应的数据源时，返回默认数据源
    private boolean lenientFallback = true;




    @Override
    public Connection getConnection() throws SQLException {
        return getSpecifiedDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getSpecifiedDataSource().getConnection(username,password);
    }

    private DataSource getSpecifiedDataSource() {
        Optional.of(dataSourceMap).orElseThrow(() ->{return  new IllegalArgumentException("DataSource map not initialized");});
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = dataSourceMap.get(lookupKey);
        if(dataSource==null) {
            if(this.lenientFallback|| lookupKey==null) {
                dataSource = this.defaultDataSources.get(ThreadLocalRandom.current().nextInt(0,this.defaultDataSources.size()));
            }
        }
        if(dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }

    protected abstract Object determineCurrentLookupKey();


    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public List<DataSource> getDefaultDataSources() {
        return defaultDataSources;
    }

    public void setDefaultDataSources(List<DataSource> defaultDataSources) {
        this.defaultDataSources = defaultDataSources;
    }

    public boolean isLenientFallback() {
        return lenientFallback;
    }

    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
    }
}
