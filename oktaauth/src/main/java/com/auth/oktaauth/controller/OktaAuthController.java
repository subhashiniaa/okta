package com.auth.oktaauth.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.oktaauth.authentication.OktaClientProviderBuilder;
import com.auth.oktaauth.bean.AuthenticationRequest;
import com.auth.oktaauth.security.config.CustomAuthenticationProvider;
import com.auth.oktaauth.service.OktaAuthenticationService;
import com.okta.authn.sdk.AuthenticationException;
import com.okta.sdk.client.Client;

@RestController
@RequestMapping("/auth")
public class OktaAuthController {
	
	
	
	@Autowired
	private OktaClientProviderBuilder oktaProvider;
	
	@Autowired 
	private CustomAuthenticationProvider provider;
	
	@Autowired
	private OktaAuthenticationService oktaService;
	
	
	
	@RequestMapping(method = POST)
	private String getUser(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
//		String username = authenticationRequest.getUsername();
//		System.out.println(username);
//		Client client=oktaProvider.getOktaCredentials();
//		String user1=client.getUser(username).toString();
//		System.out.println(user1);
		String response= oktaService.getAuthenticate(authenticationRequest);
		System.out.println(response);
		
		return response;
		
	}
	
	

}
