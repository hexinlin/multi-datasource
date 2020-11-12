package com.xinlinh.multi.datasource.core;

/**
 * @ClassName: DyMultiDataSourceHolder
 * @Description: 动态数据源工具类，基于线程本地变量存储、访问即将使用的数据源的key
 * @Author:xinlinh
 * @Date: 2020/10/20 15:25
 * @Version: 1.0
 **/
public class DyMultiDataSourceHolder {
    private final static ThreadLocal<String> THREAD_DATA_SOURCE_KEY = new ThreadLocal<>();

    public static String getThreadDataSourceKey() {
        return THREAD_DATA_SOURCE_KEY.get();
    }

    public static void setThreadDataSourceKey(String dataSourceKey) {
        THREAD_DATA_SOURCE_KEY.set(dataSourceKey);
    }

    public static void clearThreadDataSourceKey() {
        THREAD_DATA_SOURCE_KEY.remove();
    }
}
