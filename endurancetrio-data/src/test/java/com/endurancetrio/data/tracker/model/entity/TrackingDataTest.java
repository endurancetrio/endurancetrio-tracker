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

package com.endurancetrio.data.tracker.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link TrackingData} entity.
 * <p>
 * This test may seem redundant since it only verify getters and setters, but its purpose is to
 * establish a testing culture from the very beginning of the project. It serves as a reminder that
 * every part of the application should be testable and that tests should always be present.
 */
class TrackingDataTest {

  private static final Long ID = 1L;
  private static final String OWNER = "system";
  private static final String DEVICE = "SDABC";
  private static final Instant TIME = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double LATITUDE = 39.510058;
  private static final Double LONGITUDE = -9.136079;
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";

  private TrackingData underTest;

  @BeforeEach
  void setUp() {

    TrackerAccount account = new TrackerAccount(OWNER, KEY, true);

    underTest = new TrackingData();
    underTest.setId(ID);
    underTest.setAccount(account);
    underTest.setDevice(DEVICE);
    underTest.setTime(TIME);
    underTest.setLatitude(LATITUDE);
    underTest.setLongitude(LONGITUDE);
  }

  @Test
  void entityShouldRetainValues() {

    assertEquals(ID, underTest.getId());
    assertEquals(OWNER, underTest.getAccount().getOwner());
    assertEquals(DEVICE, underTest.getDevice());
    assertEquals(TIME, underTest.getTime());
    assertEquals(LATITUDE, underTest.getLatitude());
    assertEquals(LONGITUDE, underTest.getLongitude());
  }
}
