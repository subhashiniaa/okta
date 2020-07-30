package com.auth.oktaauth.service;

import com.okta.authn.sdk.AuthenticationException;
import com.okta.authn.sdk.AuthenticationStateHandler;
import com.okta.authn.sdk.client.AuthenticationClient;
import com.okta.authn.sdk.http.RequestContext;
import com.okta.authn.sdk.resource.ActivateFactorRequest;
import com.okta.authn.sdk.resource.AuthenticationRequest;
import com.okta.authn.sdk.resource.AuthenticationResponse;
import com.okta.authn.sdk.resource.ChangePasswordRequest;
import com.okta.authn.sdk.resource.FactorEnrollRequest;
import com.okta.authn.sdk.resource.RecoverPasswordRequest;
import com.okta.authn.sdk.resource.RecoveryQuestionAnswerRequest;
import com.okta.authn.sdk.resource.UnlockAccountRequest;
import com.okta.authn.sdk.resource.VerifyFactorRequest;
import com.okta.authn.sdk.resource.VerifyRecoveryRequest;
import com.okta.sdk.ds.DataStore;
import com.okta.sdk.resource.Resource;
import com.okta.sdk.resource.user.factor.FactorProfile;
import com.okta.sdk.resource.user.factor.FactorProvider;
import com.okta.sdk.resource.user.factor.FactorType;

public class AuthService implements AuthenticationClient{

	@Override
	public DataStore getDataStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Resource> T instantiate(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse authenticate(String username, char[] password, String relayState,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		return authenticate(instantiate(AuthenticationRequest.class)
                .setUsername(username)
                .setPassword(password)
                .setRelayState(relayState),
             stateHandler);
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse changePassword(char[] oldPassword, char[] newPassword, String stateToken,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse resetPassword(char[] newPassword, String stateToken,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse resetPassword(ChangePasswordRequest changePasswordRequest,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse enrollFactor(FactorType factorType, FactorProvider factorProvider,
			FactorProfile factorProfile, String stateToken, AuthenticationStateHandler stateHandler)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse enrollFactor(FactorEnrollRequest factorEnrollRequest, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse recoverPassword(String username, FactorType factorType, String relayState,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse recoverPassword(RecoverPasswordRequest request, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse unlockAccount(String username, FactorType factorType, String relayState,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse unlockAccount(UnlockAccountRequest request, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse answerRecoveryQuestion(String answer, String stateToken,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse answerRecoveryQuestion(RecoveryQuestionAnswerRequest request,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse previous(String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse skip(String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse cancel(String stateToken, RequestContext requestContext)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse activateFactor(String factorId, ActivateFactorRequest request,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse verifyFactor(String factorId, VerifyFactorRequest request,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse verifyFactor(String factorId, String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse challengeFactor(String factorId, String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse verifyUnlockAccount(FactorType factorType, VerifyRecoveryRequest request,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse resendActivateFactor(String factorId, String stateToken,
			RequestContext requestContext, AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse resendVerifyFactor(String factorId, String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse verifyActivation(String factorId, String stateToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthenticationResponse verifyRecoveryToken(String recoveryToken, RequestContext requestContext,
			AuthenticationStateHandler stateHandler) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

}
