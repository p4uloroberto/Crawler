package com.nosbielc.crawler.central.boot.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.*;

@Configuration
@EnableElasticsearchRepositories("com.nosbielc.crawler.central.persistence.elastic")
public class ElasticSearchConfiguration {

    private static final Logger logger = LogManager.getLogger(ElasticSearchConfiguration.class);
    private static final String ELASTIC_VERSION = "6.5.1";

    @Bean
    @Profile("dev")
    public EmbeddedElastic createElasticServer() {
        try {
            return EmbeddedElastic.builder()
                    .withElasticVersion(ELASTIC_VERSION)
                    .withSetting(HTTP_PORT, "9200")
                    .withSetting(TRANSPORT_TCP_PORT, "9300")
                    .withSetting(CLUSTER_NAME, "elasticsearch-local-cluster")
                    .withEsJavaOpts("-Xms128m -Xmx512m")
                    .withStartTimeout(1, TimeUnit.MINUTES)
                    .withDownloadDirectory(new File("./elastic"))
                    .build()
                    .start();
        } catch (InterruptedException | IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new BeanInitializationException("Cannot Initialize EmbededElastic: " + ex.getMessage(), ex);
        }
    }
}
