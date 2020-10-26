package com.sjwi.saml.idp.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class MetadataInitializer {
	
	@Autowired
	ServletContext context;
	
	@PostConstruct
	public void initializeMetadata() throws FileNotFoundException, IOException {
		String metadata = new String(Files.readAllBytes(ResourceUtils.getFile("classpath:metadata_template.xml").toPath()), StandardCharsets.US_ASCII)
				.replace("{{XSL_ID}}", "ID_" + System.currentTimeMillis() + new Random().nextLong())
				.replace("{{IDP_ENTITY_ID}}", ApplicationConstants.ISSUER_URI)
				.replace("{{X509_CERT}}", ApplicationConstants.getPublicCertificateString()
						.replace("-----BEGIN CERTIFICATE-----", "")
						.replace("-----END CERTIFICATE-----", "").trim())
				.replace("{{SLO_URL}}", ApplicationConstants.SLO_URL)
				.replace("{{SSO_URL}}", ApplicationConstants.SSO_URL)
				.replace("{{COMPANY}}", ApplicationConstants.COMPANY)
				.replace("{{GIVEN_NAME}}", ApplicationConstants.GIVEN_NAME)
				.replace("{{SUR_NAME}}", ApplicationConstants.SUR_NAME)
				.replace("{{OPERATIONS_EMAIL}}", ApplicationConstants.OPERATIONS_EMAIL);
		String metadataFilePath = context.getRealPath("/") + "files/metadata.xml";
		new File(metadataFilePath).createNewFile(); 
		PrintWriter writer = new PrintWriter(metadataFilePath);
		writer.print(metadata);
		writer.close();
	}
}
