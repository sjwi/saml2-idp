package com.sjwi.saml.idp.model;

import static com.sjwi.saml.idp.model.security.GridManager.DEMO_USER_GRID_MAP_COOKIE;
import static com.sjwi.saml.idp.model.security.KeyManager.DEMO_USER_KEY_COOKIE;
import static com.sjwi.saml.idp.model.security.PasswordManager.DEMO_USER_CREDENTIALS_COOKIE;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.sjwi.saml.idp.model.security.AuthenticationManager;
import com.sjwi.saml.idp.model.security.GridManager;
import com.sjwi.saml.idp.model.security.KeyManager;
import com.sjwi.saml.idp.model.security.PasswordManager;

public class CookieUser {

	public static final String DEMO_USER_COOKIE = "DEMO_USER";
	
	private final List<AuthenticationManager> authenticationManagers;
	private final String username;

	public CookieUser(List<Cookie> cookies, String username) {
		List<AuthenticationManager> managers = new ArrayList<>();
		if (cookies != null) {
			cookies.stream().forEach(c -> {
				if (c.getName().equalsIgnoreCase(DEMO_USER_GRID_MAP_COOKIE)) {
					managers.add(new GridManager(c.getValue(),username,true));
				}
				else if (c.getName().equalsIgnoreCase(DEMO_USER_CREDENTIALS_COOKIE)) {
					managers.add(new PasswordManager(c.getValue(),username,true));
				}
				else if (c.getName().equalsIgnoreCase(DEMO_USER_KEY_COOKIE)) {
					managers.add(new KeyManager(c.getValue(),username,true));
				}
			});
		} 
		this.username = username;
		this.authenticationManagers = managers;
	}

	public PasswordManager getPasswordManager() {
		AuthenticationManager placeholderManager = authenticationManagers.stream().filter(m -> m instanceof PasswordManager).findAny().orElse(null);
		return placeholderManager == null? null: (PasswordManager) placeholderManager;
	}

	public GridManager getGridManager() {
		AuthenticationManager placeholderManager = authenticationManagers.stream().filter(m -> m instanceof GridManager).findAny().orElse(null);
		return placeholderManager == null? null: (GridManager) placeholderManager;
	}

	public KeyManager getKeyManager() {
		AuthenticationManager placeholderManager = authenticationManagers.stream().filter(m -> m instanceof KeyManager).findAny().orElse(null);
		return placeholderManager == null? null: (KeyManager) placeholderManager;
	}

	public String getUsername() {
		return username;
	}
}