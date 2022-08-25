package com.edu.eduservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @CreateTime: 2022-08-15
 */
@Configuration
@Import({MyBatisPlusConfig.class})
public class EduConfig {
}
