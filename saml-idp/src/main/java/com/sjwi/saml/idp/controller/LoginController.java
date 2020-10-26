package com.sjwi.saml.idp.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.saml.idp.exception.InvalidLoginException;
import com.sjwi.saml.idp.exception.InvalidSessionException;
import com.sjwi.saml.idp.exception.InvalidUserException;
import com.sjwi.saml.idp.exception.SamlBuildException;
import com.sjwi.saml.idp.exception.SamlResponseException;
import com.sjwi.saml.idp.exception.SamlSignException;
import com.sjwi.saml.idp.model.CookieUser;
import com.sjwi.saml.idp.model.KeyPrompt;
import com.sjwi.saml.idp.model.security.GridManager;
import com.sjwi.saml.idp.security.KeyCertificatePair;

@Controller
public class LoginController {

	@Autowired
	ServletContext context;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	KeyCertificatePair keyCertificatePair;

	/**
	 * Login landing page
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = {"/","/login", "/slo/saml2"})
	protected ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		if (null != auth && !("anonymousUser").equals(auth.getName())) {
			response.sendRedirect(context.getContextPath() + "/authenticated");
		}
		ModelAndView mv = new ModelAndView("prompt-for-login");
		mv.addObject("USERNAME",cookieUser.getUsername());
		mv.addObject("PW_ENROLLED",cookieUser.getPasswordManager() != null);
		if (cookieUser.getKeyManager() != null) {
			KeyPrompt prompt = new KeyPrompt();
			mv.addObject("PROMPT",prompt.getPrompt());
			mv.addObject("KEY_ENROLLED",true);
			request.getSession().setAttribute("HEX", prompt.toString());
		}
		if (cookieUser.getGridManager() != null) {
			mv.addObject("GRID_ENROLLED",true);
			List<List<String>> grid = GridManager.getHashedGrid();
			request.getSession().setAttribute("GRID_KEY", GridManager.gridToString(grid));
			request.setAttribute("GRID", grid);
		} else {
			request.setAttribute("GRID", GridManager.getEnrollmentGrid());
		}
		return mv;
	}

	@RequestMapping(value = {"/login/password"}, method = RequestMethod.POST)
	protected ModelAndView passwordLogin(
			@RequestParam(value = "password", required = true) String password,
			HttpServletRequest request,
			HttpServletResponse response) throws InvalidSessionException, SamlResponseException, InvalidUserException, InvalidLoginException, SamlBuildException, SamlSignException, IOException {

		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		//Login logic to handle authentication for Custom Identity Provider
		if (cookieUser.getPasswordManager().isValidAuthenticationAttempt(password)) {
			SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(
						new User(cookieUser.getUsername(),"",ControllerHelper.getUserAuthorities()), null, ControllerHelper.getUserAuthorities()));
			return new ModelAndView("forward:/sso/saml2");
		}
		// Invalid login attempt
		else {
			request.getSession().setAttribute("PW_ERROR", "Invalid username or password. Try again.");
			return new ModelAndView("redirect:/login");
		}
	}

	@RequestMapping(value = {"/login/key"}, method = RequestMethod.POST)
	protected ModelAndView keyLogin(
			@RequestParam(value = "PASSWORD", required = true) String password,
			HttpServletRequest request,
			HttpServletResponse response) throws InvalidSessionException, SamlResponseException, InvalidUserException, InvalidLoginException, SamlBuildException, SamlSignException, IOException {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		cookieUser.getKeyManager().setPrompt(new KeyPrompt(request.getSession().getAttribute("HEX").toString()));
		if (cookieUser.getKeyManager().isValidAuthenticationAttempt(password)) {
			SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(
						new User(cookieUser.getUsername(),"",ControllerHelper.getUserAuthorities()), null, ControllerHelper.getUserAuthorities()));
			return new ModelAndView("forward:/sso/saml2");
		}
		else {
			request.getSession().setAttribute("KEY_ERROR", "Invalid combination. Try again.");
			return new ModelAndView("redirect:/login#key-tab");
		}
	}

	@RequestMapping(value = {"/login/grid"}, method = RequestMethod.POST)
	protected ModelAndView gridLogin(
			@RequestParam(value = "pattern", required = true) String[] pattern,
			HttpServletRequest request,
			HttpServletResponse response) throws InvalidSessionException, SamlResponseException, InvalidUserException, InvalidLoginException, SamlBuildException, SamlSignException, IOException {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		cookieUser.getGridManager().setGrid(request.getSession().getAttribute("GRID_KEY").toString().split(","));
		if (cookieUser.getGridManager().isValidAuthenticationAttempt(String.join(",", pattern))) {
			SecurityContextHolder.getContext()
				.setAuthentication(new UsernamePasswordAuthenticationToken(
						new User(cookieUser.getUsername(),"",ControllerHelper.getUserAuthorities()), null, ControllerHelper.getUserAuthorities()));
			return new ModelAndView("forward:/sso/saml2");
		}
		else {
			request.getSession().setAttribute("GRID_ERROR", "Incorrect pattern. Try again.");
			return new ModelAndView("redirect:/login#grid-tab");
		}
	}

	@RequestMapping(value = {"/authenticated"}, method = RequestMethod.GET)
	protected ModelAndView authenticated(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		request.setAttribute("USERNAME", ((User) auth.getPrincipal()).getUsername());
		return new ModelAndView("authenticated");
	}

	@ExceptionHandler(InvalidLoginException.class)
	public ModelAndView invalidLoginExceptionHandler(InvalidLoginException e, HttpServletRequest request, HttpServletResponse response) throws InvalidSessionException {
		e.printStackTrace();
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		ModelAndView mv = new ModelAndView("prompt-for-login");
		mv.addObject("ERROR","Invalid username or password");
		mv.addObject("USERNAME",cookieUser.getUsername());
		return mv;
	}
	
}
