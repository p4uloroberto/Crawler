package com.nosbielc.crawler.camelapidb.routers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RouterHawtio extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:Hawtio")
                .id("Internal::HawtioRouter")
                .description("Router exemplo para visualização no Hawtio.")
                .log("Executando Processador 1")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Processador 1 executado");
                    }
                })
                .log("Executando Processador 2")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Processador 2 executado");
                    }
                })
                .log("Executando Processador 3")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Processador 3 executado");
                    }
                })
                .end();
    }
}
