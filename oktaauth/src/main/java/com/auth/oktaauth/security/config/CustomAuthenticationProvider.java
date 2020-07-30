package com.auth.oktaauth.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import com.auth.oktaauth.service.OktaAuthenticationService;

/**
 * Custom Authentication that manipulates the logic to call the authentication
 * with Cognito.
 */
@Component
public class CustomAuthenticationProvider 
implements AuthenticationProvider {

	@Autowired
	OktaAuthenticationService cognitoService;
	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient= null;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) {
		OAuth2AuthorizationCodeAuthenticationToken authorizationCodeAuthentication =
				(OAuth2AuthorizationCodeAuthenticationToken) authentication;

//			OAuth2AuthorizationExchangeValidator.validate(
//				authorizationCodeAuthentication.getAuthorizationExchange());

			OAuth2AccessTokenResponse accessTokenResponse =
				this.accessTokenResponseClient.getTokenResponse(
					new OAuth2AuthorizationCodeGrantRequest(
						authorizationCodeAuthentication.getClientRegistration(),
						authorizationCodeAuthentication.getAuthorizationExchange()));

			OAuth2AuthorizationCodeAuthenticationToken authenticationResult =
				new OAuth2AuthorizationCodeAuthenticationToken(
					authorizationCodeAuthentication.getClientRegistration(),
					authorizationCodeAuthentication.getAuthorizationExchange(),
					accessTokenResponse.getAccessToken(),
					accessTokenResponse.getRefreshToken(),
					accessTokenResponse.getAdditionalParameters());
			authenticationResult.setDetails(authorizationCodeAuthentication.getDetails());

			return authenticationResult;


		
	}
	
	
	

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(
				UsernamePasswordAuthenticationToken.class);
	}


	
	
	
}
