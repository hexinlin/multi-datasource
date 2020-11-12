package com.xinlinh.multi.datasource.springdemo.aop;

import com.xinlinh.multi.datasource.core.DyMultiDataSourceHolder;
import com.xinlinh.multi.datasource.springdemo.annotation.DyDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName: DyDataSourceAspect
 * @Description: 动态数据源切面处理器
 * @Author:xinlinh
 * @Date: 2020/11/11 16:13
 * @Version: 1.0
 **/
@Order(-1)
@Aspect
@Component
public class DyDataSourceAspect {

    /**
     * @return void
     * @Author xinlinh
     * @Description 切换数据源
     * @Date 2020/11/11 16:19
     * @Param [point, dyDataSource]
     **/
    @Before("@annotation(dyDataSource)")
    public void switchDataSource(JoinPoint point, DyDataSource dyDataSource) {
        //设置数据源key
        DyMultiDataSourceHolder.setThreadDataSourceKey(dyDataSource.value());
    }

    /**
     * @return void
     * @Author xinlinh
     * @Description 重置数据源，避免线程重用造成的数据源混乱。
     * @Date 2020/11/11 16:20
     * @Param [point, dyDataSource]
     **/
    @After("@annotation(dyDataSource)")
    public void resetDataSource(JoinPoint point, DyDataSource dyDataSource) {
        DyMultiDataSourceHolder.clearThreadDataSourceKey();
    }
}
