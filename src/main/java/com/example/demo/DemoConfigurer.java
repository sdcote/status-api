package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class DemoConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/appstatus").permitAll()
                .antMatchers(HttpMethod.POST, "/appstatus").hasRole("ADMIN")
                .antMatchers("/").permitAll()
        ;
        super.configure(http);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication()
                .withUser("devop").password("{noop}secret").authorities("ROLE_DEVOP")
                .and()
                .withUser("sysop").password("{noop}secret").authorities("ROLE_DEVOP", "ROLE_SYSOP")
                .and()
                .withUser("admin").password("{noop}secret").authorities("ROLE_DEVOP", "ROLE_SYSOP", "ROLE_ADMIN");
    }

}