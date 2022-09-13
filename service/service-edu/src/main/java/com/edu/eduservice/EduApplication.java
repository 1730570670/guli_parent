package com.edu.eduservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @CreateTime: 2022-08-15
 * EduSpringBoot启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.edu")
@MapperScan("com.edu.eduservice.mapper")
@Slf4j
@EnableDiscoveryClient//服务的发现
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
        log.info("eduApplication启动成功");
    }
}
