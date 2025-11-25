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
import static com.endurancetrio.app.tracker.constants.TrackerPathsAPI.TRACKER_RESOURCE_DEVICES;
import static com.endurancetrio.app.tracker.constants.TrackerPathsAPI.TRACKER_V1;

import com.endurancetrio.app.common.annotation.EnduranceTrioRestController;
import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
import com.endurancetrio.business.tracker.service.TrackingDataService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@EnduranceTrioRestController
@RequestMapping(API_PATH + TRACKER_DOMAIN + TRACKER_V1)
public class TrackerRestController implements TrackerAPI {

  private static final Logger LOG = LoggerFactory.getLogger(TrackerRestController.class);

  private final TrackingDataService trackingDataService;

  @Autowired
  public TrackerRestController(TrackingDataService trackingDataService) {
    this.trackingDataService = trackingDataService;
  }

  @Override
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(
      value = TRACKER_RESOURCE_DEVICES, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<EnduranceTrioResponse<TrackingDataDTO>> save(
      @Valid @RequestBody TrackingDataDTO trackingDataDTO
  ) {

    if (trackingDataDTO == null) {
      LOG.warn("The request made to save tracking data is invalid (null)");
      throw new BadRequestException(EnduranceTrioError.BAD_REQUEST);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new NotFoundException(EnduranceTrioError.NOT_FOUND);
    }
    String owner = authentication.getName();

    if (owner == null || owner.isBlank()) {
      LOG.error("There is no authenticated owner for saving tracking data");
      throw new NotFoundException(EnduranceTrioError.NOT_FOUND);
    }

    HttpStatus status = HttpStatus.CREATED;
    TrackingDataDTO data = trackingDataService.save(owner, trackingDataDTO);

    EnduranceTrioResponse<TrackingDataDTO> response = new EnduranceTrioResponse<>(status.value(),
        status.getReasonPhrase(), DETAILS_SUCCESS, data
    );

    return ResponseEntity.status(status).body(response);
  }

  @Override
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(
      value = TRACKER_RESOURCE_DEVICES, produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<EnduranceTrioResponse<List<TrackingDataDTO>>> getMostRecentRecordForEachDevice() {

    HttpStatus status = HttpStatus.OK;
    List<TrackingDataDTO> data = trackingDataService.findMostRecentRecordForEachDevice();

    EnduranceTrioResponse<List<TrackingDataDTO>> response = new EnduranceTrioResponse<>(
        status.value(), status.getReasonPhrase(), DETAILS_SUCCESS, data);

    return ResponseEntity.status(status).body(response);
  }
}
