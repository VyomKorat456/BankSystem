package com.example.account_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class OAuthClientConfig {

    @Bean
    OAuth2AuthorizedClientManager clientManager(
            ClientRegistrationRepository repo,
            OAuth2AuthorizedClientService service) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(repo, service);

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}

