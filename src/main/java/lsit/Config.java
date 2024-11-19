
package lsit;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class Config {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public Config(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/"))
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));
        return http.build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("{baseUrl}");
        return successHandler;
    }
}
