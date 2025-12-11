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

package com.endurancetrio.business.tracker.service;

import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.mapper.RouteMapper;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
import com.endurancetrio.data.tracker.repository.RouteRepository;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteServiceMain implements RouteService {

  private static final Logger LOG = LoggerFactory.getLogger(RouteServiceMain.class);

  private final DeviceTelemetryRepository deviceTelemetryRepository;
  private final RouteRepository routeRepository;
  private final RouteMapper routeMapper;

  @Autowired
  public RouteServiceMain(
      DeviceTelemetryRepository deviceTelemetryRepository, RouteRepository routeRepository,
      RouteMapper routeMapper
  ) {
    this.deviceTelemetryRepository = deviceTelemetryRepository;
    this.routeRepository = routeRepository;
    this.routeMapper = routeMapper;
  }

  @Override
  @Transactional
  public RouteDTO save(RouteDTO routeDTO) {

    validateRouteDevices(routeDTO);

    Route entity;
    if (routeDTO.id() != null) {

      entity = routeRepository.findById(routeDTO.id()).orElseThrow(() -> {
        String errorMessage = String.format("Route update failed: No route found with ID %d",
            routeDTO.id()
        );

        LOG.warn(errorMessage);
        return new NotFoundException(errorMessage, EnduranceTrioError.NOT_FOUND);
      });

      routeMapper.updateEntity(routeDTO, entity);
    } else {

      entity = routeMapper.map(routeDTO);
    }

    return routeMapper.map(routeRepository.save(entity));
  }

  /**
   * Validates that all devices referenced in the RouteDTO exist in the system.
   *
   * @param routeDTO the RouteDTO containing segments with device information
   * @throws NotFoundException if any device is not found in the system
   */
  private void validateRouteDevices(RouteDTO routeDTO) {
    Set<String> routeDevices = extractDevices(routeDTO);
    Set<String> existingDevices = deviceTelemetryRepository.findExistingDevicesFrom(routeDevices);

    if (existingDevices.size() != routeDevices.size()) {
      Set<String> missingDevices = routeDevices.stream()
          .filter(device -> !existingDevices.contains(device))
          .collect(Collectors.toSet());

      String errorMessage = String.format(
          "Cannot process route. The following devices are not registered: %s",
          String.join(", ", missingDevices)
      );

      LOG.warn(errorMessage);
      throw new BadRequestException(errorMessage, EnduranceTrioError.BAD_REQUEST);
    }
  }

  /**
   * Extracts unique device identifiers from the segments of the provided RouteDTO.
   *
   * @param routeDTO the RouteDTO containing segments with device information
   * @return a set of unique device identifiers
   */
  private Set<String> extractDevices(RouteDTO routeDTO) {

    return Optional.ofNullable(routeDTO)
        .map(RouteDTO::segments)
        .orElseGet(Collections::emptyList)
        .stream()
        .filter(Objects::nonNull)
        .flatMap(segment -> Stream.of(segment.startDevice(), segment.endDevice()))
        .collect(Collectors.toSet());
  }
}
