package com.sjwi.saml.idp.saml2;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.springframework.core.io.ClassPathResource;

import com.sjwi.saml.idp.config.ApplicationConstants;
import com.sjwi.saml.idp.exception.InvalidSessionException;
import com.sjwi.saml.idp.exception.InvalidUserException;
import com.sjwi.saml.idp.exception.SamlBuildException;
import com.sjwi.saml.idp.exception.SamlResponseException;
import com.sjwi.saml.idp.model.SamlAttributes;

public class SamlResponseBuilder {

	private static final long ONE_MINUTE_IN_MILLIS = 60000; 
	
	/**
	 * Method to build the response 
	 * 
	 * @param request
	 * @param response
	 * @param success assert the user as successfully logged in or failed if false
	 * @throws InvalidUserException 
	 * @throws SamlResponseException 
	 * @throws InvalidSessionException 
	 */
	public String buildSamlResponse(SamlAttributes samlAttributes,  boolean success) throws SamlResponseException, InvalidUserException, InvalidSessionException {

		try {
			String strResponseXml = buildResponseXMLString(samlAttributes);
			// If login failed, set status to failed
			if (!success) {
				strResponseXml = strResponseXml.replace("urn:oasis:names:tc:SAML:2.0:status:Success",
						"urn:oasis:names:tc:SAML:2.0:status:Responder");
			}
			return strResponseXml;

		}
		catch (Exception e) {
			
			throw new SamlResponseException(e);
		}
	}
	/**
	 * Builds the XML String from the metadata template with parameters/constants
	 * 
	 * @param email  the Subject ID
	 * @param req_Id Request ID SP
	 * @param acs    post URL for sending response back to SP
	 * @return Built XML String
	 * @throws SamlBuildException 
	 * @throws Exception 
	 */
	private String buildResponseXMLString(SamlAttributes samlAttributes) throws SamlBuildException {

		try {

			Scanner scan = new Scanner(new FileInputStream(new ClassPathResource("response_template.xml").getFile()), "UTF-8");
			String strResponseXml = scan.useDelimiter("\\A").next().trim();
			scan.close();

			SecureRandomIdentifierGenerator generator = new SecureRandomIdentifierGenerator();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			return strResponseXml.replaceAll("_ASSERTION_ID", generator.generateIdentifier().trim())
					.replaceAll("_REQUEST_ID", samlAttributes.getRequestId())
					.replaceAll("_SUBJECT_NAME_ID", samlAttributes.getSubjectNameId())
					.replaceAll("_ID", generator.generateIdentifier().trim())
					.replaceAll("_ISSUE_INSTANT", dateFormat.format(new Date()) + "Z")
					.replaceAll("_ISSUER_URL", ApplicationConstants.ISSUER_URI)
					.replaceAll("_NOT_BEFORE", dateFormat.format(new Date()) + "Z")
					.replaceAll("_NOT_ON_OR_AFTER", dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis() + (5 * ONE_MINUTE_IN_MILLIS))) + "Z")
					.replaceAll("_ACS_URL", samlAttributes.getAcsUrl())
					.replaceAll("_AUDIENCE_URL", samlAttributes.getEntityId());

		} catch (Exception e) {

			throw new SamlBuildException(e);
		}

	}
}
