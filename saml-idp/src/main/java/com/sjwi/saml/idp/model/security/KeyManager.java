package com.sjwi.saml.idp.model.security;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sjwi.saml.idp.model.KeyPrompt;

public class KeyManager extends AuthenticationManager {

	public static final String DEMO_USER_KEY_COOKIE = "DEMO_USER_KEY";
	private KeyPrompt prompt = null;

	public KeyManager(String encryptedAttribute, String userName, boolean encrypted) throws RuntimeException {
		super(encryptedAttribute, userName, encrypted);
	}

	public List<String> getDecryptedKeyInFourDigitTextBlocks(String userId) throws AuthenticationException{
		String decryptedKey = this.getDecryptedUserAttribute();
		return IntStream.range(0,4)
				.mapToObj(i -> decryptedKey.substring(i*4,(i+1)*4).toUpperCase())
				.collect(Collectors.toList());
	}
	
	public static String generateNewKey() {
		return IntStream.range(0, 16)
				.mapToObj(i -> Integer.toHexString(new SecureRandom().nextInt(16)))
				.collect(Collectors.joining());
	}


	@Override
	public boolean isValidAuthenticationAttempt(String attempt) {
		return String.join("", prompt.getPrompt().stream()
					.map(k -> String.valueOf(this.getDecryptedUserAttribute().charAt(k-1)).toLowerCase())
					.collect(Collectors.toList()))
				.equals(attempt.toLowerCase());
	}

	public void setPrompt(KeyPrompt prompt) {
		this.prompt = prompt;
	}
}
