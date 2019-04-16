package com.nosbielc.crawler.camelapidb;

import com.nosbielc.crawler.camelapidb.model.Album;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() {

        // tag::rest[]
        restConfiguration() // <1>
                .bindingMode(RestBindingMode.json) // <2>
                .apiContextPath("/api-doc") // <3>
                .apiProperty("api.title",
                        "My Collection of Things API");
        // end::rest[]

        // tag::route[]
        rest("/albums") // <1>
                .description("Album collection API")
                .consumes("application/json")
                .produces("application/json")

                .get("/{albumId}").id("getAlbum")
                .description("Get an album") // <2>
                .outType(Album.class)
                .route()
                .to("sql:SELECT artist, title FROM albums" // <3>
                        + " WHERE id = :#albumId")
                .bean(OutputProcessor.class) // <4>
                .endRest()

                .get().id("getAllAlbums")
                .description("Get all albums") // <5>
                .outType(Album[].class)
                .route()
                .to("sql:SELECT artist, title FROM albums"
                        + " ORDER BY artist, title")
                .bean(OutputProcessor.class)
                .endRest();
        // end::route[]

    }

}
