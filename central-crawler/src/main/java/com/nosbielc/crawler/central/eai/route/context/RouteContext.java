package com.nosbielc.crawler.central.eai.route.context;

import org.apache.camel.Exchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class RouteContext {

    private static final Logger logger = LogManager.getLogger(RouteContext.class);

    private final Date startTime;
    private final Exchange exchange;
    private final int batchSize;
    private boolean byPass;
    private Date endTime;

    public RouteContext(Exchange exchange, int batchSize) {
        this.exchange = exchange;
        this.batchSize = batchSize;
        this.exchange.setExchangeId(this.getClass().getSimpleName());
        this.startTime = Date.from(Instant.now());
        logger.info("-- START CONTEXT: " + exchange.getFromRouteId() + " --");
    }

    public Exchange getExchange() {
        return exchange;
    }

    public RouteContext endContext() {
        this.endTime = Date.from(Instant.now());
        logger.info("-- FINISHED CONTEXT: " + exchange.getFromRouteId() + " --");
        return this;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public boolean isByPass() {
        return byPass;
    }

    public RouteContext setByPass(boolean byPass) {
        this.byPass = byPass;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isCompleted() {
        return getEndTime() != null;
    }

    public RouteContext setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteContext)) return false;
        RouteContext that = (RouteContext) o;
        return Objects.equals(exchange, that.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchange);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RouteContext.class.getSimpleName() + "[", "]")
                .add("exchange=" + exchange)
                .toString();
    }

    public Pageable getFirstPage(Sort sort) {
        return (batchSize <= 0) ?
                PageRequest.of(0, 1000, sort):
                PageRequest.of(0, batchSize, sort);
    }

    public Pageable getFirstPage() {
        return getFirstPage(Sort.unsorted());
    }
}
