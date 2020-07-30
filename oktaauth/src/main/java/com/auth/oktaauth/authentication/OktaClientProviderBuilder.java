
package com.auth.oktaauth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.auth.oktaauth.util.AppConfig;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;

/**
 * AWSClientProviderBuilder.java class that contains the logic
 */

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OktaClientProviderBuilder {

	@Autowired
	private AppConfig appConfig;

	public Client getOktaCredentials() {
		System.out.println(appConfig.getIssuer());
		Client client = Clients.builder()
				.setOrgUrl(appConfig.getIssuer())
				.setClientCredentials(new TokenClientCredentials(appConfig.getDevtoken())).build();
		
		return client;
	}
	
	public AuthenticationClient credentials() {
		AuthenticationClient cli=AuthenticationClients.builder().setOrgUrl(appConfig.getIssuer()).build();
		return cli;
	}
	

	

	

}
