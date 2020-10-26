package com.sjwi.saml.idp.security;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import org.opensaml.xml.security.SecurityHelper;

import com.sjwi.saml.idp.config.ApplicationConstants;
import com.sjwi.saml.idp.exception.KeyCertificatePairException;

public class KeyCertificatePair {

	private PrivateKey privateKey;
	private X509Certificate publicCertificate;
	
	public KeyCertificatePair(File privateKeyFile, String publicCertificateString) throws KeyCertificatePairException {
		this.privateKey = getPrivateKey(privateKeyFile);
		this.publicCertificate = getPublicCertificate(publicCertificateString);
	}
	
	/**
	 * Method builds the private key from a File
	 * 
	 * @param privateKeyFile Private key.der file
	 * @return Generated java.security.PrivateKey from .der file
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(File privateKeyFile) throws KeyCertificatePairException {
		
		try {
			FileInputStream fis = new FileInputStream(privateKeyFile);
			DataInputStream dis = new DataInputStream(fis);
			byte[] keyBytes = new byte[(int) privateKeyFile.length()];
			dis.readFully(keyBytes);
			dis.close();

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");

			return kf.generatePrivate(spec);
		} catch (Exception e){
			throw new KeyCertificatePairException("Unable to find a valid " + ApplicationConstants.PRIVATE_KEY_FILE + " file in the resources directory. A private key file in the Distinguished Encoding Rules (DER) format is required for the application to run.",e);
		} 
	}

	private X509Certificate getPublicCertificate(String publicCertificateString) throws KeyCertificatePairException {

		try {
			return SecurityHelper.buildJavaX509Cert(
					publicCertificateString
					.replace("-----BEGIN CERTIFICATE-----", "")
					.replace("-----END CERTIFICATE-----", "").trim());
		} catch (Exception e){
			throw new KeyCertificatePairException("Unable to find a valid " + ApplicationConstants.PUBLIC_CERTIFICATE_FILE + " file in the resources directory. A public PEM-encoded X509 certificate file is required for the application to run.",e);
		} 
	}

	public X509Certificate getPublicCertificate() {
		return publicCertificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
}
