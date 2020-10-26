package com.sjwi.saml.idp.exception;


public class InvalidSessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5702453907061100833L;
	

	public InvalidSessionException(String message) {
		super(message);
	}

	public InvalidSessionException(Throwable cause) {
		super(cause);
	}

	public InvalidSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
