package com.xinlinh.multi.datasource.jdbcdemo;

import com.xinlinh.multi.datasource.core.DyMultiDataSource;
import com.xinlinh.multi.datasource.core.DyMultiDataSourceHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @ClassName: MultiDataSourceJdbcDemo
 * @Description: 基于JDBC使用MultiDataSource
 * @Author:xinlinh
 * @Date: 2020/10/26 10:14
 * @Version: 1.0
 **/
public class MultiDataSourceJdbcDemo {

    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        test4();
    }

    /**
     * @return void
     * @Author xinlinh
     * @Description 不指定数据源，使用默认数据源
     * @Date 2020/10/27 10:11
     * @Param []
     **/
    public static void test1() throws Exception {
        String sql = "select * from t_user";
        DyMultiDataSource dynamicDataSource = new DyMultiDataSource();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dynamicDataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    /**
     * @return void
     * @Author xinlinh
     * @Description 使用指定数据源
     * @Date 2020/10/27 10:11
     * @Param []
     **/
    public static void test2() throws Exception {
        String sql = "select * from t_user";
        DyMultiDataSource dynamicDataSource = new DyMultiDataSource();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DyMultiDataSourceHolder.setThreadDataSourceKey("datasource3");//指定使用datasource3
            connection = dynamicDataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        } finally {
            DyMultiDataSourceHolder.clearThreadDataSourceKey();
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * @return void
     * @Author xinlinh
     * @Description 不指定数据源，使用默认数据源，通过编码方式注入数据源
     * @Date 2020/10/27 10:44
     * @Param []
     **/
    public static void test3() throws Exception {
        String sql = "select * from t_user";
        DyMultiDataSourceSelf dynamicDataSource = new DyMultiDataSourceSelf(true);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dynamicDataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * @return void
     * @Author xinlinh
     * @Description 使用指定数据源
     * @Date 2020/10/27 10:44
     * @Param []
     **/
    public static void test4() throws Exception {
        String sql = "select * from t_user";
        DyMultiDataSourceSelf dynamicDataSource = new DyMultiDataSourceSelf(true);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DyMultiDataSourceHolder.setThreadDataSourceKey("datasource5");//指定使用datasource5
            connection = dynamicDataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        } finally {
            DyMultiDataSourceHolder.clearThreadDataSourceKey();
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
