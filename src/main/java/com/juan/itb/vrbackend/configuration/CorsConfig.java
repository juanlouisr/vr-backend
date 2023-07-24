package com.juan.itb.vrbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Bean
  public CorsWebFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();

    config.addAllowedOrigin("*"); // Allow requests from any origin
    config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, etc.)
    config.addAllowedHeader("*"); // Allow all headers

    source.registerCorsConfiguration("/**", config);
    return new CorsWebFilter(source);
  }
}
