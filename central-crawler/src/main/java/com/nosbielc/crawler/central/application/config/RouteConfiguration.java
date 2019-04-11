package com.nosbielc.crawler.central.application.config;

import java.util.Objects;
import java.util.StringJoiner;

public abstract class RouteConfiguration {

    private boolean enabled;
    private boolean byPass;
    private String name;
    private String fromInternal;
    private String from;
    private String schedule;
    private String to;
    private long redeliveryDelay = 5;
    private int maximumRedeliveries = 5;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isByPass() {
        return byPass;
    }

    public RouteConfiguration setByPass(boolean byPass) {
        this.byPass = byPass;
        return this;
    }

    public RouteConfiguration setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getName() {
        return name;
    }

    public RouteConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public String getFromInternal() {
        return fromInternal;
    }

    public RouteConfiguration setFromInternal(String fromInternal) {
        this.fromInternal = fromInternal;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public RouteConfiguration setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public RouteConfiguration setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public String getTo() {
        return to;
    }

    public RouteConfiguration setTo(String to) {
        this.to = to;
        return this;
    }

    public long getRedeliveryDelay() {
        return redeliveryDelay;
    }

    public RouteConfiguration setRedeliveryDelay(long redeliveryDelay) {
        this.redeliveryDelay = redeliveryDelay;
        return this;
    }

    public int getMaximumRedeliveries() {
        return maximumRedeliveries;
    }

    public RouteConfiguration setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteConfiguration)) return false;
        RouteConfiguration that = (RouteConfiguration) o;
        return redeliveryDelay == that.redeliveryDelay &&
                maximumRedeliveries == that.maximumRedeliveries &&
                Objects.equals(name, that.name) &&
                Objects.equals(from, that.from) &&
                Objects.equals(schedule, that.schedule) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, from, schedule, to, redeliveryDelay, maximumRedeliveries);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RouteConfiguration.class.getSimpleName() + "[", "]")
                .add("enabled=" + enabled)
                .add("byPass=" + byPass)
                .add("name='" + name + "'")
                .add("fromInternal='" + fromInternal + "'")
                .add("from='" + from + "'")
                .add("schedule='" + schedule + "'")
                .add("to='" + to + "'")
                .add("redeliveryDelay=" + redeliveryDelay)
                .add("maximumRedeliveries=" + maximumRedeliveries)
                .toString();
    }
}
