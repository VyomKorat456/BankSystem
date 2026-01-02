package com.example.account_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
        WebClient transactionWebClient(OAuth2AuthorizedClientManager manager, WebClient.Builder builder) {

        var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultClientRegistrationId("transaction-client");

        return builder
                .apply(oauth.oauth2Configuration())
                .baseUrl("http://TRANSACTION-SERVICE")
                .build();
    }

    @Bean
    WebClient authWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://AUTH-SERVICE")
                .build();
    }

    @Bean
    @org.springframework.cloud.client.loadbalancer.LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
