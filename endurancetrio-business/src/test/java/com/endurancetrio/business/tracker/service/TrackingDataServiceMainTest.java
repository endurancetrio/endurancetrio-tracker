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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
import com.endurancetrio.business.tracker.mapper.TrackingDataMapper;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.model.entity.TrackingData;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import com.endurancetrio.data.tracker.repository.TrackingDataRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackingDataServiceMainTest {

  private static final String OWNER = "system";
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";
  private static final boolean IS_ENABLED = true;

  private static final Long ID = 1L;
  private static final String DEVICE = "SDABC";
  private static final Instant TIME = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double LATITUDE = 39.510058;
  private static final Double LONGITUDE = -9.136079;
  private static final Boolean IS_ACTIVE = true;

  private TrackingDataDTO inputDTO;
  private TrackerAccount mockTrackerAccount;
  private TrackingData mockTrackingData;
  private TrackingData persistedTrackingData;

  @Mock
  private TrackerAccountRepository trackerAccountRepository;

  @Mock
  private TrackingDataRepository trackingDataRepository;

  @Mock
  private TrackingDataMapper trackingDataMapper;

  @InjectMocks
  private TrackingDataServiceMain underTest;

  @BeforeEach
  void setUp() {
    inputDTO = new TrackingDataDTO(DEVICE, TIME, LATITUDE, LONGITUDE, IS_ACTIVE);

    mockTrackerAccount = new TrackerAccount(OWNER, KEY, IS_ENABLED);
    mockTrackingData = new TrackingData();
    mockTrackingData.setDevice(DEVICE);
    mockTrackingData.setLatitude(LATITUDE);
    mockTrackingData.setLongitude(LONGITUDE);
    mockTrackingData.setActive(IS_ACTIVE);
    persistedTrackingData = new TrackingData(mockTrackerAccount, DEVICE, TIME, LATITUDE, LONGITUDE,
        IS_ACTIVE
    );
    persistedTrackingData.setId(ID);


  }

  @Test
  void save() {

    TrackingDataDTO expectedDTO = new TrackingDataDTO(DEVICE, TIME, LATITUDE, LONGITUDE, IS_ACTIVE);

    when(trackerAccountRepository.getReferenceById(OWNER)).thenReturn(mockTrackerAccount);
    when(trackingDataMapper.map(inputDTO, null)).thenReturn(mockTrackingData);
    when(trackingDataRepository.save(mockTrackingData)).thenReturn(persistedTrackingData);
    when(trackingDataMapper.map(persistedTrackingData)).thenReturn(expectedDTO);

    TrackingDataDTO result = underTest.save(OWNER, inputDTO);

    verify(trackerAccountRepository, times(1)).getReferenceById(OWNER);
    verify(trackingDataMapper, times(1)).map(inputDTO, null);
    verify(trackingDataRepository, times(1)).save(mockTrackingData);
    verify(trackingDataMapper, times(1)).map(persistedTrackingData);

    assertNotNull(result);
    assertEquals(expectedDTO, result);
    assertEquals(mockTrackerAccount, mockTrackingData.getAccount());
  }

  @Test
  void saveWithNonExistingAccount() {

    String owner = "doe";

    when(trackerAccountRepository.getReferenceById(owner)).thenReturn(mock(TrackerAccount.class));
    when(trackingDataMapper.map(inputDTO, null)).thenReturn(mockTrackingData);
    when(trackingDataRepository.save(mockTrackingData)).thenThrow(new EntityNotFoundException());

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.save(owner, inputDTO)
    );

    verify(trackerAccountRepository, times(1)).getReferenceById(owner);
    verify(trackingDataMapper, times(1)).map(inputDTO, null);
    verify(trackingDataRepository, times(1)).save(mockTrackingData);
    verify(trackingDataMapper, never()).map(persistedTrackingData);

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
    assertEquals(EnduranceTrioError.NOT_FOUND.getMessage(), result.getMessage());
  }
}

