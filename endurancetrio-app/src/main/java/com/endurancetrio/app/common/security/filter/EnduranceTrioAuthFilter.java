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

package com.endurancetrio.app.common.security.filter;

import com.endurancetrio.app.common.security.token.EnduranceTrioAuthToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that intercepts HTTP requests to perform key based authentication. It extracts the key
 * and owner from the request headers, creates an authentication token, and delegates the
 * authentication process to the provided AuthenticationManager.
 * <p>
 * Expected Headers: 1. Authorization: Bearer {KEY} 2. ET-Owner: {OWNER_ID}
 */
public class EnduranceTrioAuthFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER_NAME = "Authorization";
  private static final String OWNER_HEADER_NAME = "ET-Owner";
  private static final String BEARER_PREFIX = "Bearer ";

  private final AuthenticationManager authManager;
  private final AuthenticationEntryPoint entryPoint;

  @Autowired
  public EnduranceTrioAuthFilter(
      AuthenticationManager authManager,
      AuthenticationEntryPoint entryPoint
  ) {
    this.authManager = authManager;
    this.entryPoint = entryPoint;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    String authHeader = request.getHeader(AUTH_HEADER_NAME);
    String ownerHeader = request.getHeader(OWNER_HEADER_NAME);

    if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX) || ownerHeader == null
        || ownerHeader.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String key = authHeader.substring(BEARER_PREFIX.length()).trim();
      String owner = ownerHeader.trim();

      EnduranceTrioAuthToken authRequest = new EnduranceTrioAuthToken(owner, key);

      Authentication authResult = authManager.authenticate(authRequest);

      SecurityContextHolder.getContext().setAuthentication(authResult);

      filterChain.doFilter(request, response);

    } catch (AuthenticationException exception) {
      SecurityContextHolder.clearContext();
      entryPoint.commence(request, response, exception);
    }
  }
}
