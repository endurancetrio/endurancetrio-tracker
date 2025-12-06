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
import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import com.endurancetrio.business.tracker.mapper.DeviceTelemetryMapper;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
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
class DeviceTelemetryServiceMainTest {

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

  private DeviceTelemetryDTO inputDTO;
  private DeviceTelemetryDTO firstExpectedDTO;
  private DeviceTelemetryDTO secondExpectedDTO;
  private TrackerAccount mockTrackerAccount;
  private DeviceTelemetry mockDeviceTelemetry;
  private DeviceTelemetry firstPersistedDeviceTelemetry;
  private DeviceTelemetry secondPersistedDeviceTelemetry;

  @Mock
  private TrackerAccountRepository trackerAccountRepository;

  @Mock
  private DeviceTelemetryRepository deviceTelemetryRepository;

  @Mock
  private DeviceTelemetryMapper deviceTelemetryMapper;

  @InjectMocks
  private DeviceTelemetryServiceMain underTest;

  @BeforeEach
  void setUp() {
    inputDTO = new DeviceTelemetryDTO(DEVICE_1, TIME_1, LATITUDE_1, LONGITUDE_1, IS_ACTIVE);

    mockTrackerAccount = new TrackerAccount(OWNER, KEY, IS_ENABLED);
    mockDeviceTelemetry = new DeviceTelemetry();
    mockDeviceTelemetry.setDevice(DEVICE_1);
    mockDeviceTelemetry.setLatitude(LATITUDE_1);
    mockDeviceTelemetry.setLongitude(LONGITUDE_1);
    mockDeviceTelemetry.setActive(IS_ACTIVE);
    firstPersistedDeviceTelemetry = new DeviceTelemetry(mockTrackerAccount, DEVICE_1, TIME_1, LATITUDE_1,
        LONGITUDE_1, IS_ACTIVE
    );
    firstPersistedDeviceTelemetry.setId(ID_1);
    secondPersistedDeviceTelemetry = new DeviceTelemetry(mockTrackerAccount, DEVICE_2, TIME_2, LATITUDE_2,
        LONGITUDE_2, IS_ACTIVE
    );
    firstPersistedDeviceTelemetry.setId(ID_2);

    firstExpectedDTO = new DeviceTelemetryDTO(DEVICE_1, TIME_1, LATITUDE_1, LONGITUDE_1, IS_ACTIVE);
    secondExpectedDTO = new DeviceTelemetryDTO(DEVICE_2, TIME_2, LATITUDE_2, LONGITUDE_2, IS_ACTIVE);
  }

  @Test
  void save() {

    when(trackerAccountRepository.getReferenceById(OWNER)).thenReturn(mockTrackerAccount);
    when(deviceTelemetryMapper.map(inputDTO, null)).thenReturn(mockDeviceTelemetry);
    when(deviceTelemetryRepository.save(mockDeviceTelemetry)).thenReturn(firstPersistedDeviceTelemetry);
    when(deviceTelemetryMapper.map(firstPersistedDeviceTelemetry)).thenReturn(firstExpectedDTO);

    DeviceTelemetryDTO result = underTest.save(OWNER, inputDTO);

    verify(trackerAccountRepository, times(1)).getReferenceById(OWNER);
    verify(deviceTelemetryMapper, times(1)).map(inputDTO, null);
    verify(deviceTelemetryRepository, times(1)).save(mockDeviceTelemetry);
    verify(deviceTelemetryMapper, times(1)).map(firstPersistedDeviceTelemetry);

    assertNotNull(result);
    assertEquals(firstExpectedDTO, result);
    assertEquals(mockTrackerAccount, mockDeviceTelemetry.getAccount());
  }

  @Test
  void saveWithNonExistingAccount() {

    String owner = "doe";

    when(trackerAccountRepository.getReferenceById(owner)).thenReturn(mock(TrackerAccount.class));
    when(deviceTelemetryMapper.map(inputDTO, null)).thenReturn(mockDeviceTelemetry);
    when(deviceTelemetryRepository.save(mockDeviceTelemetry)).thenThrow(new EntityNotFoundException());

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.save(owner, inputDTO)
    );

    verify(trackerAccountRepository, times(1)).getReferenceById(owner);
    verify(deviceTelemetryMapper, times(1)).map(inputDTO, null);
    verify(deviceTelemetryRepository, times(1)).save(mockDeviceTelemetry);
    verify(deviceTelemetryMapper, never()).map(firstPersistedDeviceTelemetry);

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
    assertEquals(EnduranceTrioError.NOT_FOUND.getMessage(), result.getMessage());
  }

  @Test
  void findMostRecentRecordForEachDevice() {

    int expectedResultSize = 2;

    when(deviceTelemetryRepository.findMostRecentRecordForEachDevice()).thenReturn(
        List.of(firstPersistedDeviceTelemetry, secondPersistedDeviceTelemetry));
    when(deviceTelemetryMapper.map(firstPersistedDeviceTelemetry)).thenReturn(firstExpectedDTO);
    when(deviceTelemetryMapper.map(secondPersistedDeviceTelemetry)).thenReturn(secondExpectedDTO);

    List<DeviceTelemetryDTO> result = underTest.findMostRecentRecordForEachDevice();

    verify(deviceTelemetryRepository, times(1)).findMostRecentRecordForEachDevice();
    verify(deviceTelemetryMapper, times(2)).map(any());
    verify(deviceTelemetryMapper, times(1)).map(firstPersistedDeviceTelemetry);
    verify(deviceTelemetryMapper, times(1)).map(secondPersistedDeviceTelemetry);

    assertNotNull(result);
    assertEquals(expectedResultSize, result.size());
  }

  @Test
  void findMostRecentRecordForEachDeviceWithEmptyData() {

    int expectedResultSize = 0;

    when(deviceTelemetryRepository.findMostRecentRecordForEachDevice()).thenReturn(new ArrayList<>());

    List<DeviceTelemetryDTO> result = underTest.findMostRecentRecordForEachDevice();

    verify(deviceTelemetryRepository, times(1)).findMostRecentRecordForEachDevice();
    verify(deviceTelemetryMapper, never()).map(any());

    assertNotNull(result);
    assertEquals(expectedResultSize, result.size());
  }
}
