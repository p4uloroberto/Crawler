package com.nosbielc.crawler.camelcvsjson;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "uri.output = mock:output"
})
@UseAdviceWith
public class CamelCvsJsonApplicationTests {

    @EndpointInject(uri = "mock:output")
    MockEndpoint output;

    @Produce(uri = "direct:process")
    ProducerTemplate process;

    @Autowired
    CamelContext context;

    @Test
    public void validJson() throws Exception {
        String expected = "[{\"ValorTransação\":\"5702052\",\"Descrição\":\"DescricaoTransação43374914889\",\"DataTransação\":\"2019-04-17T15:50:24.625+0000\",\"Ativo\":\"1\",\"TipoTransação\":\"2\",\"Id\":\"1\",\"Conta\":\"42161905848816\"},{\"ValorTransação\":\"6304652\",\"Descrição\":\"DescricaoTransação680209390918\",\"DataTransação\":\"2019-04-17T15:50:24.625+0000\",\"Ativo\":\"1\",\"TipoTransação\":\"0\",\"Id\":\"2\",\"Conta\":\"79167578140905\"},{\"ValorTransação\":\"874209\",\"Descrição\":\"DescricaoTransação192998560961\",\"DataTransação\":\"2019-04-17T15:50:24.625+0000\",\"Ativo\":\"1\",\"TipoTransação\":\"2\",\"Id\":\"3\",\"Conta\":\"52190510337203\"}]";

        // Esperamos que a primeira mensagem de saída seja a mesma que a string acima
        output.message(0).body().convertToString().isEqualTo(expected);

        // Agora enviamos uma mensagem de teste para seu endpoint direto: process
        process.sendBody("Id,Descrição,TipoTransação,ValorTransação,DataTransação,Ativo,Conta\n" +
                "1,DescricaoTransação43374914889,2,5702052,2019-04-17T15:50:24.625+0000,1,42161905848816\n" +
                "2,DescricaoTransação680209390918,0,6304652,2019-04-17T15:50:24.625+0000,1,79167578140905\n" +
                "3,DescricaoTransação192998560961,2,874209,2019-04-17T15:50:24.625+0000,1,52190510337203");

        // Assert que o nosso ponto final Mock está satisfeito (que a mensagem é a esperada)
        output.assertIsSatisfied();
    }

}
