package com.list_project.lsit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Config {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public Config(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/supplier/**", "/warehouse/**", "/delivery/**", "/customer/**")
                            .permitAll()
                        .anyRequest()
                            .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/"))
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("{baseUrl}");
        return successHandler;
    }
}
