package com.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserDetailsService userDetailsService;
	/*
	 * To override default authentication
	 */
	@Override
	protected void configure( AuthenticationManagerBuilder builder) throws Exception{
		/*
		 * DB Authentication
		 */
		builder.userDetailsService(userDetailsService);
		
		/*
		 * Code for in memory authentication
		 */
		//We need to have encoder here with password otherwise Exception is thrown
		/*PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		builder.inMemoryAuthentication()
		.withUser("anand").password(encoder.encode("anand123")).roles("USER")
		.and()
		.withUser("john").password(encoder.encode("john123")).roles("ADMIN");*/
	}
	
	/*
	 * To override default authorization - here used with in memory authentication
	 */
	@Override
	protected
	void configure( HttpSecurity httpSecurity) throws Exception{
		httpSecurity.cors().and().csrf().disable();
		// had to add above line otherwise /authenticate POST request was giving 403 forbidden
		//https://stackoverflow.com/questions/50486314/how-to-solve-403-error-in-spring-boot-post-request
		//disable cors may or may not be needed
		httpSecurity.authorizeRequests()
		.antMatchers("/admin").hasRole ("ADMIN")
		.antMatchers("/user").hasAnyRole("USER","ADMIN")
		.antMatchers("/all","/authenticate").permitAll()
	//	.antMatchers("/","static/css", "static/js").permitAll()
		.and().formLogin();
	}
	
	/*
	 * instead of PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
	 * we can also below code and provide as encoder
	 */
	/*@Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }*/
	
	@Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
		return super.authenticationManager();
    }
}
