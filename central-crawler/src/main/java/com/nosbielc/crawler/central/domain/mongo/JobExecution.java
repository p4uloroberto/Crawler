package com.nosbielc.crawler.central.domain.mongo;

import java.util.Date;
import java.util.StringJoiner;

public class JobExecution {

    private final Date start;
    private final Date end;

    public JobExecution(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobExecution.class.getSimpleName() + "[", "]")
                .add("start=" + start)
                .add("end=" + end)
                .toString();
    }

    public static long getAvg(JobExecution jobExecution) {
        if (jobExecution.getEnd() == null || jobExecution.getStart() == null) {
            return 0L;
        } else {
            var end = jobExecution.getEnd().getTime();
            var start = jobExecution.getStart().getTime();
            return start - end;
        }
    }
}
