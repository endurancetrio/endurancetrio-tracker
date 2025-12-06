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

import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import com.endurancetrio.business.tracker.mapper.DeviceTelemetryMapper;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceTelemetryServiceMain implements DeviceTelemetryService {

  private final TrackerAccountRepository trackerAccountRepository;
  private final DeviceTelemetryRepository deviceTelemetryRepository;
  private final DeviceTelemetryMapper deviceTelemetryMapper;

  public DeviceTelemetryServiceMain(
      TrackerAccountRepository trackerAccountRepository,
      DeviceTelemetryRepository deviceTelemetryRepository, DeviceTelemetryMapper deviceTelemetryMapper
  ) {
    this.trackerAccountRepository = trackerAccountRepository;
    this.deviceTelemetryRepository = deviceTelemetryRepository;
    this.deviceTelemetryMapper = deviceTelemetryMapper;
  }

  @Override
  @Transactional
  public DeviceTelemetryDTO save(String owner, DeviceTelemetryDTO deviceTelemetryDTO) {

    TrackerAccount accountReference = trackerAccountRepository.getReferenceById(owner);

    DeviceTelemetry deviceTelemetry = deviceTelemetryMapper.map(deviceTelemetryDTO, null);
    deviceTelemetry.setAccount(accountReference);

    try {
      return deviceTelemetryMapper.map(deviceTelemetryRepository.save(deviceTelemetry));
    } catch (EntityNotFoundException exception) {
      throw new NotFoundException(EnduranceTrioError.NOT_FOUND);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<DeviceTelemetryDTO> findMostRecentRecordForEachDevice() {

    List<DeviceTelemetry> latestLocations = deviceTelemetryRepository.findMostRecentRecordForEachDevice();

    return latestLocations.stream().map(deviceTelemetryMapper::map).toList();
  }
}
