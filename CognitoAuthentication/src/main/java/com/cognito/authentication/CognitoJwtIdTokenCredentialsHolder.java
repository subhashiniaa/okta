package com.cognito.authentication;


/**
 * Bean that holds the IDToken associated with a specify user.
 * @version
 */
public class CognitoJwtIdTokenCredentialsHolder {

	private String idToken;
	
    public String getIdToken() {
        return idToken;
    }

    public CognitoJwtIdTokenCredentialsHolder setIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

   
}
