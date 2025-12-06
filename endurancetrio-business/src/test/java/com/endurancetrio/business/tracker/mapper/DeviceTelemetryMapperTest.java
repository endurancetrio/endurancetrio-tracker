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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeviceTelemetryMapperTest {

  private static final String OWNER = "system";
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";
  private static final Boolean IS_ENABLED = true;

  private static final String DEVICE = "SDABC";
  private static final Instant TIME = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double LATITUDE = 39.510058;
  private static final Double LONGITUDE = -9.136079;
  private static final Boolean IS_ACTIVE = true;

  private DeviceTelemetry entityTest;
  private DeviceTelemetryDTO dtoTest;
  private TrackerAccount account;

  @Mock
  private TrackerAccountMapper accountMapper;

  @InjectMocks
  private DeviceTelemetryMapper underTest;

  @BeforeEach
  void setUp() {

    Long testId = 1L;

    account = new TrackerAccount(OWNER, KEY, IS_ENABLED);

    entityTest = new DeviceTelemetry();
    entityTest.setId(testId);
    entityTest.setAccount(account);
    entityTest.setDevice(DEVICE);
    entityTest.setTime(TIME);
    entityTest.setLatitude(LATITUDE);
    entityTest.setLongitude(LONGITUDE);
    entityTest.setActive(IS_ACTIVE);

    dtoTest = new DeviceTelemetryDTO(DEVICE, TIME, LATITUDE, LONGITUDE, IS_ACTIVE);
  }

  @Test
  void mapDTO() {

    TrackerAccountDTO accountDto = new TrackerAccountDTO(OWNER, KEY, true);

    when(accountMapper.map(accountDto)).thenReturn(this.account);

    DeviceTelemetry result = underTest.map(dtoTest, accountDto);

    assertNull(result.getId());
    assertEquals(OWNER, result.getAccount().getOwner());
    assertEquals(DEVICE, result.getDevice());
    assertEquals(TIME, result.getTime());
    assertEquals(LATITUDE, result.getLatitude());
    assertEquals(LONGITUDE, result.getLongitude());
    assertTrue(result.isActive());
  }

  @Test
  void mapEntity() {

    DeviceTelemetryDTO result = underTest.map(entityTest);

    assertEquals(DEVICE, result.device());
    assertEquals(TIME, result.time());
    assertEquals(LATITUDE, result.latitude());
    assertEquals(LONGITUDE, result.longitude());
    assertTrue(result.active());
  }

  @Test
  void mapNullDTO() {

    DeviceTelemetry result = underTest.map(null, null);
    assertNull(result);
  }

  @Test
  void mapNullEntity() {

    DeviceTelemetryDTO result = underTest.map(null);
    assertNull(result);
  }
}
