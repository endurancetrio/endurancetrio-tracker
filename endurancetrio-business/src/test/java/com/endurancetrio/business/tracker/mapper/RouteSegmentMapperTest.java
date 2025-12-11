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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteSegmentMapperTest {

  private static final Long ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private RouteSegmentDTO testDTO;
  private RouteSegment testEntity;

  @InjectMocks
  private RouteSegmentMapper underTest;

  @BeforeEach
  void setUp() {
    testDTO = new RouteSegmentDTO(ID, ORDER, START_DEVICE, END_DEVICE);

    testEntity = new RouteSegment();
    testEntity.setId(ID);
    testEntity.setOrder(ORDER);
    testEntity.setStartDevice(START_DEVICE);
    testEntity.setEndDevice(END_DEVICE);
  }

  @Test
  void mapDTO() {

    RouteSegment result = underTest.map(testDTO);

    assertNotNull(result);
    assertEquals(ID, result.getId());
    assertEquals(ORDER, result.getOrder());
    assertEquals(START_DEVICE, result.getStartDevice());
    assertEquals(END_DEVICE, result.getEndDevice());
  }

  @Test
  void mapEntity() {
    RouteSegmentDTO result = underTest.map(testEntity);

    assertNotNull(result);
    assertEquals(ID, result.id());
    assertEquals(ORDER, result.order());
    assertEquals(START_DEVICE, result.startDevice());
    assertEquals(END_DEVICE, result.endDevice());
  }

  @Test
  void updateEntity() {
    RouteSegmentDTO updatedDto = new RouteSegmentDTO(ID, 2, "SDXYZ", "SDUVW");

    underTest.updateEntity(updatedDto, testEntity);

    assertEquals(2, testEntity.getOrder());
    assertEquals("SDXYZ", testEntity.getStartDevice());
    assertEquals("SDUVW", testEntity.getEndDevice());
  }

  @Test
  void mapNullDTO() {
    RouteSegment result = underTest.map((RouteSegmentDTO) null);

    assertNull(result);
  }

  @Test
  void mapNullEntity() {
    RouteSegmentDTO result = underTest.map((RouteSegment) null);

    assertNull(result);
  }

  @Test
  void updateEntityWithNullDTO() {

    underTest.updateEntity(null, testEntity);

    assertEquals(ORDER, testEntity.getOrder());
    assertEquals(START_DEVICE, testEntity.getStartDevice());
    assertEquals(END_DEVICE, testEntity.getEndDevice());
  }

  @Test
  void updateEntityWithNullEntity() {
    RouteSegmentDTO updatedDto = new RouteSegmentDTO(ID, 2, "SDXYZ", "SDUVW");

    assertDoesNotThrow(() -> underTest.updateEntity(updatedDto, null));
  }
}
