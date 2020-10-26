package com.sjwi.saml.idp.model.security;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.sjwi.saml.idp.security.SecretGenerator;

public abstract class AuthenticationManager {

	protected final String encryptedAttribute;
	protected final String userName;
	
	public AuthenticationManager(String encryptedAttribute, String userName, boolean encrypted) throws RuntimeException {
		this.userName = userName;
		if (encrypted) {
			this.encryptedAttribute = encryptedAttribute;
		} else {
			try {
				this.encryptedAttribute = encryptUserAttribute(encryptedAttribute,userName);
			} catch (Exception e) {
				throw new RuntimeException("Unable to encrypt user attribute");
			}
		}
	}
	
	public abstract boolean isValidAuthenticationAttempt(String attempt);

	private String encryptUserAttribute(String decryptedAttribute, String userId) throws Exception {
		java.security.Key aesKey = new SecretKeySpec(new SecretGenerator(userId).getSecret().getBytes(),"AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(decryptedAttribute.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	}
	
	public String getDecryptedUserAttribute() throws AuthenticationException {
		
		try {
			Cipher cipher = Cipher.getInstance("AES");

			java.security.Key aesKey = new SecretKeySpec(new SecretGenerator(userName).getSecret().getBytes(),"AES");

			byte[] decodedString = org.apache.commons.codec.binary.Base64.decodeBase64(encryptedAttribute.getBytes("UTF-8"));

			cipher.init(Cipher.DECRYPT_MODE, aesKey);

			return new String(cipher.doFinal(decodedString));
		}
		catch (Exception e) {
			throw new AuthenticationException("Unable to decrypt attribute",e);
		}

	}

	public String getEncryptedUserAttribute() {
		return encryptedAttribute;
	}

	protected class AuthenticationException extends RuntimeException{

		private static final long serialVersionUID = 1L;
		public AuthenticationException(String s, Exception e) {
			super (s,e);
		}
		
	}
}
