package com.nosbielc.crawler.camelapidb;

import com.nosbielc.crawler.camelapidb.model.Transacao;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() {

        // tag::rest[]
        restConfiguration() // <1> Iniciar um bloco de configuração REST
                .bindingMode(RestBindingMode.json) // <2> Vincula os objetos de entrada e saída como JSON
                .apiContextPath("/api-doc") // <3> Definir o caminho para a definição de Swagger do serviço REST
                .apiProperty("api.title",
                        "Minha Coleção de Transações API");
        // end::rest[]

        // tag::route[]
        rest("/transacao") // <1> Declare um serviço REST com URI /transacao
                .description("Transação API - Fast Furious")
                .consumes("application/json")
                .produces("application/json")

                .get("/{transacaoId}").id("getTransacao")
                .description("Retorna uma Transação") // <2>  operação que espera uma transacaoId na URL
                .outType(Transacao.class)
                .route()
                .to("sql:SELECT id, descricao, tipo_transacao, vlr_transacao,"
                        + " dt_transacao, is_ativo, conta FROM transacao"
                        + " WHERE id = :#transacaoId") // <3> Usando o componente SQL para buscar uma transacao do banco de dados
                .bean(OutputProcessor.class) // <4> Bean usado para Output da requisição
                .endRest()

                .get().id("getAllTransacoes")
                .description("Lista todas as Transacoes") // <5> Operação que retorna todas as transações.
                .outType(Transacao[].class)
                .route()
                .to("sql:SELECT * FROM transacao"
                        + " ORDER BY id ASC")
                .bean(OutputProcessor.class)
                .endRest();
        // end::route[]

    }

}
