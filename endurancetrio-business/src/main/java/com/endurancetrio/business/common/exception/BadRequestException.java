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

package com.endurancetrio.business.common.exception;

import com.endurancetrio.business.common.dto.ErrorDTO;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.common.exception.base.EnduranceTrioException;
import java.util.Collections;

/**
 * The {@link BadRequestException} class represents an exception that is thrown when a request is
 * made with invalid or incomplete data.
 * <p>
 * The code associated with this exception is 400 because we are following the HTTP status code
 * conventions for the project exceptions codes.
 * <p>
 * See <a href="https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml" /> for
 * more details.
 */
public class BadRequestException extends EnduranceTrioException {

  private static final int BAD_REQUEST = 400;

  public BadRequestException(String message) {
    super(BAD_REQUEST, message);
  }

  public BadRequestException(EnduranceTrioError error) {
    super(error.getCode(), error.getMessage(), Collections.singletonList(new ErrorDTO(error)));
  }
}
