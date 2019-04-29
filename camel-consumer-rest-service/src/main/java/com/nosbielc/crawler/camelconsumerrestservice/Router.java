package com.nosbielc.crawler.camelconsumerrestservice;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:ConsumerRest?period=5000")
                .id("RouterConsumerRest::Internal")
                .description("Consumer de Rest")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("http://localhost:8089/api/transacao"))
                .to("http4:localhost:8089/api/transacao").convertBodyTo(String.class)
                .log("Transações Obtidas - ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String input = (String) exchange.getIn().getBody();

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(input);

                        JSONArray jsonArray = (JSONArray) obj;

                        jsonArray.stream().forEach(item -> {
                            String id = ((JSONObject) item).get("id").toString();
                        });

                        exchange.getIn().setBody("OK", String.class);
                    }
                });
    }
}
