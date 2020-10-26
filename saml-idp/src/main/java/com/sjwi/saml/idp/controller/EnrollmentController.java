package com.sjwi.saml.idp.controller;

import static com.sjwi.saml.idp.model.security.GridManager.DEMO_USER_GRID_MAP_COOKIE;
import static com.sjwi.saml.idp.model.security.KeyManager.DEMO_USER_KEY_COOKIE;
import static com.sjwi.saml.idp.model.security.PasswordManager.DEMO_USER_CREDENTIALS_COOKIE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjwi.saml.idp.file.pdf.PdfUtility;
import com.sjwi.saml.idp.model.CookieUser;
import com.sjwi.saml.idp.model.security.GridManager;
import com.sjwi.saml.idp.model.security.KeyManager;
import com.sjwi.saml.idp.model.security.PasswordManager;

@Controller
public class EnrollmentController {

	@Autowired
	ServletContext context;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = {"/enroll/password"}, method = RequestMethod.POST)
	protected void enrollUserInPassword(
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "confirmPassword", required = true) String confirmPassword,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		if (password.equals(confirmPassword) && password.length() > 3) {
			PasswordManager pwManager = new PasswordManager(passwordEncoder.encode(password),cookieUser.getUsername(),false);
			response.addCookie(ControllerHelper.buildCustomCookie(DEMO_USER_CREDENTIALS_COOKIE, pwManager.getEncryptedUserAttribute()));
		} else {
			request.getSession().setAttribute("ERROR", "Invalid password. A password must be greater than 4 characters.");
		}
		response.sendRedirect(context.getContextPath() + "/login");
	}

	@RequestMapping(value = {"/enroll/key"}, method = RequestMethod.GET)
	protected void enrollUserInKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		KeyManager key = new KeyManager(KeyManager.generateNewKey(),cookieUser.getUsername(),false);
		PdfUtility pdfUtility = new PdfUtility(context.getRealPath("/"), cookieUser.getUsername());
		pdfUtility.buildEnrollmentPdf(key);
		response.addCookie(ControllerHelper.buildCustomCookie(DEMO_USER_KEY_COOKIE, key.getEncryptedUserAttribute()));
	}

	@RequestMapping(value = {"/enroll/grid"}, method = RequestMethod.POST)
	protected void enrollUserInGrid(@RequestParam(value = "pattern", required = true) String[] enrollmentGrid,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		GridManager gridManager = new GridManager(String.join(",", enrollmentGrid), cookieUser.getUsername(),false);
		response.addCookie(ControllerHelper.buildCustomCookie(DEMO_USER_GRID_MAP_COOKIE, gridManager.getEncryptedUserAttribute()));
		response.sendRedirect(context.getContextPath() + "/login#grid-tab");
	}
	
	@RequestMapping(value = {"/reset"}, method = RequestMethod.GET)
	protected void resetMethod(@RequestParam(value="type",required = true) String type,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Cookie cookie = null;
		if (type.equals("password")) {
			cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(DEMO_USER_CREDENTIALS_COOKIE)).findFirst().orElse(null);
		} else if (type.equals("key")) {
			cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(DEMO_USER_KEY_COOKIE)).findFirst().orElse(null);
		}
		else {
			cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equalsIgnoreCase(DEMO_USER_GRID_MAP_COOKIE)).findFirst().orElse(null);
		}
		cookie.setValue("");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		response.sendRedirect(context.getContextPath() + "/login");
	}

	@RequestMapping(value="/download-key")
	@ResponseBody
	protected void downloadKey(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CookieUser cookieUser = ControllerHelper.readCookieUserFromRequest(request, response);
		PdfUtility pdfUtility = new PdfUtility(context.getRealPath("/"), cookieUser.getUsername());
		pdfUtility.buildEnrollmentPdf(cookieUser.getKeyManager());
		response.setContentType("application/pdf; name=\"" + pdfUtility.getFileName() + "\"");
        response.addHeader("Content-Disposition", "inline; filename=\""+ pdfUtility.getFileName() + ".pdf\"");
		response.addCookie(ControllerHelper.buildCustomCookie(DEMO_USER_KEY_COOKIE, cookieUser.getKeyManager().getEncryptedUserAttribute()));
        Files.copy(Paths.get(pdfUtility.getPath()), response.getOutputStream());
	}
}
