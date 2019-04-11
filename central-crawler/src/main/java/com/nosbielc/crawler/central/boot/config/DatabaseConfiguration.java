package com.nosbielc.crawler.central.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.nosbielc.crawler.central.persistence.database")
public class DatabaseConfiguration {
}
