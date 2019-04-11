package com.nosbielc.crawler.central.domain.mongo;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Document(collection = "metrics")
public class JobMetric {

    @Id
    private final String name;
    private Date start = new Date();
    private Date lastExecution = null;
    private JobState state;
    private String averageExecutionTime = "Never Runned";
    private List<JobExecution> executions = new ArrayList<>();

    @PersistenceConstructor
    public JobMetric(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public Date getStart() {
        return start;
    }

    public JobMetric setStart(Date start) {
        this.start = start;
        return this;
    }

    public JobState getState() {
        return state;
    }

    public JobMetric setState(JobState state) {
        this.state = state;
        return this;
    }

    public JobMetric setAverageExecutionTime(String averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
        return this;
    }

    public List<JobExecution> getExecutions() {
        return executions;
    }

    public JobMetric setExecutions(List<JobExecution> executions) {
        this.executions = executions;
        return this;
    }

    public Date getLastExecution() {
        return lastExecution;
    }

    public JobMetric setLastExecution(Date lastExecution) {
        this.lastExecution = lastExecution;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobMetric)) return false;
        JobMetric that = (JobMetric) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(averageExecutionTime, that.averageExecutionTime) &&
                Objects.equals(executions, that.executions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, averageExecutionTime, executions);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobMetric.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("averageExecutionTime=" + averageExecutionTime)
                .add("executions=" + executions)
                .toString();
    }

    public void addExecution(JobExecution jobExecution) {
        if (getExecutions() == null) {
            this.executions = new ArrayList<>();
        }
        getExecutions().add(jobExecution);
        setAverageExecutionTime(calculateNewAverage());
    }

    private String calculateNewAverage() {
        AtomicLong total = new AtomicLong();
        getExecutions().stream()
                .map(JobExecution::getAvg)
                .forEach(total::addAndGet);
        long avg = (total.get() / getExecutions().size());
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .printZeroIfSupported()
                .minimumPrintedDigits(2)
                .appendHours()
                .appendSuffix("H:")
                .appendMinutes()
                .appendSuffix("m:")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();
        String average = formatter.print(new Duration(avg).toPeriod())
                .replaceAll("-", "");

        return (average.isEmpty()) ? "00H:00m:00s" : average;
    }
}
