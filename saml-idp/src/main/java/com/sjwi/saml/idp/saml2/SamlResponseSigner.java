package com.sjwi.saml.idp.saml2;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sjwi.saml.idp.exception.SamlSignException;
import com.sjwi.saml.idp.file.xml.DocumentTransformer;
import com.sjwi.saml.idp.security.KeyCertificatePair;


/**
 * Utility to sign the SAML objects with server keys
 * @author sjwi
 *
 */
public class SamlResponseSigner {

	private static final String ASSERTION_EXPRESSION = "//*[name()='samlp:Response']//*[name()='saml:Assertion']";
	private static final String ASSERTION_EXPRESSION_ID = "//*[local-name()='Response']//*[local-name()='Assertion']//@ID";
	private static final String ASSERTION_EXPRESSION_SUBJECT = "//*[local-name()='Response']//*[local-name()='Assertion']//*[local-name()='Subject']";
	private static final String RESPONSE_EXPRESSION = "//*[name()='samlp:Response']";
	private static final String RESPONSE_EXPRESSION_ID = "//*[local-name()='Response']//@ID";
	private static final String RESPONSE_EXPRESSION_SUBJECT = "//*[local-name()='Response']//*[local-name()='Status']";
	
	private KeyCertificatePair keyCertPair;
	/**
	 * Method signs the SAML response with server certificates
	 * 
	 * @param strResponseXML The XML response to be signed
	 * @return The signed SAML response in the form of a string
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws SamlSignException 
	 * @throws Exception 
	 */
	public SamlResponseSigner(KeyCertificatePair keyCertPair) throws SamlSignException {
		this.keyCertPair = keyCertPair;
	}
	
	public String signSamlResponseDocument(Document samlResponseToSign) throws SamlSignException{
		try {
			return signSamlDocument(samlResponseToSign);

		} catch (Exception e) {
			throw new SamlSignException("Exception while attempting to sign the SAML Response.",e);
		}
	}

	public String signSamlResponseString(String samlResponseToSign) throws SamlSignException{
		try {
			return signSamlDocument(DocumentTransformer.getXmlDocumentFromString(samlResponseToSign));

		} catch (Exception e) {
			throw new SamlSignException("Exception while attempting to sign the SAML Response.",e);
		}
	}
	
    private String signSamlDocument(Document samlDocumentToSign) throws Exception {

		signSamlParts(samlDocumentToSign, 0); //Sign assertion (signerCode 0)
		signSamlParts(samlDocumentToSign, 1); //Sign response (signerCode 1)

		return DocumentTransformer.getStringFromXmlDocument(samlDocumentToSign);
	}

	/**
     * Sign an Assertion using the Private Key & Public Certificate from a KeyStore.
	 * @param samlDocumentToSign 
     * @param doc XML Document to be signed
     * @param privKey Private Key 
     * @param cert Public Certificate
     * @return Signed XML Document
     * @throws Exception
     */
    private void signSamlParts(Document samlDocumentToSign, int signerCode) throws Exception {

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        XPathFactory xPathfactory = XPathFactory.newInstance();

        XPath xpath = xPathfactory.newXPath();
        XPathExpression exprAssertion = xpath.compile(signerCode == 0 ? ASSERTION_EXPRESSION : RESPONSE_EXPRESSION);
        Element assertionNode = (Element) exprAssertion.evaluate(samlDocumentToSign, XPathConstants.NODE);   
        
        assertionNode.setIdAttribute("ID", true);

        XPathExpression exprAssertionID = xpath.compile(signerCode == 0 ? ASSERTION_EXPRESSION_ID : RESPONSE_EXPRESSION_ID);
        String assertionID = (String) exprAssertionID.evaluate(samlDocumentToSign, XPathConstants.STRING);

        XPathExpression exprAssertionSubject = xpath.compile(signerCode == 0 ? ASSERTION_EXPRESSION_SUBJECT : RESPONSE_EXPRESSION_SUBJECT);
        Node insertionNode = (Node) exprAssertionSubject.evaluate(samlDocumentToSign, XPathConstants.NODE);        

        DOMSignContext dsc = new DOMSignContext(keyCertPair.getPrivateKey(), assertionNode, insertionNode);

        CanonicalizationMethod canonicalizationMethod = fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);
        
        SignatureMethod signatureMethod = fac.newSignatureMethod(org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256, null);

        List<Transform> transformList = getTransformList(fac,signerCode);

        Reference reference = fac.newReference("#" + assertionID, fac.newDigestMethod(DigestMethod.SHA256, null), transformList, null, null);        
        List<Reference> referenceList = Collections.singletonList(reference);                
       
        SignedInfo si = fac.newSignedInfo(canonicalizationMethod, signatureMethod, referenceList);

        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
        x509Content.add(keyCertPair.getPublicCertificate());
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        XMLSignature signature = fac.newXMLSignature(si, ki);
        
        signature.sign(dsc);
    }
    
    private List<Transform> getTransformList(XMLSignatureFactory fac, int signerCode) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{

		List<Transform> transformList = new ArrayList<Transform>(1);
		transformList.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));  
        if (signerCode == 0) {
			TransformParameterSpec transformSpec = null;
			Transform exc14nTransform = fac.newTransform("http://www.w3.org/2001/10/xml-exc-c14n#", transformSpec);
			transformList.add(exc14nTransform);
        }
        return transformList;
    }
    
}