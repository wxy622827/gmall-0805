package com.atguigu.gmall.item.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxy
 * @create 2020-01-14 21:13
 */
@Configuration

public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
      return  new ThreadPoolExecutor(500,800,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100000));

    }
}
