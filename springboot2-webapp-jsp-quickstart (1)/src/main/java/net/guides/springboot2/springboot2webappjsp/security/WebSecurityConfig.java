package net.guides.springboot2.springboot2webappjsp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;

@Configuration
//@ConditionalOnProperty(value="app.security.basic.enabled",havingValue = "false")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        follow is the clear all
      //http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();


        http.authorizeRequests().anyRequest().authenticated().and().oauth2Login();



//        http.csrf().disable();
//
//
//                http.authorizeRequests()
//                .antMatchers("/oauth2/**").permitAll();
////               http.authorizeRequests().and().formLogin()
////
////                    .loginPage("/login")
//        //.and()
//                http.oauth2Login().loginPage("/login/oauth2")
//                    .userInfoEndpoint()
//                    .userService(oauth2UserService);
////                    .and()
////                .and()
////                .logout().permitAll();
//        http.httpBasic().disable();
    }




}
