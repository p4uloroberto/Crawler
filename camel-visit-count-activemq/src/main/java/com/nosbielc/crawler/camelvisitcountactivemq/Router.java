package com.nosbielc.crawler.camelvisitcountactivemq;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Cria um serviço da Web simples usando o servidor da Web Jetty
        from("jetty:http://localhost:{{router.web.port}}/hello")
                .id("WebAppRouter::Internal")
                .description("Cria um serviço da Web simples usando o servidor da Web Jetty")

                // Registre o timestamp da visita em um cabeçalho
                .setHeader("RequestDate", simple("${date:now}"))
                .log("Visita web recebida, envio de visita a fila")

                // Enviar a mensagem para uma fila do ActiveMQ
                .to("activemq:queue:ACCESS_LOG")

                //Retornar o conteúdo de um arquivo de texto para o usuário
                .setBody(simple("resource:classpath:data/greeting.txt"));

        // Receber mensagens da fila de log
        from("activemq:queue:ACCESS_LOG")
                .id("ActiveMQQueueRouter::Internal")
                .description("Receber mensagens da fila de log")

                .log("Mensagem recebida da fila, salvando no banco de dados")

                // Seta os parametros que serão usados na query dentro do body
                // e.g. param1,param2,param3 ...
                .setBody(simple("${header.RequestDate},${header.CamelHttpUrl}"))

                // Execute a instrução SQL INSERT
                .to("sql:INSERT INTO visits ( visit_date, url ) VALUES ( #, # )");

        // Periodicamente, escreva o número total de visitas ao registro
        from("timer:ListarQTVisitasTimer?period=5000")
                .id("ListarQTVisitasTimerRouter::Internal")
                .description("Periodicamente, escreva o número total de visitas ao registro")

                .to("sql:SELECT COUNT(*) AS visit_count FROM visits")
                .log("Visitas gravadas no Banco de Dados - ${body[0][VISIT_COUNT]}");

    }

}
