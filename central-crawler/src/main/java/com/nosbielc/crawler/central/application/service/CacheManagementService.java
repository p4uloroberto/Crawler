package com.nosbielc.crawler.central.application.service;

import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CacheManagementService {

    Mono<HttpStatus> dropAll();

    Flux<String> getAllCollections();
}
