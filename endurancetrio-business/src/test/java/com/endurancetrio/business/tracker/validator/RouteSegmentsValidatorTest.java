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

package com.endurancetrio.business.tracker.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RouteSegmentsValidatorTest {

  private static final Integer S1_ORDER = 1;
  private static final String S1_START_DEVICE = "SDABC";
  private static final String S1_END_DEVICE = "SDDEF";

  private static final Integer S2_ORDER = 2;
  private static final String S2_START_DEVICE = "SDDEF";
  private static final String S2_END_DEVICE = "SDFGH";

  private static final Long ROUTE_ID = 1L;
  private static final String REFERENCE = "20260921ETU001-001S";

  private RouteSegmentDTO testSegment1;
  private RouteSegmentDTO testSegment2;

  private Validator underTest;

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      underTest = factory.getValidator();
    }

    testSegment1 = new RouteSegmentDTO(null, S1_ORDER, S1_START_DEVICE, S1_END_DEVICE);
    testSegment2 = new RouteSegmentDTO(null, S2_ORDER, S2_START_DEVICE, S2_END_DEVICE);
  }

  @Test
  void isValid() {

    RouteDTO testRoute = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegment1, testSegment2));

    Set<ConstraintViolation<RouteDTO>> result = underTest.validate(testRoute);

    assertTrue(result.isEmpty());
  }

  @Test
  void isValidWithInvalidFirstOrder() {

    RouteSegmentDTO caseSegment = new RouteSegmentDTO(null, S2_ORDER, S1_START_DEVICE,
        S1_END_DEVICE
    );
    RouteDTO testRoute = new RouteDTO(null, REFERENCE, List.of(caseSegment));

    Set<ConstraintViolation<RouteDTO>> result = underTest.validate(testRoute);

    assertEquals(1, result.size());
    assertEquals("Route must start with a segment of order 1",
        result.iterator().next().getMessage()
    );
  }

  @Test
  void isValidWithInvalidDeviceSequence() {

    RouteSegmentDTO caseSegment = new RouteSegmentDTO(null, S2_ORDER, S2_END_DEVICE,
        S2_START_DEVICE
    );
    RouteDTO testRoute = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegment1, caseSegment));

    Set<ConstraintViolation<RouteDTO>> result = underTest.validate(testRoute);

    assertEquals(1, result.size());
    assertTrue(result.iterator().next().getMessage().contains("Device mismatch"));
  }

  @Test
  void isValidWithSequenceError() {

    RouteSegmentDTO caseSegment = new RouteSegmentDTO(null, 3, S2_START_DEVICE, S2_END_DEVICE);
    RouteDTO testRoute = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegment1, caseSegment));

    Set<ConstraintViolation<RouteDTO>> result = underTest.validate(testRoute);

    assertEquals(1, result.size());
    assertTrue(result.iterator().next().getMessage().contains("Sequence error"));
  }

  @Test
  void isValidWithMultipleViolations() {

    RouteSegmentDTO caseSegment = new RouteSegmentDTO(null, 3, S2_END_DEVICE,
        S2_START_DEVICE
    );
    RouteDTO testRoute = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegment1, caseSegment));

    Set<ConstraintViolation<RouteDTO>> result = underTest.validate(testRoute);

    assertEquals(2, result.size());
  }
}
