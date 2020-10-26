package com.sjwi.saml.idp.exception;


public class SamlResponseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1742928489896128688L;


	public SamlResponseException(String message) {
		super(message);
	}

	public SamlResponseException(Throwable cause) {
		super(cause);
		
	}

	public SamlResponseException(String message, Throwable cause) {
		super(message, cause);
	}


	public SamlResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
