package com.auth.oktaauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;

import com.auth.oktaauth.authentication.OktaClientProviderBuilder;
import com.auth.oktaauth.bean.AuthenticationRequest;
import com.auth.oktaauth.util.AppConfig;
import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationFailureException;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.client.AuthenticationClientBuilder;
import com.okta.authn.sdk.client.AuthenticationClients;
import com.okta.authn.sdk.example.ExampleAuthenticationStateHandler;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.sdk.client.Client;
import com.okta.sdk.impl.config.ClientConfiguration;

@Service
public class OktaAuthenticationService {

	@Autowired
	private AppConfig appconfig;

	@Autowired(required = false)
	OAuth2AuthorizationCodeAuthenticationProvider oauth;
	
	private AuthenticationManager authenticationManager;

	@Autowired
	private OktaClientProviderBuilder oktaBuilder;
	
	@Autowired(required = false)
	private AuthService authservice;

//	private OAuth2AuthorizedClientService clientService;

	private Client getOktaClient() {
		return oktaBuilder.getOktaCredentials();
	}

//	private AuthenticationClient authenticationClient;
//	
//	@Autowired
//	private CustomAuthenticationClient customAuth;
//	
//	
//    public void LoginResource(AuthenticationClient authenticationClient) {
//        this.authenticationClient = authenticationClient;
//    }

	public String getAuthenticate(AuthenticationRequest authRequest) throws AuthenticationException{

		AuthenticationResponse authResponse = null;
//		Client client = getOktaClient();
		System.out.println(appconfig.getRegistrationId());
		ClientRegistration.withRegistrationId(appconfig.getRegistrationId())
		.clientId(appconfig.getClientId())
		.clientSecret(appconfig.getClientSecret())
		.redirectUriTemplate(appconfig.getIssuer())
		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		.authorizationUri(appconfig.getRedirectURI())
		.tokenUri(appconfig.getRedirectURI())
		.build();
		
		char[] pass=authRequest.getPassword().toCharArray();
//		ClientConfiguration con=new ClientConfiguration();
//		con.setRetryMaxElapsed(0);
//		con.setRetryMaxElapsed(0);
		
		AuthenticationClient client = AuthenticationClients.builder()
				   .setOrgUrl("https://dev-852780.okta.com")
				   .build();
		authResponse=client.authenticate(authRequest.getUsername(), pass, null, new  ExampleAuthenticationStateHandler());
		System.out.println(authRequest.getUsername());
		
		try {
		
		System.out.println(pass);
		authResponse=authservice.authenticate(authRequest.getUsername(), pass, null, new  ExampleAuthenticationStateHandler());
		} catch (AuthenticationFailureException e) {
			e.printStackTrace();
			e.getMessage();
        return "authenticate";
    }
			return "success";

		

	}

}
