package com.sjwi.saml.idp.exception;


public class SamlSignException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1742928489896128688L;


	public SamlSignException(String message) {
		super(message);
	}

	public SamlSignException(Throwable cause) {
		super(cause);
		
	}

	public SamlSignException(String message, Throwable cause) {
		super(message, cause);
	}


	public SamlSignException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
