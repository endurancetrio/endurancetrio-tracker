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
 * Unit test for the {@link Route} entity.
 * <p>
 * This test may seem redundant since it only verify getters and constructor, but its purpose is to
 * establish a testing culture from the very beginning of the project. It serves as a reminder that
 * every part of the application should be testable and that tests should always be present.
 */
class RouteTest {

  private static final Long SEGMENT_ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private static final String REFERENCE = "SMP";

  private Route underTest;

  @BeforeEach
  void setUp() {
    RouteSegment testSegment = new RouteSegment();
    testSegment.setId(SEGMENT_ID);
    testSegment.setOrder(ORDER);
    testSegment.setStartDevice(START_DEVICE);
    testSegment.setEndDevice(END_DEVICE);

    underTest = new Route();
    underTest.setReference(REFERENCE);
    underTest.getSegments().add(testSegment);
  }

  @Test
  void entityShouldRetainValues() {

    assertEquals(REFERENCE, underTest.getReference());
    assertEquals(1, underTest.getSegments().size());
    RouteSegment segment = underTest.getSegments().getFirst();
    assertEquals(SEGMENT_ID, segment.getId());
    assertEquals(ORDER, segment.getOrder());
    assertEquals(START_DEVICE, segment.getStartDevice());
    assertEquals(END_DEVICE, segment.getEndDevice());
  }
}
