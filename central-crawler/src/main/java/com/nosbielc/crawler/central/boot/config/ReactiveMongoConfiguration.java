package com.nosbielc.crawler.central.boot.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.event.CommandListener;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.ReactiveMongoClientFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories("com.nosbielc.crawler.central.persistence.mongo")
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;
    private final Environment environment;
    private final CommandListener monitorCommandListener;

    @Autowired
    public ReactiveMongoConfiguration(MongoProperties mongoProperties,
                                      Environment environment,
                                      CommandListener monitorCommandListener) {
        this.mongoProperties = mongoProperties;
        this.environment = environment;
        this.monitorCommandListener = monitorCommandListener;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        ReactiveMongoClientFactory factory = new ReactiveMongoClientFactory(mongoProperties, environment, null);
        MongoClientSettings settings = MongoClientSettings
                .builder()
                .applicationName("ETLCrawlerApplication")
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.UNACKNOWLEDGED)
                .retryWrites(true)
                .applyToConnectionPoolSettings(this::connectionPoolSettings)
                .addCommandListener(monitorCommandListener)
                .build();
        return factory.createMongoClient(settings);
    }

    private void connectionPoolSettings(ConnectionPoolSettings.Builder builder) {
        builder.maintenanceFrequency(4, TimeUnit.HOURS);
        builder.maxConnectionIdleTime(2, TimeUnit.MINUTES);
        builder.maxWaitQueueSize(1000);
        builder.minSize(10);
        builder.maxSize(300);
    }

    @Override
    protected String getDatabaseName() {
        String profile = environment.getActiveProfiles()[0];
        return String.join("-", mongoProperties.getDatabase(), profile);
    }
}
