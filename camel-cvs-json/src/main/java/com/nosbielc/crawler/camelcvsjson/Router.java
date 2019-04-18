package com.nosbielc.crawler.camelcvsjson;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Override
    public void configure() {

        // tag::dataFormats[]
            // Crie um formato de dados para que possamos personalizar suas opções
            CsvDataFormat csv = new CsvDataFormat();

            // Por padrão, o componente CSV lerá o primeiro
            // linha de cabeçalho.
            // Se também habilitarmos a configuração `setUseMaps`, então o
            // o componente armazenará cada linha em um mapa, de modo que:
            // Header1=Value, Header2=Value, Header3=Value, etc.
            csv.setUseMaps(true);
            //csv.setDelimiter(",");

            // Configurar um formato simples de saída JSON
            JsonDataFormat json =
                    new JsonDataFormat(JsonLibrary.Jackson);

            // Ativar a saída bem formatada para o nosso JSON
            //json.setPrettyPrint(true);
        // end::dataFormats[]

        // A lógica principal de processamento de mensagens está dentro desta rota
        // Ele expõe um endpoint direto chamado 'direct: process'

        // tag::route[]
            from("direct:process")
                    .log("Processando o arquivo")

                    // Desencadeia o CSV de entrada em um objeto Java
                    .unmarshal(csv)

                    // Marshal o objeto para uma string JSON
                    .marshal(json)

                    // Definir o nome do arquivo de saída
                    // Use o mesmo nome que o arquivo de entrada,
                    // mas com uma extensão `.json`
                    .setHeader(Exchange.FILE_NAME,
                            simple("${file:name.noext}.json"))

                    // Escreva o arquivo no nosso local de saída
                    .to("{{uri.output}}");
            // end::route[]

            // Uma rota 'borda' que recebe arquivos do nosso local de entrada,
            // então invoca a lógica de processamento principal via um endpoint direto

        // tag::edgeRoute[]
            from("{{uri.input}}")
                    .log("Recebendo o Arquivo")

                    // Envie o arquivo para o terminal 'process'
                    .to("direct:process");
        // end::edgeRoute[]
    }

}