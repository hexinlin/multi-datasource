package com.xinlinh.multi.datasource.springdemo.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: DyDataSource
 * @Description: 用于指定数据源的注解类
 * @Author:xinlinh
 * @Date: 2020/11/11 16:09
 * @Version: 1.0
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface DyDataSource {
    /**
     * @Author xinlinh
     * @Description 数据源KEY值
     * @Date 2020/11/11 16:10
     * @Param []
     * @return java.lang.String
     **/
    String value();
}
