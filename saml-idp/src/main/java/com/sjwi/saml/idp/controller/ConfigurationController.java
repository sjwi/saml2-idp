package com.sjwi.saml.idp.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

@Controller
public class ConfigurationController {
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value="/sso/saml2/metadata")
	@ResponseBody
	protected void generateMetaData(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, CertificateEncodingException, TransformerFactoryConfigurationError, TransformerException, SAXException, ParserConfigurationException {
		response.setContentType("text/xml; name=\"federation_metadata.xml\"");
        response.addHeader("Content-Disposition", "inline; filename=\"federation_metadata.xml\"");
        Files.copy(Paths.get(context.getRealPath("/") + "files/metadata.xml"), response.getOutputStream());
	}
	
	@RequestMapping(value="/config")
	@ResponseBody
	protected ModelAndView configuration(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException, CertificateEncodingException, TransformerFactoryConfigurationError, TransformerException, SAXException, ParserConfigurationException {
		return new ModelAndView("config");
	}

}
