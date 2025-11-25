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
import static org.mockito.ArgumentMatchers.any;
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
import java.util.ArrayList;
import java.util.List;
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

  private static final Long ID_1 = 1L;
  private static final String DEVICE_1 = "SDABC";
  private static final Instant TIME_1 = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double LATITUDE_1 = 39.510058;
  private static final Double LONGITUDE_1 = -9.136079;
  private static final Long ID_2 = 2L;
  private static final String DEVICE_2 = "SDDEF";
  private static final Instant TIME_2 = Instant.parse("2026-09-19T06:00:06Z");
  private static final Double LATITUDE_2 = 39.509001;
  private static final Double LONGITUDE_2 = -9.139602;
  private static final Boolean IS_ACTIVE = true;

  private TrackingDataDTO inputDTO;
  private TrackingDataDTO firstExpectedDTO;
  private TrackingDataDTO secondExpectedDTO;
  private TrackerAccount mockTrackerAccount;
  private TrackingData mockTrackingData;
  private TrackingData firstPersistedTrackingData;
  private TrackingData secondPersistedTrackingData;

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
    inputDTO = new TrackingDataDTO(DEVICE_1, TIME_1, LATITUDE_1, LONGITUDE_1, IS_ACTIVE);

    mockTrackerAccount = new TrackerAccount(OWNER, KEY, IS_ENABLED);
    mockTrackingData = new TrackingData();
    mockTrackingData.setDevice(DEVICE_1);
    mockTrackingData.setLatitude(LATITUDE_1);
    mockTrackingData.setLongitude(LONGITUDE_1);
    mockTrackingData.setActive(IS_ACTIVE);
    firstPersistedTrackingData = new TrackingData(mockTrackerAccount, DEVICE_1, TIME_1, LATITUDE_1,
        LONGITUDE_1, IS_ACTIVE
    );
    firstPersistedTrackingData.setId(ID_1);
    secondPersistedTrackingData = new TrackingData(mockTrackerAccount, DEVICE_2, TIME_2, LATITUDE_2,
        LONGITUDE_2, IS_ACTIVE
    );
    firstPersistedTrackingData.setId(ID_2);

    firstExpectedDTO = new TrackingDataDTO(DEVICE_1, TIME_1, LATITUDE_1, LONGITUDE_1, IS_ACTIVE);
    secondExpectedDTO = new TrackingDataDTO(DEVICE_2, TIME_2, LATITUDE_2, LONGITUDE_2, IS_ACTIVE);
  }

  @Test
  void save() {

    when(trackerAccountRepository.getReferenceById(OWNER)).thenReturn(mockTrackerAccount);
    when(trackingDataMapper.map(inputDTO, null)).thenReturn(mockTrackingData);
    when(trackingDataRepository.save(mockTrackingData)).thenReturn(firstPersistedTrackingData);
    when(trackingDataMapper.map(firstPersistedTrackingData)).thenReturn(firstExpectedDTO);

    TrackingDataDTO result = underTest.save(OWNER, inputDTO);

    verify(trackerAccountRepository, times(1)).getReferenceById(OWNER);
    verify(trackingDataMapper, times(1)).map(inputDTO, null);
    verify(trackingDataRepository, times(1)).save(mockTrackingData);
    verify(trackingDataMapper, times(1)).map(firstPersistedTrackingData);

    assertNotNull(result);
    assertEquals(firstExpectedDTO, result);
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
    verify(trackingDataMapper, never()).map(firstPersistedTrackingData);

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
    assertEquals(EnduranceTrioError.NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  void findMostRecentRecordForEachDevice() {

    int expectedResultSize = 2;

    when(trackingDataRepository.findMostRecentRecordForEachDevice()).thenReturn(
        List.of(firstPersistedTrackingData, secondPersistedTrackingData));
    when(trackingDataMapper.map(firstPersistedTrackingData)).thenReturn(firstExpectedDTO);
    when(trackingDataMapper.map(secondPersistedTrackingData)).thenReturn(secondExpectedDTO);

    List<TrackingDataDTO> result = underTest.findMostRecentRecordForEachDevice();

    verify(trackingDataRepository, times(1)).findMostRecentRecordForEachDevice();
    verify(trackingDataMapper, times(2)).map(any());
    verify(trackingDataMapper, times(1)).map(firstPersistedTrackingData);
    verify(trackingDataMapper, times(1)).map(secondPersistedTrackingData);

    assertNotNull(result);
    assertEquals(expectedResultSize, result.size());
  }

  @Test
  void findMostRecentRecordForEachDeviceWithEmptyData() {

    int expectedResultSize = 0;

    when(trackingDataRepository.findMostRecentRecordForEachDevice()).thenReturn(new ArrayList<>());

    List<TrackingDataDTO> result = underTest.findMostRecentRecordForEachDevice();

    verify(trackingDataRepository, times(1)).findMostRecentRecordForEachDevice();
    verify(trackingDataMapper, never()).map(any());

    assertNotNull(result);
    assertEquals(expectedResultSize, result.size());
  }
}
