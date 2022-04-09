package com.sjwi.saml.idp.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.util.ResourceUtils;

public class ApplicationConstants {

	/*
	 * The issuerUri can be set to whatever you want and will need to be passed to your service provider
	 */
	public static final String ISSUER_URI = "https://stephenky.com/saml-idp";
	public static final String SLO_URL = "https://stephenky.com/saml-idp/slo/saml2";
	public static final String SSO_URL = "https://stephenky.com/saml-idp/slo/saml2";
	public static final String COMPANY = "StephenKY";
	public static final String GIVEN_NAME = "Stephen";
	public static final String SUR_NAME = "Williams";
	public static final String OPERATIONS_EMAIL = "stephenjw@fastmail.com";
	public static final String AUTHORIZATION_ENDPOINT = "https://stephenky.com/saml-idp/sso/saml2";
	
	public static final String PUBLIC_CERTIFICATE_FILE = "idp-public-cert.pem";

	public static final String PRIVATE_KEY_FILE = "idp-private-key.der";
	

	public static String getPublicCertificateString() throws IOException {
        
		return new String(Files.readAllBytes(getPublicCertificate().toPath()), StandardCharsets.US_ASCII);
	}
	public static File getPublicCertificate() throws IOException {
		return ResourceUtils.getFile("classpath:" + PUBLIC_CERTIFICATE_FILE);
	}
	public static File getPrivateKeyFile() throws IOException {
		return ResourceUtils.getFile("classpath:" + PRIVATE_KEY_FILE);
	}
}
