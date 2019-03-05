package com.mapr.ps.cloud.terraform.maprdeployui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WicketWebSecurityApapterConfig extends WebSecurityConfigurerAdapter {
	@Value("${maprdeployui.login_username}")
	private String loginUsername;
	@Value("${maprdeployui.login_password}")
	private String loginPassword;


	@Override
	@Bean(name = "authenticationManager")
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests().antMatchers("/**").permitAll()
			.and()
			.logout()
			.permitAll();
		http.headers().frameOptions().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser(loginUsername).password("{noop}" + loginPassword).authorities("USER", "ADMIN");
	}

}