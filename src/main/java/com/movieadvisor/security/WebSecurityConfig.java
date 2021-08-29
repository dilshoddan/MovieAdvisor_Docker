package com.movieadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private static final String USERNAME_QUERY = "SELECT username, password, 1 AS enabled"
			+ "  FROM user WHERE username = ?";
	
	private static final String ROLES_BY_USERNAME_QUERY = "SELECT username, role"
			+ "  FROM user"
			+ "  WHERE username =?";
 
    @Autowired
    private DataSource dataSource;
	
    @Autowired
    protected void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	auth.jdbcAuthentication()
			.dataSource(dataSource)
	        .usersByUsernameQuery(USERNAME_QUERY)
	        .authoritiesByUsernameQuery(ROLES_BY_USERNAME_QUERY);
	        }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
		.authorizeRequests()
			.antMatchers("HttpMethod.GET", "/**").permitAll()
			.antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
			.anyRequest().authenticated()
			.and()
			.csrf().disable()
		.httpBasic();
	}
}