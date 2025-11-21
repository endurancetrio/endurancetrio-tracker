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

package com.endurancetrio.app.common.constants;

import org.springframework.http.HttpStatus;

/**
 * The {@link ControllerConstants} class contains constant values used across controller classes
 * in the application.
 */
public class ControllerConstants {

  public static final String API_VERSION_1 = "/v1";
  public static final String API_DOMAIN_TRACKER = "/tracker";

  public static final String API_RESOURCE_DEVICES = "/devices";

  public static final int STATUS_200 = HttpStatus.OK.value();
  public static final int STATUS_500 = HttpStatus.INTERNAL_SERVER_ERROR.value();

  public static final String MSG_200 = HttpStatus.OK.getReasonPhrase();
  public static final String MSG_500 = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

  public static final String DETAILS_AUTH_DENIED = "Access Denied: Missing required permissions";
  public static final String DETAILS_AUTH_FAILURE = "Authentication failed";
  public static final String DETAILS_SERVER_ERROR = "An internal server error occurred";
  public static final String DETAILS_SUCCESS = "Request handled successfully";

  private ControllerConstants() {
    throw new IllegalStateException("Utility Class");
  }
}
