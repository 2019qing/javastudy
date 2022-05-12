package com.itheima;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogSystemApplicationTests {
    @Autowired
    private DruidDataSource druidDataSource;
    @Test
    public void contextLoads() {
        System.out.println(druidDataSource);
    }

}
