package com.auth.oktaauth.authentication;


/**
 * Bean that holds the IDToken associated with a specify user.
 * @version
 */
public class OktaJwtIdTokenCredentialsHolder {

	private String idToken;
	
    public String getIdToken() {
        return idToken;
    }

    public OktaJwtIdTokenCredentialsHolder setIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

   
}
