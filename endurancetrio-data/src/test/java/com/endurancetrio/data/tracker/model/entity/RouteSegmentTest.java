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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link RouteSegment} entity.
 * <p>
 * This test may seem redundant since it only verify getters and constructor, but its purpose is to
 * establish a testing culture from the very beginning of the project. It serves as a reminder that
 * every part of the application should be testable and that tests should always be present.
 */
class RouteSegmentTest {

  private static final Long ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private RouteSegment underTest;

  @BeforeEach
  void setUp() {
    underTest = new RouteSegment();
    underTest.setId(ID);
    underTest.setOrder(ORDER);
    underTest.setStartDevice(START_DEVICE);
    underTest.setEndDevice(END_DEVICE);
  }

  @Test
  void entityShouldRetainValues() {

    assertEquals(ID, underTest.getId());
    assertEquals(ORDER, underTest.getOrder());
    assertEquals(START_DEVICE, underTest.getStartDevice());
    assertEquals(END_DEVICE, underTest.getEndDevice());
  }
}
