package com.poddubchak.authorizationserver.Application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

        AuthenticationManager authenticationManager;


    public AuthorizationServerConfig(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {

        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();

    }



    //************************************

    //    AuthenticationManager authenticationManager;
//    KeyPair keyPair;
//
//    public AuthorizationServerConfig(
//            AuthenticationConfiguration authenticationConfiguration,
//            KeyPair keyPair) throws Exception {
//
//        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
//        this.keyPair = keyPair;
//    }
//*********************************
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        // @formatter:off
        clients.inMemory()
                .withClient("reader")
                .authorizedGrantTypes("password")
                .secret("{noop}123")
                .scopes("read")
                .accessTokenValiditySeconds(600_000_000)
                .and()
                .withClient("writer")
                .authorizedGrantTypes("password")
                .secret("{noop}123")
                .scopes("write")
                .accessTokenValiditySeconds(600_000_000);
        // @formatter:on
    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        // @formatter:off
//        endpoints
//                .authenticationManager(this.authenticationManager)
//                .accessTokenConverter(accessTokenConverter())
//                .tokenStore(tokenStore());
//        // @formatter:on
//    }
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(accessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setKeyPair(this.keyPair);
//
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
//        converter.setAccessTokenConverter(accessTokenConverter);
//
//        return converter;
//    }

}
