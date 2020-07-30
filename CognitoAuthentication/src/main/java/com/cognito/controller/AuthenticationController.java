
package com.cognito.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cognito.model.AuthenticationRequest;
import com.cognito.model.AuthenticationResponse;
import com.cognito.model.PasswordRequest;
import com.cognito.model.PasswordResponse;
import com.cognito.model.UserResponse;
import com.cognito.service.CognitoAuthenticationService;
import com.cognito.util.Constants;
import com.nimbusds.jose.JOSEException;

/**
 * SpringBoot Authentication Controller
 * @version 1.0
 */

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired(required = false)
	private AuthenticationManager authenticationManager;

	@Autowired(required = false)
	private CognitoAuthenticationService authService;

	/**
	 * Spring Controller that has the logic to authenticate.
	 * @param authenticationRequest
	 * @return ResponseEntity<?>
	 * @throws AuthenticationException
	 * @throws IOException
	 * @throws JOSEException
	 */
	@SuppressWarnings("unchecked")
	@CrossOrigin
	@RequestMapping(method = POST)
	public ResponseEntity<AuthenticationResponse> authenticationRequest(
			@RequestBody AuthenticationRequest authenticationRequest) {

		String expiresIn = null;
		String token = null;
		String accessToken = null;
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		String newPassword = authenticationRequest.getNewPassword();
		AuthenticationResponse authenticationResponse = null;

		Map<String, String> credentials = new HashMap<>();
		credentials.put(Constants.PASS_WORD_KEY, password);
		credentials.put(Constants.NEW_PASS_WORD_KEY, newPassword);

		// throws authenticationException if it fails !
		Authentication authentication = this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, credentials));

		Map<String, String> authenticatedCredentials = (Map<String, String>) authentication.getCredentials();
		token = authenticatedCredentials.get(Constants.ID_TOKEN_KEY);
		expiresIn = authenticatedCredentials.get(Constants.EXPIRES_IN_KEY);
		accessToken = authenticatedCredentials.get(Constants.ACCESS_TOKEN_KEY);

		UserResponse userResponse = authService.getUserInfo(accessToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		authenticationResponse = new AuthenticationResponse(token, expiresIn, accessToken, userResponse);
		authenticationResponse.setAccessToken(token);
		authenticationResponse.setExpiresIn(expiresIn);
		authenticationResponse.setSessionToken(accessToken);
		authenticationResponse.setUserData(userResponse);

		// Return the token
		return ResponseEntity.ok(new AuthenticationResponse(token, expiresIn, accessToken, userResponse));

	}

	/**
	 * resetPassword - Method that reset password -user into Amazon Cognito
	 * @param authenticationRequest
	 * @return ResponseEntity<PasswordResponse>
	 */

	@RequestMapping(value = "/ResetPassword", method = RequestMethod.POST)
	public ResponseEntity<PasswordResponse> resetPassword(@RequestBody PasswordRequest resetPasswordRequest) {

		// Calls the service that Signs up an specific User
		PasswordResponse response = authService.resetPassword(resetPasswordRequest);

		return ResponseEntity.ok(response);

	}
	/**
		 * confirmResetPassword - Method that confirm password reset -user into Amazon
		 * Cognito
		 * @param authenticationRequest
		 * @return ResponseEntity<PasswordResponse>
		 */
		@RequestMapping(value = "/ConfirmResetPassword", method = RequestMethod.POST)
		public ResponseEntity<PasswordResponse> confirmResetPassword(@RequestBody PasswordRequest resetPasswordRequest) {
	
			// Calls the service that Signs up an specific User
			PasswordResponse response = authService.confirmResetPassword(resetPasswordRequest);
	
			return ResponseEntity.ok(response);
	
		}
	

}
