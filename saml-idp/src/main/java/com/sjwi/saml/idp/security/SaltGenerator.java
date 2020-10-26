package com.sjwi.saml.idp.security;

public class SaltGenerator {

	private final static int MOD_LIMIT = 1000000;
	private String salt;

	public SaltGenerator(String seed){
		salt = Integer.toString(customModulation(seed.chars().sum()));
	}

	private int customModulation(int ascii) {
		if (ascii > MOD_LIMIT) {
			return ascii;
		}
		return customModulation((int) ((((((ascii - 1) / 3.1) * 14.2) + 45) / 2) * 3.9));
	}

	public String getSalt() {
		return salt;
	}
}
