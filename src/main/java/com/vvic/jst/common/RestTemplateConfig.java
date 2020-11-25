package com.vvic.jst.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>描述类的信息</p>
 *
 * <pre>
 * @author zhongjunkang
 * @date 2020/11/16/0016
 * <pre>
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    private RestTemplateBuilder builder;

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}
