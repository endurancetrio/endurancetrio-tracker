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
import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
import com.endurancetrio.data.tracker.model.entity.TrackingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link TrackingDataMapper} is a utility class for converting between {@link TrackingDataDTO} and
 * {@link TrackingData} objects.
 */
@Component
public class TrackingDataMapper {

  private final TrackerAccountMapper accountMapper;

  @Autowired
  public TrackingDataMapper(TrackerAccountMapper accountMapper) {
    this.accountMapper = accountMapper;
  }

  /**
   * Converts a {@link TrackingDataDTO} to a {@link TrackingData} entity.
   *
   * @param dto the {@link TrackingDataDTO} to be mapped
   * @return the corresponding {@link TrackingData} entity
   */
  public TrackingData map(TrackingDataDTO dto, TrackerAccountDTO account) {

    if (dto == null) {
      return null;
    }

    TrackingData entity = new TrackingData();
    entity.setAccount(accountMapper.map(account));
    entity.setDevice(dto.device());
    entity.setTime(dto.time());
    entity.setLatitude(dto.latitude());
    entity.setLongitude(dto.longitude());
    entity.setActive(dto.active());

    return entity;
  }

  /**
   * Converts a {@link TrackingData} entity to a {@link TrackingDataDTO}.
   *
   * @param entity the {@link TrackingData} entity to be mapped
   * @return the corresponding {@link TrackingDataDTO}
   */
  public TrackingDataDTO map(TrackingData entity) {

    if (entity == null) {
      return null;
    }

    return new TrackingDataDTO(entity.getAccount().getOwner(), entity.getDevice(), entity.getTime(),
        entity.getLatitude(), entity.getLongitude(), entity.isActive()
    );
  }
}
