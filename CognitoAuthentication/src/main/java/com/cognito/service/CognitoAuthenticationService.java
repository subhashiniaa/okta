package com.cognito.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.cognito.authentication.AWSClientProviderBuilder;
import com.cognito.exception.CognitoException;
import com.cognito.model.AuthenticationRequest;
import com.cognito.model.PasswordRequest;
import com.cognito.model.PasswordResponse;
import com.cognito.model.UserResponse;
import com.cognito.security.model.SpringSecurityUser;
import com.cognito.util.AWSConfig;




/**
 * CognitoAuthenticationService.java class that contains the logic to connect to Cognito using Username and password.
 */
@Service
public class CognitoAuthenticationService {

	/**New password key*/
	private static final String NEW_PASS_WORD = "NEW_PASSWORD";
	/**New password required challenge key*/
	private static final String NEW_PASS_WORD_REQUIRED = "NEW_PASSWORD_REQUIRED";
	/**Password key*/
	private static final String PASS_WORD = "PASSWORD";
	/**Username key*/
	private static final String USERNAME = "USERNAME";



	private final Logger classLogger = LoggerFactory.getLogger(this.getClass());


	@Autowired 
	AWSClientProviderBuilder cognitoBuilder;

	@Autowired
	private AWSConfig cognitoConfig;



	/**
	 * getAmazonCognitoIdentityClient 
	 *@return
	 */
	private AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {

		return cognitoBuilder.getAWSCognitoIdentityClient();

	}

	

	/**
	 * Method that contains the logic of authentication with AWS Cognito.
	 *@param authenticationRequest
	 *@return SpringSecurityUser 
	 */
	public  SpringSecurityUser authenticate(AuthenticationRequest authenticationRequest){

		AuthenticationResultType authenticationResult = null;
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();


		try {

			final Map<String, String> authParams = new HashMap<>();
			authParams.put(USERNAME, authenticationRequest.getUsername());
			authParams.put(PASS_WORD, authenticationRequest.getPassword());

			final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
			authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
			.withClientId(cognitoConfig.getClientId())
			.withUserPoolId(cognitoConfig.getPoolId())
			.withAuthParameters(authParams);

			AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);


			//Has a Challenge
			if(StringUtils.isNotBlank(result.getChallengeName())) {

				//If the challenge is required new Password validates if it has the new password variable.
				if(NEW_PASS_WORD_REQUIRED.equals(result.getChallengeName())){

					if(null == authenticationRequest.getNewPassword()) {
						throw new CognitoException("User must provide a new password", CognitoException.USER_MUST_CHANGE_PASS_WORD_EXCEPTION_CODE, result.getChallengeName());
					}else {
						//we still need the username

						final Map<String, String> challengeResponses = new HashMap<>();
						challengeResponses.put(USERNAME, authenticationRequest.getUsername());
						challengeResponses.put(PASS_WORD, authenticationRequest.getPassword());

						//add the new password to the params map
						challengeResponses.put(NEW_PASS_WORD, authenticationRequest.getNewPassword());

						//populate the challenge response
						final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
						request.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
						.withChallengeResponses(challengeResponses)
						.withClientId(cognitoConfig.getClientId())
						.withUserPoolId(cognitoConfig.getPoolId())
						.withSession(result.getSession());

						AdminRespondToAuthChallengeResult resultChallenge = cognitoClient.adminRespondToAuthChallenge(request);
						authenticationResult = resultChallenge.getAuthenticationResult();

					}
				}else {
					//has another challenge
					throw new CognitoException(result.getChallengeName(), CognitoException.USER_MUST_DO_ANOTHER_CHALLENGE, result.getChallengeName());
				}

			}else {
				//Doesn't have a challenge
				authenticationResult = result.getAuthenticationResult();
			}

			//AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_MANAGER")
			SpringSecurityUser userAuthenticated = new SpringSecurityUser(authenticationRequest.getUsername(),authenticationRequest.getPassword(),null,null,null);
			userAuthenticated.setAccessToken(authenticationResult.getAccessToken());
			userAuthenticated.setExpiresIn(authenticationResult.getExpiresIn());
			userAuthenticated.setTokenType(authenticationResult.getTokenType());
			userAuthenticated.setRefreshToken(authenticationResult.getRefreshToken());
			userAuthenticated.setIdToken(authenticationResult.getIdToken());


			if(classLogger.isInfoEnabled()) {
				classLogger.info("User successfully authenticated userInfo: username {}", authenticationRequest.getUsername());
			}


			return userAuthenticated;
		}catch(com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(),e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch (CognitoException cognitoException) {
			throw cognitoException;
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}

	/**
	 * getUserInfo - Returns the data of the specified user.
	 *@param accessToken
	 *@return userResponse - object that contains all the cognito data.
	 */
	public UserResponse getUserInfo(String accessToken) {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		try {

			if (StringUtils.isBlank(accessToken)){
				throw new CognitoException("User must provide an access token",CognitoException.INVALID_ACCESS_TOKEN_EXCEPTION, "User must provide an access token");
			}

			GetUserRequest userRequest = new GetUserRequest()
					.withAccessToken(accessToken);

			GetUserResult userResult = cognitoClient.getUser(userRequest);

			List<AttributeType> userAttributes = userResult.getUserAttributes();
			UserResponse userResponse = getUserAttributesData(userAttributes, userResult.getUsername());

			return userResponse;


		}catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e){
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}

	/**
	 * getUserAttributesData 
	 *@param userResult
	 *@param username
	 *@return userResponse - Object filled with all the user data.
	 */
	private UserResponse getUserAttributesData(List<AttributeType> userAttributes, String username) {
		UserResponse userResponse = new UserResponse();

		userResponse.setUsername(username);

		for(AttributeType attribute : userAttributes) {
			if(attribute.getName().equals("email")) {
				userResponse.setEmail(attribute.getValue());
			}else if(attribute.getName().equals("phone_number")) {
				userResponse.setPhoneNumber(attribute.getValue());
			}else if(attribute.getName().equals("name")) {
				userResponse.setName(attribute.getValue());
			}else if(attribute.getName().equals("family_name")) {
				userResponse.setLastname(attribute.getValue());
			}else if(attribute.getName().equals("custom:companyPosition")) {
				userResponse.setCompanyPosition(attribute.getValue());
			}
		}

		return userResponse;
	}

	
	
	
	/**
	 * addUserToGroup - Adds an specific user to an specific group
	 *@param username
	 *@param groupname 
	 */
	public void addUserToGroup(String username, String groupname){
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		if(classLogger.isInfoEnabled()) {
			classLogger.info(String.format("Adding user  %1$s, to %2$s group", username, groupname));
		}

		try {

			AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
					.withGroupName(groupname)
					.withUserPoolId(cognitoConfig.getPoolId())
					.withUsername(username);

			cognitoClient.adminAddUserToGroup(addUserToGroupRequest);

			if(classLogger.isInfoEnabled()) {
				classLogger.info(String.format("User  %1$s added to %2$s group", username , groupname));
			}

		}catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e){
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}
	
	
	
	/**
	 * ResetPassword - Method that contains the logic to send reset password code for a Amazon Cognito user.
	 *@param passwordRequest
	 *@return PasswordResponse
	 */

	public PasswordResponse resetPassword(PasswordRequest passwordRequest){
		String username = passwordRequest.getUsername();
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		if(classLogger.isInfoEnabled()) {
			classLogger.info("Reset password {}", username);
		}

		try {

			//If username is blank it throws an error
			if (StringUtils.isBlank(username)) {
				throw new CognitoException("Invalid username", CognitoException.INVALID_USERNAME_EXCEPTION, "Invalid username");
			}

			ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
					.withClientId(cognitoConfig.getClientId())
					.withUsername(username);

			ForgotPasswordResult forgotPasswordResult =  cognitoClient.forgotPassword(forgotPasswordRequest);

			if(classLogger.isInfoEnabled()) {
				classLogger.info("Reset password response Delivery Details: {} ",forgotPasswordResult.getCodeDeliveryDetails());
			}

			PasswordResponse passwordResponse = new PasswordResponse();
			passwordResponse.setDestination(forgotPasswordResult.getCodeDeliveryDetails().getDestination());
			passwordResponse.setDeliveryMedium(forgotPasswordResult.getCodeDeliveryDetails().getDeliveryMedium());
			passwordResponse.setUsername(username);
			passwordResponse.setMessage("SUCCESS");


			return passwordResponse;

		}catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e){
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch(CognitoException e) {
			classLogger.error(e.getMessage(), e);
			throw e;
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}
	
	/**
	 * confirmResetPassword - Method that contains that allows a user to enter a confirmation code to reset a forgotten password.
	 *@param passwordRequest
	 *@return PasswordResponse
	 */

	public PasswordResponse confirmResetPassword(PasswordRequest passwordRequest){
		String username = passwordRequest.getUsername();
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();



		if(classLogger.isInfoEnabled()) {
			classLogger.info("Confirm Reset Password {}", username);
		}

		try {

			ConfirmForgotPasswordRequest forgotPasswordRequest = new ConfirmForgotPasswordRequest()
					.withClientId(cognitoConfig.getClientId())
					.withUsername(username)
					.withPassword(passwordRequest.getPassword())
					.withConfirmationCode(passwordRequest.getConfirmationCode());

			cognitoClient.confirmForgotPassword(forgotPasswordRequest);
			

			PasswordResponse passwordResponse = new PasswordResponse();
			passwordResponse.setUsername(username);
			passwordResponse.setMessage("SUCCESS");



			if(classLogger.isInfoEnabled()) {
				classLogger.info("Confirm Reset Password successful for {} ", username);
			}

			return passwordResponse;


		}catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e){
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		}catch(CognitoException e) {
			throw e;
		}catch(Exception e) {
			classLogger.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE,e.getMessage());
		}

	}
}
