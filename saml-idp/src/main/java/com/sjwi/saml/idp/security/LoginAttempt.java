package com.sjwi.saml.idp.security;

public class LoginAttempt {
	
	private boolean userLoggedIn;

	public LoginAttempt(String password, String username) {
		//custom logic required to handle login request
		userLoggedIn = password.equals("admin");
	}
	public boolean isValidLogin() {
		return userLoggedIn;
	}
}
