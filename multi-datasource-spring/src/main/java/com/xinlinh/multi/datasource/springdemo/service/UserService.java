package com.xinlinh.multi.datasource.springdemo.service;

import com.xinlinh.multi.datasource.springdemo.annotation.DyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author:xinlinh
 * @Date: 2020/11/11 16:47
 * @Version: 1.0
 **/
@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sql = "select * from t_user";

    @DyDataSource("datasource1")
    public void queryFromDataSource1() {
        jdbcTemplate.query(sql, rs -> {
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        });
    }

    @DyDataSource("datasource2")
    public void queryFromDataSource2() {
        jdbcTemplate.query(sql, rs -> {
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        });
    }

    @DyDataSource("datasource3")
    public void queryFromDataSource3() {
        jdbcTemplate.query(sql, rs -> {
            while (rs.next()) {
                System.out.println(String.format("id:%s,name:%s,sex:%s", rs.getInt("id"), rs.getString("name"), rs.getInt("sex")));
            }
        });
    }
}
