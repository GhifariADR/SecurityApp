package com.explore.securityApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MidtransWebClientConfiguration {

    @Value("${midtrans.server.key}")
    private String SERVER_KEY;


    @Bean
    public WebClient midtransClient() {
        return WebClient.builder()
                .baseUrl("https://api.sandbox.midtrans.com/v2/")
                .defaultHeaders(headers -> headers.setBasicAuth(SERVER_KEY, ""))
                .build();
    }
}
