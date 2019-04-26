package com.nosbielc.crawler.camelinvokesoapservice.routers;

import com.nosbielc.crawler.camelinvokesoapservice.processors.TransacaoRequestProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Autowired
    TransacaoRequestProcessor transacaoRequestProcessor;

    @Override
    public void configure() throws Exception {

        // Pegue um arquivo específico (transacoes.csv) da nossa pasta de dados para teste.
        // Definir "noop=true" significa que Camel não fará nada para o arquivo
        // quando terminar de processá-lo.
        from("file:{{source.directory}}?fileName=transacoes.csv")
                .id("CapturaArquivoRouter::Internal")
                .description("Router responsavel por buscar arquivos para processamento")
                .log("Arquivo Recebido -> ${body}")
                .to("direct:process");

        // Encapsular toda a nossa lógica por trás de um endpoint direct
        from("direct:process")
                .id("InvokeSoapserviceRouter::Internal")
                .description("Router que realiza integração com SOAP Service")
                // Unmarshal the CSV into a Java object (a List)
                .unmarshal().csv()

                // Divida a lista e processe cada linha
                .split()
                // Isso diz ao Camel que ele deve usar o mais
                // método natural para dividir o corpo (ou seja, por elementos da lista)
                .body()

                // Diga ao Camel qual operação SOAP queremos invocar.
                // Isso está definido no nosso arquivo application.properties
                .setHeader(CxfConstants.OPERATION_NAME,
                        simple("{{webservice.operation}}"))

                // Construa nossa carga útil de solicitação, criando um novo objeto
                // do tipo UpdateTransacaoRequestType e, em seguida, defini-lo em
                // o corpo da mensagem
                .process(transacaoRequestProcessor)

                .log("Prestes a invocar o serviço SOAP para transacao: "
                        + "FirstName = ${body.transacao.firstName}, "
                        + "LastName = ${body.transacao.lastName}")

                // Invoque o serviço SOAP, usando o corpo da mensagem como
                // objeto de solicitação
                // Para a opção serviceClass, especificamos a interface
                // que foi gerado para nós por cxf-codegen:wsdl2java
                .to("cxf://{{webservice.address}}"
                        + "?serviceClass=crawler.nosbielc.com.transacaoservice.Transacao"
                        + "&wsdlURL=/wsdl/transacaoservice.wsdl")

                // Após invocar o serviço SOAP, o Camel coloca a resposta no
                // corpo do Exchange, como um objeto do tipo
                // org.apache.cxf.message.MessageContentsList.
                // O objeto de resposta SOAP é o primeiro item dessa lista.

                // Podemos usar a linguagem simples do Camel para obter elementos da resposta.
                .log("Serviço SOAP retornou status = ${body[0].status}")
                .end();
    }
}
