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
import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
import com.endurancetrio.business.tracker.mapper.TrackingDataMapper;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.model.entity.TrackingData;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import com.endurancetrio.data.tracker.repository.TrackingDataRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackingDataServiceMain implements TrackingDataService {

  private final TrackerAccountRepository trackerAccountRepository;
  private final TrackingDataRepository trackingDataRepository;
  private final TrackingDataMapper trackingDataMapper;

  public TrackingDataServiceMain(
      TrackerAccountRepository trackerAccountRepository,
      TrackingDataRepository trackingDataRepository, TrackingDataMapper trackingDataMapper
  ) {
    this.trackerAccountRepository = trackerAccountRepository;
    this.trackingDataRepository = trackingDataRepository;
    this.trackingDataMapper = trackingDataMapper;
  }

  @Override
  @Transactional
  public TrackingDataDTO save(String owner, TrackingDataDTO trackingDataDTO) {

    TrackerAccount accountReference = trackerAccountRepository.getReferenceById(owner);

    TrackingData trackingData = trackingDataMapper.map(trackingDataDTO, null);
    trackingData.setAccount(accountReference);

    try {
      return trackingDataMapper.map(trackingDataRepository.save(trackingData));
    } catch (EntityNotFoundException exception) {
      throw new NotFoundException(EnduranceTrioError.NOT_FOUND);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<TrackingDataDTO> findMostRecentRecordForEachDevice() {

    List<TrackingData> latestLocations = trackingDataRepository.findMostRecentRecordForEachDevice();

    return latestLocations.stream().map(trackingDataMapper::map).toList();
  }
}
