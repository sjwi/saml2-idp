package com.sjwi.saml.idp.saml2;

import java.util.Base64;

import com.sjwi.saml.idp.exception.InvalidSessionException;
import com.sjwi.saml.idp.exception.InvalidUserException;
import com.sjwi.saml.idp.exception.SamlBuildException;
import com.sjwi.saml.idp.exception.SamlResponseException;
import com.sjwi.saml.idp.exception.SamlSignException;
import com.sjwi.saml.idp.model.SamlAttributes;
import com.sjwi.saml.idp.security.KeyCertificatePair;

public class SamlResponse {
	private final SamlAttributes samlAttributes;
	private String responseString;

	public SamlResponse(SamlAttributes samlAttributes) {
		this.samlAttributes = samlAttributes;
	}
	
	public void buildSamlResponse() throws SamlBuildException {
		buildSamlResponse(true);
	}
	
	public void buildSamlResponse(boolean success) throws SamlBuildException {
		try {
			responseString = new SamlResponseBuilder().buildSamlResponse(samlAttributes, success);
		} catch (InvalidUserException | SamlResponseException | InvalidSessionException e) {
			throw new SamlBuildException(e);
		}
	}
	
	public void signSamlResponse(KeyCertificatePair keyCertPair) throws SamlSignException {
		responseString = new SamlResponseSigner(keyCertPair).signSamlResponseString(responseString);
	}
	
	public String getResponseString() {
		return responseString;
	}
	public String getEncodedResponseString() {
		return Base64.getEncoder().encodeToString(responseString.getBytes()).trim();
	}
	
}
