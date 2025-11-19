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

import com.endurancetrio.app.common.annotation.EnduranceTrioRestController;
import com.endurancetrio.app.common.constants.ControllerConstants;
import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import com.endurancetrio.business.common.dto.ErrorDTO;
import com.endurancetrio.business.common.exception.base.EnduranceTrioException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The {@link EnduranceTrioExceptionHandlerAPI} class is a global exception handler for the
 * EnduranceTrio Tracker application. It captures unhandled exceptions and custom
 * EnduranceTrioExceptions, returning standardized error responses.
 */
@RestControllerAdvice(annotations = EnduranceTrioRestController.class)
public class EnduranceTrioExceptionHandlerAPI extends ResponseEntityExceptionHandler {

  private static final Logger LOG = Logger.getLogger(
      EnduranceTrioExceptionHandlerAPI.class.getName());

  private static final String MSG_STATUS_ERROR = ControllerConstants.MSG_STATUS_ERROR;
  private static final String MSG_CODE_ERROR = ControllerConstants.MSG_CODE_SERVER_ERROR;

  @ExceptionHandler(EnduranceTrioException.class)
  public ResponseEntity<EnduranceTrioResponse<List<ErrorDTO>>> handledException(
      EnduranceTrioException exception
  ) {

    HttpStatus status = HttpStatus.resolve(exception.getCode());
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    LOG.log(Level.WARNING, "Handled Exception ({0}): {1}",
        new Object[]{status, exception.getMessage()}
    );

    EnduranceTrioResponse<List<ErrorDTO>> response = new EnduranceTrioResponse<>(MSG_STATUS_ERROR,
        String.valueOf(status.value()), exception.getMessage(), exception.getErrors()
    );

    return new ResponseEntity<>(response, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<EnduranceTrioResponse<String>> unhandledException(Exception exception) {

    LOG.log(Level.SEVERE, MSG_CODE_ERROR, exception);

    EnduranceTrioResponse<String> response = new EnduranceTrioResponse<>(MSG_STATUS_ERROR,
        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MSG_CODE_ERROR
    );

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
