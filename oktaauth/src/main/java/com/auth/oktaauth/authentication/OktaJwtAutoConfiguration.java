package com.auth.oktaauth.authentication;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.auth.oktaauth.security.filter.OktaIdTokenProcessor;
import com.auth.oktaauth.security.filter.OktaJwtAuthenticationFilter;
import com.auth.oktaauth.util.AppConfig;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

/**
 * Method that exposes the AWS Cognito configuration.
 * @version
 */
@Configuration
@Import(AppConfig.class)
//@ConditionalOnClass({OktaJwtAuthenticationFilter.class, OktaIdTokenProcessor.class})
public class OktaJwtAutoConfiguration {

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
    public OktaJwtIdTokenCredentialsHolder awsCognitoCredentialsHolder() {
        return new OktaJwtIdTokenCredentialsHolder();
    }

//    @Bean
//    public OktaIdTokenProcessor oktaIdTokenProcessor() { return new OktaIdTokenProcessor(); }
//
//    @Bean
//    public OktaJwtAuthenticationProvider jwtAuthenticationProvider() { return new OktaJwtAuthenticationProvider(); }


//    @Bean
//    public OktaJwtAuthenticationFilter oktaJwtAuthenticationFilter() {
//        return new OktaJwtAuthenticationFilter(oktaIdTokenProcessor());
//    }

//	@Autowired(required=true)
//	private AppConfig jwtConfiguration;

    /**
     * Method that exposes the cognito configuration. 
     *@return ConfigurableJWTProcessor 
     *@throws MalformedURLException
     */
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
//        ResourceRetriever resourceRetriever = new DefaultResourceRetriever(jwtConfiguration.getConnectionTimeout(), jwtConfiguration.getReadTimeout());
//        //https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json.
//        URL jwkSetURL = new URL(jwtConfiguration.getJwkUrl());
//        //Creates the JSON Web Key (JWK)
//        JWKSource keySource = new RemoteJWKSet(jwkSetURL, resourceRetriever);
//        ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
//        JWSKeySelector keySelector = new JWSVerificationKeySelector(RS256, keySource);
//        jwtProcessor.setJWSKeySelector(keySelector);
//        return jwtProcessor;
//    }

}