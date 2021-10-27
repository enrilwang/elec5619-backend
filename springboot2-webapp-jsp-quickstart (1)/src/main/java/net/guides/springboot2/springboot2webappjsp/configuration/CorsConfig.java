package net.guides.springboot2.springboot2webappjsp.configuration;

import org.springframework.web.filter.CorsFilter;
//import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {





    private CorsConfiguration build() {
        CorsConfiguration corsCon = new CorsConfiguration();
        corsCon.setAllowCredentials(true);
        corsCon.addAllowedOrigin("http://localhost:8081");
        corsCon.addAllowedOrigin("http://127.0.0.1:8081");
        corsCon.addAllowedHeader("*");
        corsCon.addAllowedMethod("*");
//        corsCon.addAllowedMethod("GET");
//        corsCon.addAllowedMethod("POST");
//        corsCon.addAllowedMethod("OPTIONS");
        return corsCon;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", build());

        return new CorsFilter(source);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8081","http://127.0.0.1:8081").allowCredentials(true).allowedHeaders(CorsConfiguration.ALL).allowedMethods(CorsConfiguration.ALL);
    }

}
