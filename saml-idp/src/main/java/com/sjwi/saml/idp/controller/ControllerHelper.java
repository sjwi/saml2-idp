package com.sjwi.saml.idp.controller;

import static com.sjwi.saml.idp.model.CookieUser.DEMO_USER_COOKIE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.saml.idp.exception.InvalidSessionException;
import com.sjwi.saml.idp.exception.SamlBuildException;
import com.sjwi.saml.idp.exception.SamlSignException;
import com.sjwi.saml.idp.model.CookieUser;
import com.sjwi.saml.idp.model.SamlAttributes;
import com.sjwi.saml.idp.saml2.SamlResponse;
import com.sjwi.saml.idp.security.KeyCertificatePair;

public class ControllerHelper {

	public static CookieUser readCookieUserFromRequest(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookieArray = request.getCookies();
		List<Cookie> cookies = cookieArray == null? new ArrayList<Cookie>(): Arrays.asList(cookieArray);
		String username = null;
		if (!cookies.stream().anyMatch(c -> c.getName().equalsIgnoreCase(DEMO_USER_COOKIE))) {
			 username = "DemoUser_" + Long.toString(System.currentTimeMillis()).substring(5,10);
			 response.addCookie(buildCustomCookie(DEMO_USER_COOKIE,username));
		} else {
			username = cookies.stream()
					.filter(c -> c.getName().equalsIgnoreCase(DEMO_USER_COOKIE))
					.map(c -> c.getValue())
					.findFirst().orElse(null);

		}
		return new CookieUser(cookies, username);
	}

	public static Cookie buildCustomCookie(String key, String value) {
		Cookie cookie = new Cookie(key,value);
		cookie.setMaxAge(60 * 60 * 24 * 7 * 52 * 10);
		cookie.setPath("/");
		return cookie;
	}
	
	public static List<SimpleGrantedAuthority> getUserAuthorities(){
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		return authorities;
	}

	public static ModelAndView getAuthenticatedSamlResponseForm(HttpServletRequest request, KeyCertificatePair keyCertificatePair) throws InvalidSessionException, SamlBuildException, SamlSignException {
		request.getSession().setAttribute("VALID_USER", "TRUE");
		SamlAttributes samlAttributes = new SamlAttributes(request);
		SamlResponse samlResponse = new SamlResponse(new SamlAttributes(request));
		samlResponse.buildSamlResponse();
		samlResponse.signSamlResponse(keyCertificatePair);
		ModelAndView mv = new ModelAndView("saml-response");
		mv.addObject("ACS_URL",samlAttributes.getAcsUrl());
		mv.addObject("RELAY_STATE",samlAttributes.getRelayState());
		mv.addObject("SAML_RESPONSE", samlResponse.getEncodedResponseString());
		clearSessionAttributes(request);
		return mv;	
	}

	private static void clearSessionAttributes(HttpServletRequest request) {
		request.getSession().removeAttribute("NAME_ID");
		request.getSession().removeAttribute("ACS");
		request.getSession().removeAttribute("RELAY_STATE");
		request.getSession().removeAttribute("ENTITY_ID");
		request.getSession().removeAttribute("REQ_ID");
		request.getSession().removeAttribute("SAMLRequest");
		request.getSession().removeAttribute("RELAY_STATE");
	}	
}
