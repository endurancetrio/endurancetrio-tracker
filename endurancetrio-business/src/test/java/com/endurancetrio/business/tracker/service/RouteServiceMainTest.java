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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteMetricsDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.business.tracker.dto.geojson.Feature;
import com.endurancetrio.business.tracker.mapper.RouteMapper;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
import com.endurancetrio.data.tracker.repository.RouteRepository;
import java.time.Instant;
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

  private static final String OWNER = "system";
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";
  private static final boolean IS_ENABLED = true;

  private static final Long DT1_ID = 1L;
  private static final String DT1_DEVICE = "SDABC";
  private static final Instant DT1_TIME = Instant.parse("2026-09-19T06:00:00Z");
  private static final Double DT1_LATITUDE = 39.510058;
  private static final Double DT1_LONGITUDE = -9.136079;
  private static final Long DT2_ID = 2L;
  private static final String DT2_DEVICE = "SDDEF";
  private static final Instant DT2_TIME = Instant.parse("2026-09-19T06:00:06Z");
  private static final Double DT2_LATITUDE = 39.509001;
  private static final Double DT2_LONGITUDE = -9.139602;
  private static final Boolean IS_ACTIVE = true;

  private static final Long SEGMENT_ID = 1L;
  private static final Integer ORDER = 1;
  private static final String START_DEVICE = "SDABC";
  private static final String END_DEVICE = "SDDEF";

  private static final Long ROUTE_ID = 1L;
  private static final String REFERENCE = "SMP";

  private DeviceTelemetry dt1TestDeviceTelemetry;
  private DeviceTelemetry dt2TestDeviceTelemetry;
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

    TrackerAccount testAccount = new TrackerAccount(OWNER, KEY, IS_ENABLED);
    RouteSegmentDTO testSegmentDTO = new RouteSegmentDTO(null, ORDER, START_DEVICE, END_DEVICE);

    dt1TestDeviceTelemetry = new DeviceTelemetry(testAccount, DT1_DEVICE, DT1_TIME, DT1_LATITUDE,
        DT1_LONGITUDE, IS_ACTIVE
    );
    dt2TestDeviceTelemetry = new DeviceTelemetry(testAccount, DT2_DEVICE, DT2_TIME, DT2_LATITUDE,
        DT2_LONGITUDE, IS_ACTIVE
    );
    dt1TestDeviceTelemetry.setId(DT1_ID);
    dt2TestDeviceTelemetry.setId(DT2_ID);

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

  @Test
  void findAll() {
    when(routeRepository.findAll()).thenReturn(List.of(testEntity));
    when(routeMapper.map(any(Route.class))).thenReturn(testDTO);

    List<RouteDTO> result = underTest.findAll();

    verify(routeRepository, times(1)).findAll();
    verify(routeMapper, times(1)).map(any(Route.class));

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(ROUTE_ID, result.getFirst().id());
    assertEquals(REFERENCE, result.getFirst().reference());
    assertEquals(1, result.getFirst().segments().size());
  }

  @Test
  void findAllWhenNoRoutesExist() {
    when(routeRepository.findAll()).thenReturn(List.of());

    List<RouteDTO> result = underTest.findAll();

    verify(routeRepository, times(1)).findAll();
    verify(routeMapper, times(0)).map(any(Route.class));

    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void findById() {

    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(testEntity));
    when(routeMapper.map(any(Route.class))).thenReturn(testDTO);

    RouteDTO result = underTest.findById(ROUTE_ID);

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeMapper, times(1)).map(any(Route.class));

    assertNotNull(result);
    assertEquals(ROUTE_ID, result.id());
    assertEquals(REFERENCE, result.reference());
    assertEquals(1, result.segments().size());
  }

  @Test
  void findByIdWhenRouteDoesNotExist() {
    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.empty());

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.findById(ROUTE_ID)
    );

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeMapper, times(0)).map(any(Route.class));

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
  }

  @Test
  void getRouteMetrics() {

    List<DeviceTelemetry> testTelemetry = List.of(dt1TestDeviceTelemetry, dt2TestDeviceTelemetry);

    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(testEntity));
    when(routeMapper.map(testEntity)).thenReturn(testDTO);
    when(deviceTelemetryRepository.findMostRecentByDevices(any())).thenReturn(testTelemetry);

    RouteMetricsDTO result = underTest.getRouteMetrics(ROUTE_ID);
    List<Feature> lineStringResult = result.features()
        .stream()
        .filter(feature -> feature.geometry().getType().equals("LineString"))
        .toList();

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeMapper, times(1)).map(testEntity);
    verify(deviceTelemetryRepository, times(1)).findMostRecentByDevices(any());

    assertNotNull(result);
    assertEquals(3, result.features().size());
    assertEquals("Point", result.features().getFirst().geometry().getType());

    assertEquals(1, lineStringResult.size());
    assertNotNull(lineStringResult.getFirst().properties().get("id"));
    assertNotNull(lineStringResult.getFirst().properties().get("reference"));
    assertNotNull(lineStringResult.getFirst().properties().get("totalDistance"));
    assertNotNull(lineStringResult.getFirst().properties().get("segments"));
  }

  @Test
  void getRouteMetricsWhenRouteIsNonExistent() {

    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.empty());

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.getRouteMetrics(ROUTE_ID)
    );

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeMapper, never()).map(testEntity);
    verify(deviceTelemetryRepository, never()).findMostRecentByDevices(any());

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
  }

  @Test
  void getRouteMetricsWhenTelemetryIsMissing() {

    List<DeviceTelemetry> testTelemetry = List.of(dt1TestDeviceTelemetry);

    when(routeRepository.findById(ROUTE_ID)).thenReturn(Optional.of(testEntity));
    when(routeMapper.map(testEntity)).thenReturn(testDTO);
    when(deviceTelemetryRepository.findMostRecentByDevices(any())).thenReturn(testTelemetry);

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.getRouteMetrics(ROUTE_ID)
    );

    verify(routeRepository, times(1)).findById(ROUTE_ID);
    verify(routeMapper, times(1)).map(testEntity);
    verify(deviceTelemetryRepository, times(1)).findMostRecentByDevices(any());

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
  }
}
