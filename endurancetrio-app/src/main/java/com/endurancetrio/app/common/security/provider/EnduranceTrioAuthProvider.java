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

package com.endurancetrio.app.common.security.provider;

import com.endurancetrio.app.common.security.token.EnduranceTrioAuthToken;
import com.endurancetrio.business.tracker.service.TrackerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

/**
 * Custom AuthenticationProvider to handle Key based authentication.
 */
@Component
public class EnduranceTrioAuthProvider implements AuthenticationProvider {

  private static final String INVALID_KEY_MESSAGE = "Invalid Key or Account Disabled";
  private static final String KEY_AUTH_ROLE = "ROLE_TRACKER";

  private final TrackerAccountService trackerAccountService;

  @Autowired
  public EnduranceTrioAuthProvider(TrackerAccountService trackerAccountService) {
    this.trackerAccountService = trackerAccountService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    EnduranceTrioAuthToken authRequest = (EnduranceTrioAuthToken) authentication;

    String owner = (String) authRequest.getPrincipal();
    String key = (String) authRequest.getCredentials();

    boolean isValid = trackerAccountService.validateKey(owner, key);

    if (!isValid) {
      throw new BadCredentialsException(INVALID_KEY_MESSAGE);
    }

    return new EnduranceTrioAuthToken(owner,
        AuthorityUtils.createAuthorityList(KEY_AUTH_ROLE)
    );
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return EnduranceTrioAuthToken.class.isAssignableFrom(authentication);
  }
}
