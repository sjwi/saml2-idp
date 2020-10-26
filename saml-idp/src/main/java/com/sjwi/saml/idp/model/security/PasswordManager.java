package com.sjwi.saml.idp.model.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordManager extends AuthenticationManager {

	public static final String DEMO_USER_CREDENTIALS_COOKIE = "DEMO_USER_CREDENTIALS";

	public PasswordManager(String encryptedAttribute, String userName, boolean encrypted) throws RuntimeException {
		super(encryptedAttribute, userName, encrypted);
	}

	@Override
	public boolean isValidAuthenticationAttempt(String attempt) {
		return new BCryptPasswordEncoder().matches(attempt,this.getDecryptedUserAttribute());
	}

}
