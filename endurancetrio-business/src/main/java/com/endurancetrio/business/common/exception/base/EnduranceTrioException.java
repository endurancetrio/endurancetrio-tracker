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

package com.endurancetrio.business.common.exception.base;

import com.endurancetrio.business.common.dto.ErrorDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link EnduranceTrioException} class serves as the base exception for all custom exceptions
 * in the EnduranceTrio Tracker project. It extends the standard Java {@link Exception} class and
 * includes additional fields for an error code and a list of detailed error information.
 */
public abstract class EnduranceTrioException extends RuntimeException {

  private final int code;
  private final List<ErrorDTO> errors = new ArrayList<>();

  protected EnduranceTrioException(int code, String message) {
    super(message);
    this.code = code;
  }

  protected EnduranceTrioException(int code, String message, List<ErrorDTO> errors) {
    super(message);
    this.code = code;
    if (errors != null) {
      this.errors.addAll(errors);
    }
  }

  public int getCode() {
    return code;
  }

  public List<ErrorDTO> getErrors() {
    return errors;
  }
}
