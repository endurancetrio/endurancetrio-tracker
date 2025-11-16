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

import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
import com.endurancetrio.data.tracker.model.entity.TrackingData;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackingDataMapperTest {

  private static final String DEVICE = "SDABC";
  private static final Instant TIME = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double LATITUDE = 39.510058;
  private static final Double LONGITUDE = -9.136079;
  private static final String OWNER = "system";

  private TrackingData entityTest;
  private TrackingDataDTO dtoTest;

  private TrackingDataMapper underTest;

  @BeforeEach
  void setUp() {

    Long testId = 1L;

    underTest = new TrackingDataMapper();

    entityTest = new TrackingData();
    entityTest.setId(testId);
    entityTest.setDevice(DEVICE);
    entityTest.setTime(TIME);
    entityTest.setLatitude(LATITUDE);
    entityTest.setLongitude(LONGITUDE);
    entityTest.setOwner(OWNER);

    dtoTest = new TrackingDataDTO();
    dtoTest.setDevice(DEVICE);
    dtoTest.setTime(TIME);
    dtoTest.setLatitude(LATITUDE);
    dtoTest.setLongitude(LONGITUDE);
    dtoTest.setOwner(OWNER);
  }

  @Test
  void mapEntity() {

    TrackingData entity = underTest.map(dtoTest);

    assertNull(entity.getId());
    assertEquals(DEVICE, entity.getDevice());
    assertEquals(TIME, entity.getTime());
    assertEquals(LATITUDE, entity.getLatitude());
    assertEquals(LONGITUDE, entity.getLongitude());
    assertEquals(OWNER, entity.getOwner());
  }

  @Test
  void mapDTO() {

    TrackingDataDTO dto = underTest.map(entityTest);

    assertEquals(DEVICE, dto.getDevice());
    assertEquals(TIME, dto.getTime());
    assertEquals(LATITUDE, dto.getLatitude());
    assertEquals(LONGITUDE, dto.getLongitude());
    assertEquals(OWNER, dto.getOwner());
  }

  @Test
  void mapEntityWithNullParameter() {

    TrackingData result = underTest.map((TrackingDataDTO) null);
    assertNull(result);
  }

  @Test
  void mapDtoWithNullParameter() {

    TrackingDataDTO result = underTest.map((TrackingData) null);
    assertNull(result);
  }
}
