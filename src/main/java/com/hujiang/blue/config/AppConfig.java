package com.hujiang.blue.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Configuration
@ComponentScan("com.hujiang")
@SpringBootApplication
@EnableAutoConfiguration()
@EnableScheduling
public class AppConfig {
}
