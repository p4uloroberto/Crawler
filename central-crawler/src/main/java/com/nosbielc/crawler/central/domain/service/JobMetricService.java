package com.nosbielc.crawler.central.domain.service;

import com.nosbielc.crawler.central.domain.mongo.JobMetric;
import com.nosbielc.crawler.central.eai.route.context.RouteContext;

public interface JobMetricService {

    JobMetric updateOnError(String routeId);

    JobMetric updateStarted(String routeId);

    JobMetric updateExecution(String routeId, RouteContext context);

    JobMetric updateSleeping(String routeId);

    JobMetric updateRunning(String routeId);
}
