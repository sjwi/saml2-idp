package com.sjwi.saml.idp.config;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opensaml.xml.io.UnmarshallerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sjwi.saml.idp.exception.KeyCertificatePairException;
import com.sjwi.saml.idp.exception.SamlParseException;
import com.sjwi.saml.idp.saml2.SamlRequestParser;
import com.sjwi.saml.idp.security.KeyCertificatePair;

@Configuration
public class SpringConfig {
	@Bean
	public SamlRequestParser samlRequestParser() throws SamlParseException {
		return new SamlRequestParser();
	}
	@Bean
	public UnmarshallerFactory unmarshallerFactory() {
		return org.opensaml.Configuration.getUnmarshallerFactory();
	}
	@Bean
	public DocumentBuilder documentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		documentBuilderFactory.setNamespaceAware(true);

		return documentBuilderFactory.newDocumentBuilder();
	}
	@Bean
	public KeyCertificatePair keyCertificatePair() throws KeyCertificatePairException {
		try {
			return new KeyCertificatePair(ApplicationConstants.getPrivateKeyFile(),ApplicationConstants.getPublicCertificateString());
		} catch (IOException e) {
			throw new KeyCertificatePairException(e);
		}
	}
}
