package com.auth.oktaauth.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:okta.properties")
@ConfigurationProperties
public class AppConfig {

	
	private static final String JSON_WEB_TOKEN_SET_URL_SUFFIX = "/.well-known/jwks.json";
	
	private String clientId;
	private String clientSecret;
	private String endpoint;
	private String issuer;
	private String identityPoolId;
	private String group;
	private String devtoken;
	private String registrationId;
	private String redirectURI;

	private String userNameField = "cognito:username";
	private String groupsField = "cognito:groups";
	private int connectionTimeout = 2000;
	private int readTimeout = 2000;
	private String httpHeader = "Authorization";
	
	
//	public String getJwkUrl() {
//		StringBuilder oktaURL = new StringBuilder();
//		oktaURL.append(JSON_WEB_TOKEN_SET_URL_SUFFIX);
//		return String.format( issuer,oktaURL.toString());
//	}
//
//	public String getCognitoIdentityPoolUrl() {
//		return String.format(COGNITO_IDENTITY_POOL_URL,region,poolId);
//	}


	public String getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getDevtoken() {
		return devtoken;
	}

	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the userNameField
	 */
	public String getUserNameField() {
		return userNameField;
	}

	/**
	 * @return the groupsField
	 */
	public String getGroupsField() {
		return groupsField;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @return the httpHeader
	 */
	public String getHttpHeader() {
		return httpHeader;
	}

	/**
	 * @return the identityPoolId
	 */
	public String getIdentityPoolId() {
		return identityPoolId;
	}

	/**
	 * @param identityPoolId the identityPoolId to set
	 */
	public void setIdentityPoolId(String identityPoolId) {
		this.identityPoolId = identityPoolId;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the poolId
	 */

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}
