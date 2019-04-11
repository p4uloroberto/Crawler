package com.nosbielc.crawler.central.boot.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.nosbielc.crawler.central.application.monitoring.MongoCommandListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.nosbielc.crawler.central.persistence.mongo")
public class MongoConfiguration extends AbstractMongoConfiguration {

    private final MongoProperties mongoProperties;
    private final Environment environment;
    private final MongoCommandListener mongoCommandListener;

    @Autowired
    public MongoConfiguration(MongoProperties mongoProperties,
                              Environment environment,
                              MongoCommandListener mongoCommandListener) {
        this.mongoProperties = mongoProperties;
        this.environment = environment;
        this.mongoCommandListener = mongoCommandListener;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientFactory factory = new MongoClientFactory(mongoProperties, environment);
        MongoClientOptions options = MongoClientOptions
                .builder()
                .applicationName("CentralCrawlerApplication")
                .description("Projeto Crawler|Apache Camel com Spring Boot (Extract Load Transform Crawler Application)")
                .addCommandListener(mongoCommandListener)
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.UNACKNOWLEDGED)
                .minConnectionsPerHost(10)
                .retryWrites(true)
                .build();
        return factory.createMongoClient(options);
    }

    @Override
    protected String getDatabaseName() {
        String profile = environment.getActiveProfiles()[0];
        return String.join("-", mongoProperties.getDatabase(), profile);
    }
}
