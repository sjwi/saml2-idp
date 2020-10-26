package com.sjwi.saml.idp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity httpSecurity)
      throws Exception {
        httpSecurity.authorizeRequests()
        	.antMatchers("/authenticated").access("hasAuthority('USER')")
        	.antMatchers("/destroy-user").access("hasAuthority('USER')")
        	.antMatchers(HttpMethod.GET, "/**").permitAll()
        	.and().exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and().requestCache().requestCache(requestCache())
        	.and().logout()
        	.and().headers()
			.frameOptions().sameOrigin()
			.httpStrictTransportSecurity().disable()
			.and().csrf().disable(); 
    }
	
	@Bean
	public RequestCache requestCache() {
	   return new HttpSessionRequestCache();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
