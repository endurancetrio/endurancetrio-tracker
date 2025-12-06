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

package com.endurancetrio.business.tracker.mapper;

import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link DeviceTelemetryMapper} is a utility class for converting between {@link DeviceTelemetryDTO} and
 * {@link DeviceTelemetry} objects.
 */
@Component
public class DeviceTelemetryMapper {

  private final TrackerAccountMapper accountMapper;

  @Autowired
  public DeviceTelemetryMapper(TrackerAccountMapper accountMapper) {
    this.accountMapper = accountMapper;
  }

  /**
   * Converts a {@link DeviceTelemetryDTO} to a {@link DeviceTelemetry} entity.
   *
   * @param dto the {@link DeviceTelemetryDTO} to be mapped
   * @return the corresponding {@link DeviceTelemetry} entity
   */
  public DeviceTelemetry map(DeviceTelemetryDTO dto, TrackerAccountDTO account) {

    if (dto == null) {
      return null;
    }

    DeviceTelemetry entity = new DeviceTelemetry();
    entity.setAccount(accountMapper.map(account));
    entity.setDevice(dto.device());
    entity.setTime(dto.time());
    entity.setLatitude(dto.latitude());
    entity.setLongitude(dto.longitude());
    entity.setActive(dto.active());

    return entity;
  }

  /**
   * Converts a {@link DeviceTelemetry} entity to a {@link DeviceTelemetryDTO}.
   *
   * @param entity the {@link DeviceTelemetry} entity to be mapped
   * @return the corresponding {@link DeviceTelemetryDTO}
   */
  public DeviceTelemetryDTO map(DeviceTelemetry entity) {

    if (entity == null) {
      return null;
    }

    return new DeviceTelemetryDTO(entity.getDevice(), entity.getTime(), entity.getLatitude(),
        entity.getLongitude(), entity.isActive()
    );
  }
}
