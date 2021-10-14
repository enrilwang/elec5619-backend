package net.guides.springboot2.springboot2webappjsp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
//@ConditionalOnProperty(value="app.security.basic.enabled",havingValue = "false")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Bean
    public HttpFirewall allowAll() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


//        super.configure(web);
//        web.httpFirewall(allowAll());

//        follow is the clear all
      http.authorizeRequests().anyRequest().permitAll().and().logout().permitAll();


//        http.authorizeRequests().anyRequest().authenticated().and().oauth2Login();



        http.csrf().disable();
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


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//
//
//        super.configure(web);
//        web.httpFirewall(allowAll());
//
//
//    }

}
