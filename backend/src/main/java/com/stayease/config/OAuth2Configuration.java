package com.stayease.config;

// TEMPORARILY DISABLED - Uncomment when you have Auth0 configured
/*
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class OAuth2Configuration {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }
}
*/

/**
 * This configuration is currently disabled.
 * To enable OAuth2 with Auth0:
 * 1. Set up your Auth0 tenant
 * 2. Update .env with your Auth0 credentials
 * 3. Uncomment the configuration above
 * 4. Update application.yml with the issuer-uri
 */
public class OAuth2Configuration {
    // Placeholder class - OAuth2 disabled for now
}