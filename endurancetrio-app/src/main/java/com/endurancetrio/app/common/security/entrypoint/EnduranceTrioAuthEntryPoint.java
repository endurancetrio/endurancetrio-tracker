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

package com.endurancetrio.app.common.security.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Custom AuthenticationEntryPoint that delegates exception handling to a HandlerExceptionResolver.
 * This allows centralized exception handling via @ControllerAdvice even for authentication errors.
 */
@Component
public class EnduranceTrioAuthEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver resolver;

  public EnduranceTrioAuthEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
  ) {
    this.resolver = resolver;
  }

  @Override
  public void commence(
      @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException
  ) {

    resolver.resolveException(request, response, null, authException);
  }
}
