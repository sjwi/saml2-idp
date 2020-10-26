package com.sjwi.saml.idp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.saml.idp.exception.InvalidSessionException;
import com.sjwi.saml.idp.exception.SamlBuildException;
import com.sjwi.saml.idp.exception.SamlParseException;
import com.sjwi.saml.idp.exception.SamlSignException;
import com.sjwi.saml.idp.model.SamlAttributes;
import com.sjwi.saml.idp.saml2.SamlRequestParser;
import com.sjwi.saml.idp.security.KeyCertificatePair;

@Controller
public class SamlController {

	@Autowired
	ServletContext context;

	@Autowired
	SamlRequestParser samlRequestParser;

	@Autowired
	KeyCertificatePair keyCertificatePair;

	private static Logger log = Logger.getLogger(SamlController.class.getName());

	/**
	 * Controller method to field in-bound SAML requests from SP
	 * 
	 * @param request
	 * @param response
	 * @return Sign-in view
	 * @throws SamlParseException 
	 * @throws IOException 
	 * @throws SamlSignException 
	 * @throws SamlBuildException 
	 * @throws InvalidSessionException 
	 */
	@RequestMapping(value = {"/sso/saml2" }, method = { RequestMethod.POST})
	protected ModelAndView parseSamlRequest(Authentication auth, HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidSessionException, SamlBuildException, SamlSignException {

		log.info("Inbound Request on SSO URL");
		ModelAndView mv = null;
		try {
			log.info("Parsing SAML Request");
			samlRequestParser.parseAuthnRequest(request);

		} catch (SamlParseException e) {
			log.info("No SAML request received");
			mv = new ModelAndView("redirect:/authenticated");
		} finally {
			if (null == auth || ("anonymousUser").equals(auth.getName())) {
				log.info("User has not logged in, sending to login page");
				mv = new ModelAndView("redirect:/login");
			} else if (mv == null) {
				log.info("Sending user for additional attributes");
				mv = new ModelAndView("redirect:/sso/saml2/response/attributes");
			}
		}
		return mv;
	}

	@RequestMapping(value = {"/sso/saml2/response/attributes" }, method = { RequestMethod.GET})
	protected ModelAndView attributeForm(Authentication auth, HttpServletRequest request, HttpServletResponse response) throws InvalidSessionException, UnsupportedEncodingException {
		ModelAndView mv = new ModelAndView("saml-attribute-form");
		mv.addObject("samlAttributes", new SamlAttributes(request));
		mv.addObject("samlRequest", new String(Base64.decodeBase64(request.getSession().getAttribute("SAMLRequest").toString().getBytes("UTF-8")), "UTF-8"));
		return mv;
	}
	@RequestMapping(value = {"/sso/saml2/respond" }, method = { RequestMethod.POST})
	protected ModelAndView postSamlResponse(Authentication auth, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="subjectNameId") String subjectNameId,
			@RequestParam(name="acsUrl") String acsUrl,
			@RequestParam(name="spEntityId") String entityId
			) throws InvalidSessionException, SamlBuildException, SamlSignException {
		request.getSession().setAttribute("NAME_ID",subjectNameId);
		request.getSession().setAttribute("ACS",acsUrl);
		request.getSession().setAttribute("ENTITY_ID",entityId);
		return ControllerHelper.getAuthenticatedSamlResponseForm(request, keyCertificatePair);
	}
	
	@ExceptionHandler({Exception.class})
	public ModelAndView exceptionHanlder(Exception e) {
		e.printStackTrace();
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("ERROR",e.getMessage());
		return mv;
	}
	
}
