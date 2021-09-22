package net.guides.springboot2.springboot2webappjsp.configuration;

import org.springframework.web.filter.CorsFilter;
//import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {


    private CorsConfiguration build() {
        CorsConfiguration corsCon = new CorsConfiguration();
        corsCon.addAllowedOrigin("*");
        corsCon.addAllowedHeader("*");
        corsCon.addAllowedMethod("*");
        return corsCon;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", build());

        return new CorsFilter(source);
    }


}
