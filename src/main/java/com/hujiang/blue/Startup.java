package com.hujiang.blue;

import com.hujiang.blue.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
public class Startup {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(AppConfig.class)
                .build()
                .run(args);
    }
}
