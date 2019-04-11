package com.nosbielc.crawler.central.boot;

import com.nosbielc.crawler.central.domain.service.JobMetricService;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.spring.boot.RoutesCollector;
import org.apache.camel.spring.boot.cloud.CamelCloudAutoConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EnableCaching
@SpringBootApplication(
        scanBasePackages = {"com.nosbielc.crawler.central"},
        exclude = {MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class,
                MongoReactiveDataAutoConfiguration.class,
                CamelCloudAutoConfiguration.class})
@EntityScan("com.nosbielc.crawler.central.domain")
public class CentralCrawlerApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(CentralCrawlerApplication.class);
    private static ConfigurableEnvironment environment;

    private final CamelContext camelContext;
    private final ApplicationContext applicationContext;
    private final RoutesCollector collector;
    private final ElasticsearchOperations elasticsearchOperations;
    private final JobMetricService jobMetricService;

    @Autowired
    public CentralCrawlerApplication(ConfigurableEnvironment environment,
                                     ElasticsearchOperations elasticsearchOperations,
                                     CamelContext camelContext,
                                     RoutesCollector collector,
                                     ApplicationContext applicationContext,
                                     JobMetricService jobMetricService) {
        this.environment = environment;
        this.elasticsearchOperations = elasticsearchOperations;
        this.camelContext = camelContext;
        this.collector = collector;
        this.applicationContext = applicationContext;
        this.jobMetricService = jobMetricService;
    }

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        ConfigurableApplicationContext context = SpringApplication
                .run(CentralCrawlerApplication.class, args);

        context.setId("ETLCrawlerContext");
        context.setEnvironment(environment);

        logger.info("Context Active: " + context.isActive());
        logger.info("Context Running: " + context.isRunning());
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("--Initializing--");
        printEnvInfo();
        printElasticSearchInfo();
        registerCamelRoutes();
        updateCamelRoutesStarted();
        executeCommandLine(args);
        logger.info("--Initialized--");
    }

    private void registerCamelRoutes() throws Exception {
        camelContext.stop();
        collector.onApplicationEvent(new ContextRefreshedEvent(applicationContext));
        camelContext.start();
    }

    private void executeCommandLine(String[] args) {
        logger.info("args: " + Arrays.deepToString(args));
    }

    private void printEnvInfo() {
        logger.info("--Environment--");
        logger.info("Active Profiles: " + Arrays.deepToString(environment.getActiveProfiles()));
        logger.info(String.valueOf(environment));
        logger.info("--Environment--");
    }

    private void printElasticSearchInfo() {
        logger.info("--ElasticSearch--");
        Client client = elasticsearchOperations.getClient();
        Map<String, Settings> settingsMap = client.settings().getAsGroups();
        settingsMap.forEach((key, settings) -> logger.info(key + "=" + settings.toDelimitedString(',')));
        logger.info("--ElasticSearch--");
    }

    private void updateCamelRoutesStarted() {
        logger.info("--Apache Camel--");
        List<Route> routes = camelContext.getRoutes();
        routes.forEach(route -> {
            jobMetricService.updateStarted(route.getId());
            logger.info(String.format("Route Id: %s Desc: %s", route.getId(), route.getDescription()));
        });
        logger.info("--Apache Camel--");
    }
}
