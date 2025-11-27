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
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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

/**
 * Security configuration for the EnduranceTrio application.
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

  private final EnduranceTrioAuthProvider authProvider;
  private final EnduranceTrioAuthEntryPoint entryPoint;

  @Autowired
  public AppSecurityConfig(
      EnduranceTrioAuthProvider authProvider,
      EnduranceTrioAuthEntryPoint entryPoint
  ) {
    this.authProvider = authProvider;
    this.entryPoint = entryPoint;
  }

  /**
   * Security filter chain for API endpoints.
   *
   * @param http the HttpSecurity to configure
   * @return the configured SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  @Order(1)
  public SecurityFilterChain securityFilterChainAPI(HttpSecurity http) throws Exception {

    http.securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorization -> authorization.anyRequest().authenticated())
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
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChainWeb(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorization -> authorization.requestMatchers("/h2-tracker", "/h2-tracker/**")
                .permitAll()
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/img/**"
                ).permitAll()
                .requestMatchers(
                    "/openapi/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/swagger-ui.html"
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
