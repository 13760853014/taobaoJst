package com.vvic.jst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@ComponentScan("com.vvic.jst")
@EnableScheduling
public class JSTApplication {

    public static void main(String[] args) {
        SpringApplication.run(JSTApplication.class, args);
    }

}
