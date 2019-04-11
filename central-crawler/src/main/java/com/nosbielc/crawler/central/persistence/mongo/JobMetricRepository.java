package com.nosbielc.crawler.central.persistence.mongo;

import com.nosbielc.crawler.central.domain.mongo.JobMetric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobMetricRepository extends MongoRepository<JobMetric, String> {
}
