package com.nosbielc.crawler.central.persistence.mongo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public final class BlockControl {

    private static final Logger logger = LogManager.getLogger(BlockControl.class);

    private static final BlockControl BLOCK_ONCE = new BlockControl();

    private final AtomicInteger retryAttempt;
    private long duration = 500;

    public BlockControl() {
        this.retryAttempt = new AtomicInteger(0);
    }

    public static Duration blockOnce() {
        return BLOCK_ONCE.get();
    }

    public Duration get() {
        return Duration.ofMillis(duration);
    }

    public boolean stop() {
        var maxRetries = 3;
        return retryAttempt.get() > maxRetries;
    }

    public int getRetryAttempt() {
        return retryAttempt.incrementAndGet();
    }

    public long getDuration() {
        return duration;
    }

    public BlockControl setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public BlockControl retry(String operation, Exception ex) {
        int retry = getRetryAttempt();
        this.duration = retry * duration;
        logger.warn(String.format("Error on %s: %s retry: %s nextInterval: %s", operation, ex.getMessage(), retry, getDuration()));
        return this;
    }
}
