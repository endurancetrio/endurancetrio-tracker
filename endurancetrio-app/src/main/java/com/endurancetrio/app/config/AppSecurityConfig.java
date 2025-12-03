/*
 * Copyright (c) 2025-2025 Ricardo do Canto
 *
 * This file is part of the EnduranceTrio Tracker project.
 *
 * Licensed under the Functional Software License (FSL), Version 1.1, ALv2 Future License
 * (the "License");
 *
 * You may not use this file except in compliance with the License. You may obtain a copy
 * of the License at https://fsl.software/
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND WITHOUT WARRANTIES OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING WITHOUT LIMITATION WARRANTIES OF FITNESS FOR A PARTICULAR
 * PURPOSE, MERCHANTABILITY, TITLE OR NON-INFRINGEMENT.
 *
 * IN NO EVENT WILL WE HAVE ANY LIABILITY TO YOU ARISING OUT OF OR RELATED TO THE
 * SOFTWARE, INCLUDING INDIRECT, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * EVEN IF WE HAVE BEEN INFORMED OF THEIR POSSIBILITY IN ADVANCE.
 */

package com.endurancetrio.app.config;

import com.endurancetrio.app.common.security.entrypoint.EnduranceTrioAuthEntryPoint;
import com.endurancetrio.app.common.security.filter.EnduranceTrioAuthFilter;
import com.endurancetrio.app.common.security.provider.EnduranceTrioAuthProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration for the EnduranceTrio application.
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

  private static final Logger LOG = LoggerFactory.getLogger(AppSecurityConfig.class);

  private final String allowedOrigins;
  private final EnduranceTrioAuthProvider authProvider;
  private final EnduranceTrioAuthEntryPoint entryPoint;

  @Autowired
  public AppSecurityConfig(
      @Value("${cors.allowed-origins}") String allowedOrigins,
      EnduranceTrioAuthProvider authProvider, EnduranceTrioAuthEntryPoint entryPoint
  ) {
    this.authProvider = authProvider;
    this.entryPoint = entryPoint;
    this.allowedOrigins = allowedOrigins;
  }

  /**
   * Creates and configures a CORS (Cross-Origin Resource Sharing) configuration source bean. This
   * bean defines CORS policies for the application, allowing or restricting cross-origin requests.
   *
   * @return {@link CorsConfigurationSource} a fully configured CORS configuration source registered
   * for all application endpoints
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(getAndValidateAllowedOrigins());
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  /**
   * Parses, validates, and normalizes the configured CORS allowed origins.
   * <p>
   * This method processes the comma-separated list of origins from the {@code cors.allowed-origins}
   * configuration property. It performs the following steps:
   * <ol>
   *   <li>Returns an empty list if no origins are configured, effectively disabling CORS</li>
   *   <li>Splits the comma-separated string into individual origin entries</li>
   *   <li>Trims whitespace from each origin entry</li>
   *   <li>Filters out any blank or empty entries</li>
   * </ol>
   *
   * @return A list of validated and normalized origin strings, or an empty list if no origins are
   * configured. Never returns {@code null}.
   */
  private List<String> getAndValidateAllowedOrigins() {

    LOG.info("CORS allowed-origins configuration is set to {}", allowedOrigins);

    if (allowedOrigins == null || allowedOrigins.isEmpty()) {
      LOG.warn("CORS allowed-origins is not set!");
      return List.of();
    }

    List<String> origins = Arrays.asList(this.allowedOrigins.split(","));

    List<String> valiAllowedOrigins = origins.stream()
        .map(String::trim)
        .filter(origin -> !origin.isBlank())
        .toList();

    LOG.info("CORS valid allowed-origins: {}", valiAllowedOrigins);

    return valiAllowedOrigins;
  }

  /**
   * Security filter chain for API endpoints.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   */
  @Bean
  @Order(1)
  public SecurityFilterChain securityFilterChainAPI(HttpSecurity http) {

    http.cors(Customizer.withDefaults())
        .securityMatcher("/api/**", "/tracker/**")
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorization -> authorization.requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(entryPoint))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);

    return http.build();
  }

  /**
   * Security filter chain for UI (Thymeleaf) pages.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   */
  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChainWeb(HttpSecurity http) {
    http.authorizeHttpRequests(
            authorization -> authorization.requestMatchers("/h2-tracker", "/h2-tracker/**")
                .permitAll()
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/img/**"
                ).permitAll()
                .requestMatchers(
                    "/",
                    "/favicon.ico",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/error"
                ).permitAll()
                .anyRequest()
                .authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-tracker/**"))
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Collections.singletonList(authProvider));
  }

  @Bean
  public EnduranceTrioAuthFilter authenticationFilter() {
    return new EnduranceTrioAuthFilter(authenticationManager(), this.entryPoint);
  }
}
