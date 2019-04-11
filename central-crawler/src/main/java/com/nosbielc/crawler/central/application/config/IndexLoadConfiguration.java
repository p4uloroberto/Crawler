package com.nosbielc.crawler.central.application.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
@ConfigurationProperties(IndexLoadConfiguration.KEY)
public class IndexLoadConfiguration extends RouteConfiguration {

    public static final String KEY = "app.indexload.route";
}
