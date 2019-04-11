package com.nosbielc.crawler.central.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CacheManagementServiceProvider implements CacheManagementService {

    private final MongoOperations mongo;

    @Autowired
    public CacheManagementServiceProvider(MongoOperations mongo) {
        this.mongo = mongo;
    }

    @Override
    public Mono<HttpStatus> dropAll() {
        try {
            mongo.getCollectionNames();
            return Mono.just(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return Mono.just(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public Flux<String> getAllCollections() {
        return Flux.just(mongo.getCollectionNames().toArray(new String[0]));
    }

    private void dropCollection(String name) {
        mongo.dropCollection(name);
    }
}
