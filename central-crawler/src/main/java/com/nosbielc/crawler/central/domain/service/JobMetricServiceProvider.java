package com.nosbielc.crawler.central.domain.service;

import com.nosbielc.crawler.central.domain.mongo.JobExecution;
import com.nosbielc.crawler.central.domain.mongo.JobMetric;
import com.nosbielc.crawler.central.domain.mongo.JobState;
import com.nosbielc.crawler.central.eai.route.context.RouteContext;
import com.nosbielc.crawler.central.persistence.mongo.JobMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JobMetricServiceProvider implements JobMetricService {

    private final JobMetricRepository jobMetricRepository;

    @Autowired
    public JobMetricServiceProvider(JobMetricRepository jobMetricRepository) {
        this.jobMetricRepository = jobMetricRepository;
    }

    @Override
    public JobMetric updateOnError(String routeId) {
        var metric = findById(routeId);
        return jobMetricRepository.save(metric.setState(JobState.ON_ERROR));
    }

    @Override
    public JobMetric updateStarted(String routeId) {
        var metric = findById(routeId);
        metric.setStart(Date.from(Instant.now()));
        return jobMetricRepository.save(metric.setState(JobState.STARTED));
    }

    @Override
    public JobMetric updateExecution(String routeId, RouteContext context) {
        var metric = findById(routeId);
        metric.addExecution(new JobExecution(context.getStartTime(), context.getEndTime()));
        return jobMetricRepository.save(metric.setState(JobState.RUNNING));
    }

    @Override
    public JobMetric updateSleeping(String routeId) {
        var metric = findById(routeId);
        return jobMetricRepository.save(metric.setState(JobState.SLEEPING));
    }

    @Override
    public JobMetric updateRunning(String routeId) {
        var metric = findById(routeId);
        metric.setLastExecution(Date.from(Instant.now()));
        return jobMetricRepository.save(metric.setState(JobState.RUNNING));
    }

    private JobMetric findById(String routeId) {
        Optional<JobMetric> optional = jobMetricRepository.findById(routeId);
        return optional.orElse(new JobMetric(routeId));
    }
}
