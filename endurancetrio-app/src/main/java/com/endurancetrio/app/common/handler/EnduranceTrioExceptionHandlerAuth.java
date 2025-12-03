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

package com.endurancetrio.app.common.handler;

import static com.endurancetrio.app.common.constants.ControllerConstants.DETAILS_AUTH_DENIED;
import static com.endurancetrio.app.common.constants.ControllerConstants.DETAILS_AUTH_FAILURE;

import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The {@link EnduranceTrioExceptionHandlerAuth} class handles authentication-related exceptions
 * globally across the EnduranceTrio Tracker application.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EnduranceTrioExceptionHandlerAuth {

  private static final Logger LOG = LoggerFactory.getLogger(
      EnduranceTrioExceptionHandlerAuth.class);

  /**
   * Handles 401 Unauthorized errors (Authentication failures). Triggered when AuthenticationFilter
   * throws AuthenticationException. This exception is caught by ExceptionTranslationFilter, which
   * uses the AuthenticationEntryPoint.
   */
  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<@NonNull EnduranceTrioResponse<String>> authException(
      AuthenticationException exception
  ) {

    HttpStatus status = HttpStatus.UNAUTHORIZED;

    LOG.warn("Authentication Failure ({}): {}", status.value(), exception.getMessage());

    EnduranceTrioResponse<String> response = new EnduranceTrioResponse<>(status.value(),
        status.getReasonPhrase(), DETAILS_AUTH_FAILURE
    );

    return new ResponseEntity<>(response, status);
  }

  /**
   * Handles 403 Forbidden errors (Authorization failures). Triggered when a user tries to access a
   * resource without the necessary permissions.
   */
  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<@NonNull EnduranceTrioResponse<String>> handleAuthorizationException(
      AccessDeniedException exception
  ) {

    HttpStatus status = HttpStatus.FORBIDDEN;

    LOG.warn("Authorization Failure ({}): {}", status.value(), exception.getMessage());

    EnduranceTrioResponse<String> response = new EnduranceTrioResponse<>(status.value(),
        status.getReasonPhrase(), DETAILS_AUTH_DENIED
    );

    return new ResponseEntity<>(response, status);
  }
}
