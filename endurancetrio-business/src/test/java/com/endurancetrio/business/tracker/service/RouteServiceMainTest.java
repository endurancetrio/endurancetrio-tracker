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
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.business.tracker.mapper.RouteMapper;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
import com.endurancetrio.data.tracker.repository.RouteRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteServiceMainTest {

  private static final Long SEGMENT_ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private static final Long ROUTE_ID = 1L;
  private static final String REFERENCE = "SMP";

  private RouteDTO testDTO;
  private Route testEntity;

  @Mock
  private RouteMapper routeMapper;

  @Mock
  private DeviceTelemetryRepository deviceTelemetryRepository;

  @Mock
  private RouteRepository routeRepository;

  @InjectMocks
  private RouteServiceMain underTest;

  @BeforeEach
  void setUp() {
    RouteSegmentDTO testSegmentDTO = new RouteSegmentDTO(null, ORDER, START_DEVICE, END_DEVICE);

    RouteSegment testSegment = new RouteSegment();
    testSegment.setId(SEGMENT_ID);
    testSegment.setOrder(ORDER);
    testSegment.setStartDevice(START_DEVICE);
    testSegment.setEndDevice(END_DEVICE);

    testDTO = new RouteDTO(ROUTE_ID, REFERENCE, List.of(testSegmentDTO));

    testEntity = new Route();
    testEntity.setId(ROUTE_ID);
    testEntity.setReference(REFERENCE);
    testEntity.setSegments(List.of(testSegment));
  }

  @Test
  void save() {

    Route dbEntity = new Route();
    dbEntity.setId(ROUTE_ID);
    dbEntity.setReference("OLD-SMP");
    dbEntity.setSegments(List.of());

    RouteDTO expectedDTO = new RouteDTO(ROUTE_ID, REFERENCE,
        List.of(new RouteSegmentDTO(SEGMENT_ID, ORDER, START_DEVICE, END_DEVICE))
    );

    when(deviceTelemetryRepository.findExistingDevicesFrom(anySet())).thenReturn(
        Set.of(START_DEVICE, END_DEVICE));
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(dbEntity));
    doAnswer(invocation -> {
      Route target = invocation.getArgument(1);
      target.setReference(testEntity.getReference());
      target.setSegments(testEntity.getSegments());
      return null;
    }).when(routeMapper).updateEntity(testDTO, dbEntity);
    when(routeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    when(routeMapper.map(any(Route.class))).thenReturn(expectedDTO);

    RouteDTO result = underTest.save(testDTO);

    verify(deviceTelemetryRepository, times(1)).findExistingDevicesFrom(anySet());
    verify(routeRepository, times(1)).save(any());
    verify(routeMapper, times(1)).updateEntity(testDTO, dbEntity);
    verify(routeRepository, times(1)).save(dbEntity);
    verify(routeMapper, times(1)).map(any(Route.class));

    assertNotNull(result);
    assertEquals(ROUTE_ID, result.id());
    assertEquals(REFERENCE, result.reference());
    assertEquals(1, result.segments().size());
  }

  @Test
  void saveWithInvalidSegments() {

    RouteSegmentDTO invalidSegment = new RouteSegmentDTO(null, ORDER, "SDXXX", "SDYYY");
    RouteDTO invalidDTO = new RouteDTO(ROUTE_ID, REFERENCE, List.of(invalidSegment));

    when(deviceTelemetryRepository.findExistingDevicesFrom(anySet())).thenReturn(Set.of());

    BadRequestException result = assertThrows(BadRequestException.class,
        () -> underTest.save(invalidDTO)
    );

    verify(deviceTelemetryRepository, times(1)).findExistingDevicesFrom(anySet());
    verify(routeRepository, times(0)).save(any());
    verify(routeMapper, times(0)).map(any(Route.class));

    assertEquals(EnduranceTrioError.BAD_REQUEST.getCode(), result.getCode());
  }

  @Test
  void saveWithNonExistingRoute() {

    when(deviceTelemetryRepository.findExistingDevicesFrom(anySet())).thenReturn(
        Set.of(START_DEVICE, END_DEVICE));
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.empty());

    NotFoundException result = assertThrows(NotFoundException.class, () -> underTest.save(testDTO));

    verify(deviceTelemetryRepository, times(1)).findExistingDevicesFrom(anySet());
    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeRepository, times(0)).save(any());
    verify(routeMapper, times(0)).map(any(Route.class));

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
  }
}
