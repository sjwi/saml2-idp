package com.sjwi.saml.idp.saml2;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import com.sjwi.saml.idp.exception.SamlParseException;
import com.sjwi.saml.idp.model.SamlAttributes;

public class SamlRequestParser {

	/**
	 * Method to mine the HTTPServletRequest for important data
	 * 
	 * @param SAMLRequest Encoded SAML request
	 * @param request     HTTPServletRequest
	 * @return An object of request parameters
	 * @throws SamlParseException
	 * @throws Exception
	 */

	@Autowired
	private DocumentBuilder documentBuilder;

	@Autowired
	private UnmarshallerFactory unmarshallerFactory;

	public SamlAttributes parseAuthnRequest(HttpServletRequest request) throws SamlParseException {

		try {
			String samlRequest = request.getParameter("SAMLRequest") == null?
					request.getSession().getAttribute("SAMLRequest").toString(): request.getParameter("SAMLRequest");
			String relayState = request.getParameter("RelayState") == null?
					request.getSession().getAttribute("RELAY_STATE").toString(): request.getParameter("RelayState");

			DefaultBootstrap.bootstrap();

			Element element = documentBuilder
					.parse(new ByteArrayInputStream(
							new String(Base64.decodeBase64(samlRequest.getBytes("UTF-8")), "UTF-8").getBytes("UTF-8")))
					.getDocumentElement();

			AuthnRequest samlRequestObject = (AuthnRequest) unmarshallerFactory.getUnmarshaller(element)
					.unmarshall(element);

			if (samlRequestObject.getSubject() != null) {
				request.getSession().setAttribute("NAME_ID", samlRequestObject.getSubject().getNameID().getValue());
			}
			if (samlRequestObject.getID() != null) {
				request.getSession().setAttribute("REQ_ID", samlRequestObject.getID());
			}
			if (samlRequestObject.getAssertionConsumerServiceURL() != null) {
				request.getSession().setAttribute("ACS", samlRequestObject.getAssertionConsumerServiceURL());
			}
			if (samlRequestObject.getIssuer() != null) {
				request.getSession().setAttribute("ENTITY_ID", samlRequestObject.getIssuer().getValue());
			}

			request.getSession().setAttribute("RELAY_STATE", relayState);
			request.getSession().setAttribute("SAMLRequest", samlRequest);

			return new SamlAttributes(request);

		} catch (Exception e) {
			throw new SamlParseException(e);
		}
	}
}
