package com.sjwi.saml.idp.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountLifecycleController {
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = {"/destroy-user"})
	protected void deleteAccount(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            cookie.setValue("");
	            cookie.setPath("/");
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
	        }
	    }
	    request.getSession().invalidate();
	    response.sendRedirect(context.getContextPath() + "/logout");
	}

}
