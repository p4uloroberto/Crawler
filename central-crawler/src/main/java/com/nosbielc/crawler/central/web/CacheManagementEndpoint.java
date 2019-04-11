package com.nosbielc.crawler.central.web;

import com.nosbielc.crawler.central.application.service.CacheManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/cache")
public class CacheManagementEndpoint {

    private final CacheManagementService cacheManagementService;

    @Autowired
    public CacheManagementEndpoint(CacheManagementService cacheManagementService) {
        this.cacheManagementService = cacheManagementService;
    }

    @DeleteMapping
    public Mono<HttpStatus> deleteAll() {
        return cacheManagementService.dropAll();
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<String> getAll() {
        return cacheManagementService.getAllCollections();
    }

}
