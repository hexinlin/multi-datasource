package com.xinlinh.multi.datasource.springdemo;

import com.xinlinh.multi.datasource.springdemo.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName: MultiDataSourceSpringDemo
 * @Description: 基于Spring使用MultiDataSource
 * @Author:xinlinh
 * @Date: 2020/11/11 16:41
 * @Version: 1.0
 **/
public class MultiDataSourceSpringDemo {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.queryFromDataSource1();
        userService.queryFromDataSource2();
        userService.queryFromDataSource3();
    }
}
