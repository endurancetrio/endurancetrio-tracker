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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteMapperTest {

  private static final Long SEGMENT_ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private static final Long ROUTE_ID = 1L;
  private static final String REFERENCE = "SMP";

  private RouteDTO testDTO;
  private Route testEntity;

  @Mock
  private RouteSegmentMapper routeSegmentMapper;

  @InjectMocks
  private RouteMapper underTest;

  @BeforeEach
  void setUp() {

    RouteSegmentDTO testSegmentDTO = new RouteSegmentDTO(SEGMENT_ID, ORDER, START_DEVICE,
        END_DEVICE
    );

    RouteSegment testSegment = new RouteSegment();
    testSegment.setId(SEGMENT_ID);
    testSegment.setOrder(ORDER);
    testSegment.setStartDevice(START_DEVICE);
    testSegment.setEndDevice(END_DEVICE);

    testDTO = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegmentDTO));

    testEntity = new Route();
    testEntity.setId(ROUTE_ID);
    testEntity.setReference(REFERENCE);
    testEntity.getSegments().add(testSegment);
  }

  @Test
  void mapDTO() {

    when(routeSegmentMapper.map(testDTO.segments().getFirst())).thenReturn(
        testEntity.getSegments().getFirst());

    Route result = underTest.map(testDTO);

    verify(routeSegmentMapper, times(1)).map(testDTO.segments().getFirst());

    assertNotNull(result);
    assertEquals(ROUTE_ID, result.getId());
    assertEquals(REFERENCE, result.getReference());
    assertEquals(1, result.getSegments().size());

    RouteSegment segment = result.getSegments().getFirst();
    assertEquals(SEGMENT_ID, segment.getId());
    assertEquals(ORDER, segment.getOrder());
    assertEquals(START_DEVICE, segment.getStartDevice());
    assertEquals(END_DEVICE, segment.getEndDevice());
  }

  @Test
  void mapEntity() {
    when(routeSegmentMapper.map(testEntity.getSegments().getFirst())).thenReturn(
        new RouteSegmentDTO(SEGMENT_ID, ORDER, START_DEVICE, END_DEVICE));

    RouteDTO result = underTest.map(testEntity);

    verify(routeSegmentMapper, times(1)).map(testEntity.getSegments().getFirst());

    assertNotNull(result);
    assertEquals(ROUTE_ID, result.id());
    assertEquals(REFERENCE, result.reference());
    assertEquals(1, result.segments().size());

    RouteSegmentDTO segment = result.segments().getFirst();
    assertEquals(SEGMENT_ID, segment.id());
    assertEquals(ORDER, segment.order());
    assertEquals(START_DEVICE, segment.startDevice());
    assertEquals(END_DEVICE, segment.endDevice());
  }

  @Test
  void updateEntity() {
    String newReference = "NEW-SMP";
    RouteDTO updatedDto = new RouteDTO(ROUTE_ID, newReference, List.of());

    underTest.updateEntity(updatedDto, testEntity);

    assertNotNull(testEntity);
    assertEquals(ROUTE_ID, testEntity.getId());
    assertEquals(newReference, testEntity.getReference());
    assertEquals(0, testEntity.getSegments().size());
  }

  @Test
  void mapNullDTO() {
    Route result = underTest.map((RouteDTO) null);

    assertNull(result);
  }

  @Test
  void mapNullEntity() {
    RouteDTO result = underTest.map((Route) null);

    assertNull(result);
  }

  @Test
  void updateEntityWithNullDTO() {

    underTest.updateEntity(null, testEntity);

    assertEquals(ROUTE_ID, testEntity.getId());
    assertEquals(REFERENCE, testEntity.getReference());
    assertEquals(1, testEntity.getSegments().size());
  }

  @Test
  void updateEntityWithNullEntity() {
    String newReference = "NEW-SMP";
    RouteDTO updatedDto = new RouteDTO(ROUTE_ID, newReference, List.of());

    assertDoesNotThrow(() -> underTest.updateEntity(updatedDto, null));
  }
}
