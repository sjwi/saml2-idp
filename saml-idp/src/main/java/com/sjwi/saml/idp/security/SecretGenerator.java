package com.sjwi.saml.idp.security;

import java.security.KeyException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SecretGenerator {
	private final static int HASH_ITERATIONS = 10001;
	private final static int HASH_KEY_LENGTH = 64;

	private String secret;

	public SecretGenerator(String id) throws Exception {
		secret = hashKeyWithSalt(id, new SaltGenerator(id).getSalt());
	}

	private static String hashKeyWithSalt(String key, String salt) throws Exception {
		return Hex.encodeHexString(getEncodedByteHashWithSalt(key.toCharArray(), salt.getBytes(), HASH_ITERATIONS, HASH_KEY_LENGTH));
	}

	/**
	 * Hash function
	 * 
	 * @param key      password value to hash
	 * @param salt       salt to hash with
	 * @param iterations number of iterations
	 * @param keyLength  byte length of hash
	 * @return
	 * @throws KeyException 
	 */
	private static byte[] getEncodedByteHashWithSalt(final char[] key, final byte[] salt, final int iterations, final int keyLength) throws Exception {

		return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(new PBEKeySpec(key, salt, iterations, keyLength)).getEncoded();
	}

	public String getSecret() {
		return secret;
	}
}
