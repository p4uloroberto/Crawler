package com.nosbielc.crawler.central.boot.config;


import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableFeignClients("com.nosbielc.crawler.central.eai.ws")
public class FeignConfiguration {

    private static final Logger logger = LogManager.getLogger(FeignConfiguration.class);

    @Value("${app.ws.client.username}")
    private String username;
    @Value("${app.ws.client.password}")
    private String password;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();

    @Autowired(required = false)
    private List<FeignFormatterRegistrar> feignFormatterRegistrars = new ArrayList<>();

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder decoder() {
        return new JacksonDecoder();
    }

    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        for (FeignFormatterRegistrar feignFormatterRegistrar : feignFormatterRegistrars) {
            feignFormatterRegistrar.registerFormatters(conversionService);
        }
        return conversionService;
    }

    @Bean
    public Contract feignContract(ConversionService feignConversionService) {
        return new SpringMvcContract(this.parameterProcessors, feignConversionService);
    }

    @Bean
    @Autowired
    @Scope("prototype")
    public Feign.Builder feignBuilder(Encoder encoder,
                                      Decoder decoder,
                                      Contract contract,
                                      Retryer retryer,
                                      BasicAuthRequestInterceptor basicAuthRequestInterceptor) {
        return Feign.builder()
                .logger(getLogger())
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .requestInterceptor(basicAuthRequestInterceptor)
                .retryer(retryer);
    }

    private feign.Logger getLogger() {
        return new feign.Logger() {
            @Override
            protected void log(String configKey, String format, Object... args) {
                logger.info(String.format("%s - %s% - args=%s", configKey, format, Arrays.deepToString(args)));
            }
        };
    }

}
