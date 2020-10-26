package com.sjwi.saml.idp.file.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility to transform documents to and from XML format
 * @author sjwi
 *
 */
public class DocumentTransformer {
	public static Document getXmlDocumentFromString(String samlResponseToSign) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new ByteArrayInputStream(samlResponseToSign.getBytes()));
	}
	/**
	 * Method translates XML document to a String
	 * 
	 * @param doc XML Document
	 * @return Document to String
	 * @throws TransformerException
	 */
	public static String getStringFromXmlDocument(Document doc) throws TransformerException {

		StringWriter writer = new StringWriter();
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(writer));

		return writer.getBuffer().toString();
	}
}
