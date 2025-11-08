package com.inyro.api.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public static CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //데이터 교환이 가능한 URL 지정
        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:8080");
        allowedOriginPatterns.add("http://localhost:5500");
        allowedOriginPatterns.add("http://localhost:5174");
        allowedOriginPatterns.add("https://api.inyro.com");
        allowedOriginPatterns.add("http://127.0.0.1:8080");
        allowedOriginPatterns.add("http://127.0.0.1:5500");

        //허용하는 HTTP METHOD 지정
        ArrayList<String> allowedHttpMethods = new ArrayList<>();
        allowedHttpMethods.add("GET");
        allowedHttpMethods.add("POST");
        allowedHttpMethods.add("PUT");
        allowedHttpMethods.add("PATCH");
        allowedHttpMethods.add("DELETE");
        allowedHttpMethods.add("PATCH");

        configuration.setAllowedOrigins(allowedOriginPatterns);
        configuration.setAllowedMethods(allowedHttpMethods);

        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "PasswordToken"));
        configuration.setAllowCredentials(true); //credential TRUE

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
