package com.edu.ossservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @CreateTime: 2022-08-22
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})//排除数据源的自动装配功能
@ComponentScan(basePackages ="com.edu")
@Slf4j
public class OssApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
        log.info("oss服务启动成功");
    }
}
