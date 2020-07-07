package de.karrieretutor.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/*").permitAll() //public pages
            .antMatchers("/admin/**")       //admin pages
            .authenticated().and().formLogin().permitAll()
            // logout redirects to start page
            .and().logout().logoutSuccessUrl("/index.html");
    }
}
