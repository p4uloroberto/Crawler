package com.nosbielc.crawler.central.application.monitoring;

import com.accenture.etlcrawler.domain.mongo.JobMetric;
import com.accenture.etlcrawler.domain.service.JobMetricService;
import org.apache.camel.Exchange;
import org.apache.camel.management.event.ExchangeCompletedEvent;
import org.apache.camel.management.event.ExchangeCreatedEvent;
import org.apache.camel.management.event.ExchangeSentEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.EventObject;
import java.util.concurrent.TimeUnit;

@Component
public class RouteExecutionNotifier extends EventNotifierSupport {

    private final Logger logger = LogManager.getLogger(RouteExecutionNotifier.class);

    private final JobMetricService jobMetricService;

    @Autowired
    public RouteExecutionNotifier(JobMetricService jobMetricService) {
        this.jobMetricService = jobMetricService;
    }

    @Override
    public void notify(EventObject event) throws Exception {
        if (event instanceof ExchangeCreatedEvent) {
            logCreateExchange((ExchangeCreatedEvent) event);
        }

        if (event instanceof ExchangeSentEvent) {
            logSentEvent((ExchangeSentEvent) event);
        }

        if (event instanceof ExchangeCompletedEvent) {
            logCompletedEvent((ExchangeCompletedEvent) event);
        }
    }

    private void logCreateExchange(ExchangeCreatedEvent event) {
        Exchange exchange = event.getExchange();
        String routeId = exchange.getFromRouteId();
        JobMetric metric = jobMetricService.updateRunning(routeId);
        logger.info(String.format("Updated: %s", String.valueOf(metric)));
    }

    private void logCompletedEvent(ExchangeCompletedEvent event) {
        Exchange exchange = event.getExchange();
        String routeId = exchange.getFromRouteId();
        long elapsed = calculateElapsedTime(event);
        if (log.isInfoEnabled()) {
            log.info(String.format("-- COMPLETED ROUTE: %s [%ds or %dms]", routeId,
                    TimeUnit.MILLISECONDS.toSeconds(elapsed), elapsed));
        }
        JobMetric metric = jobMetricService.updateSleeping(routeId);
        logger.info(String.format("Updated: %s", String.valueOf(metric)));
    }

    private void logSentEvent(ExchangeSentEvent event) {
        long timeTaken = event.getTimeTaken();
        if (log.isInfoEnabled()) {
            log.info(String.format("-- SENT TO ROUTE: %s [%ds or %dms]", event.getEndpoint(),
                    TimeUnit.MILLISECONDS.toSeconds(timeTaken), timeTaken));
        }
    }

    private long calculateElapsedTime(ExchangeCompletedEvent event) {
        Date created = event.getExchange().getProperty(Exchange.CREATED_TIMESTAMP, Date.class);
        Date now = Date.from(Instant.now());
        return now.getTime() - created.getTime();
    }

    public boolean isEnabled(EventObject event) {
        return true;
    }

    @Override
    protected void doStart() throws Exception {
        // filter out unwanted events
        setIgnoreCamelContextEvents(true);
        setIgnoreServiceEvents(true);
        setIgnoreRouteEvents(true);
        setIgnoreExchangeCreatedEvent(false);
        setIgnoreExchangeCompletedEvent(false);
        setIgnoreExchangeFailedEvents(true);
        setIgnoreExchangeRedeliveryEvents(true);
        setIgnoreExchangeSentEvents(false);
    }

    @Override
    protected void doStop() throws Exception {
        //do nothing
    }
}
