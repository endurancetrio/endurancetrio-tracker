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

package com.endurancetrio.app.tracker.api;

import static com.endurancetrio.app.common.constants.ControllerConstants.API_PATH;
import static com.endurancetrio.app.common.constants.ControllerConstants.DETAILS_SUCCESS;
import static com.endurancetrio.app.tracker.constants.TrackerPathsAPI.TRACKER_DOMAIN;
import static com.endurancetrio.app.tracker.constants.TrackerPathsAPI.TRACKER_RESOURCE_ROUTES;
import static com.endurancetrio.app.tracker.constants.TrackerPathsAPI.TRACKER_V1;

import com.endurancetrio.app.common.annotation.EnduranceTrioRestController;
import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.service.RouteService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@EnduranceTrioRestController
@RequestMapping(API_PATH + TRACKER_DOMAIN + TRACKER_V1)
public class RouteRestController implements RouteApi {

  private final RouteService routeService;

  public RouteRestController(RouteService routeService) {
    this.routeService = routeService;
  }

  @Override
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(
      value = TRACKER_RESOURCE_ROUTES,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<@NonNull EnduranceTrioResponse<RouteDTO>> save(
      @Valid @RequestBody RouteDTO routeDTO
  ) {

    RouteDTO data = routeService.save(routeDTO);

    HttpStatus status;
    if (routeDTO.id() != null) {
      status = HttpStatus.OK;
    } else {
      status = HttpStatus.CREATED;
    }

    EnduranceTrioResponse<RouteDTO> response = new EnduranceTrioResponse<>(status.value(),
        status.getReasonPhrase(), DETAILS_SUCCESS, data
    );

    return ResponseEntity.status(status).body(response);
  }
}
