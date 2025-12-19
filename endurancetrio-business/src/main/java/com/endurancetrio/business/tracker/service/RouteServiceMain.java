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

import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteMetricsDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.business.tracker.dto.geojson.Feature;
import com.endurancetrio.business.tracker.dto.geojson.LineStringGeometry;
import com.endurancetrio.business.tracker.dto.geojson.PointGeometry;
import com.endurancetrio.business.tracker.dto.geojson.RouteSegmentProperty;
import com.endurancetrio.business.tracker.mapper.RouteMapper;
import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.repository.DeviceTelemetryRepository;
import com.endurancetrio.data.tracker.repository.RouteRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteServiceMain implements RouteService {

  private static final Logger LOG = LoggerFactory.getLogger(RouteServiceMain.class);

  private final DeviceTelemetryRepository deviceTelemetryRepository;
  private final RouteRepository routeRepository;
  private final RouteMapper routeMapper;

  @Autowired
  public RouteServiceMain(
      DeviceTelemetryRepository deviceTelemetryRepository, RouteRepository routeRepository,
      RouteMapper routeMapper
  ) {
    this.deviceTelemetryRepository = deviceTelemetryRepository;
    this.routeRepository = routeRepository;
    this.routeMapper = routeMapper;
  }

  @Override
  @Transactional
  public RouteDTO save(RouteDTO routeDTO) {

    validateRouteDevices(routeDTO);

    Route entity;
    if (routeDTO.id() != null) {

      entity = routeRepository.findById(routeDTO.id()).orElseThrow(() -> {
        String errorMessage = String.format("Route update failed: No route found with ID %d",
            routeDTO.id()
        );

        LOG.warn(errorMessage);
        return new NotFoundException(errorMessage, EnduranceTrioError.NOT_FOUND);
      });

      routeMapper.updateEntity(routeDTO, entity);
    } else {

      entity = routeMapper.map(routeDTO);
    }

    return routeMapper.map(routeRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RouteDTO> findAll() {

    List<@NonNull Route> routes = routeRepository.findAll();

    return routes.stream().map(routeMapper::map).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RouteDTO findById(Long id) {

    return getRouteDTO(id);
  }

  @Override
  @Transactional(readOnly = true)
  public RouteMetricsDTO getRouteMetrics(Long id) {

    RouteDTO route = getRouteDTO(id);
    ArrayList<String> devices = new ArrayList<>(extractDevices(route));
    List<DeviceTelemetry> telemetry = getDevicesTelemetry(devices);

    Map<String, DeviceTelemetry> devicesMap = telemetry.stream()
        .collect(Collectors.toMap(DeviceTelemetry::getDevice, Function.identity()));

    long totalDistance = 0L;
    List<Feature> features = new ArrayList<>();
    List<List<Double>> lineCoordinates = new ArrayList<>();
    List<RouteSegmentProperty> segmentProperties = new ArrayList<>();

    List<RouteSegmentDTO> segments = route.segments()
        .stream()
        .sorted(Comparator.comparing(RouteSegmentDTO::order))
        .toList();

    for (int i = 0; i < segments.size(); i++) {
      RouteSegmentDTO segment = segments.get(i);

      List<Double> segmentStart = getGeoJsonCoordinates(segment.startDevice(), devicesMap);
      List<Double> segmentEnd = getGeoJsonCoordinates(segment.endDevice(), devicesMap);

      // We set the LineString starting point when processing the route's first segment
      if (i == 0) {
        lineCoordinates.add(segmentStart);
        features.add(createGeometryPoint(segmentStart, i + 1));
      }

      lineCoordinates.add(segmentEnd);
      features.add(createGeometryPoint(segmentEnd, i + 2));

      long segmentDistance = Math.round(calculateDistance(segmentStart, segmentEnd));
      totalDistance += segmentDistance;

      segmentProperties.add(new RouteSegmentProperty(segment.order(), segmentDistance));
    }

    Map<String, Object> routeProperties = Map.of("id", route.id(), "reference", route.reference(),
        "totalDistance", totalDistance, "segments", segmentProperties
    );

    features.add(createGeometryLineString(lineCoordinates, routeProperties));

    return new RouteMetricsDTO(features);
  }

  /**
   * Calculates the distance between two GeoJSON points using the Haversine formula.
   * <p>
   * This method expects valid GeoJSON coordinates [longitude, latitude]. It calculates the
   * great-circle distance between them on a sphere approximation of the Earth.
   *
   * @param startCoordinates The starting point in GeoJSON format [longitude, latitude]. Must not be
   *                         null and must contain exactly 2 values.
   * @param endCoordinates   The ending point in GeoJSON format [longitude, latitude]. Must not be
   *                         null and must contain exactly 2 values.
   * @return The distance in meters, rounded to the nearest integer.
   * @throws IllegalArgumentException if coordinates are null or do not contain exactly 2 values.
   * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>
   * @see <a href="https://en.wikipedia.org/wiki/Earth_radius">Earth radius</a>
   * @see <a href="https://rosettacode.org/wiki/Haversine_formula#Java">Rosetta Code
   * Implementation</a>
   */
  private double calculateDistance(List<Double> startCoordinates, List<Double> endCoordinates) {

    // IUGG (https://iugg.org/) standard mean radius of Earth in meters
    double meanEarthRadius = 6371008.7714;

    double halfChordLength = calculateHalfChordLength(startCoordinates, endCoordinates);
    double angularDistance = 2 * Math.asin(halfChordLength);

    return angularDistance * meanEarthRadius;
  }

  /**
   * Calculates the half-chord length between two points on a unit sphere.
   * <p>
   * This value corresponds to {@code Math.sqrt(a)} in the standard Haversine formula notation,
   * representing the straight-line distance between the two points through the sphere, normalized
   * to a unit sphere radius.
   *
   * @param start The starting coordinates [longitude, latitude].
   * @param end   The ending coordinates [longitude, latitude].
   * @return The half-chord length (value between 0.0 and 1.0).
   * @throws BadRequestException if coordinates are null or strictly not a pair of values.
   */
  private static double calculateHalfChordLength(List<Double> start, List<Double> end) {

    if (start == null || end == null || start.size() != 2 || end.size() != 2) {
      String errorMessage = "Coordinates must be non-null and contain exactly [longitude, latitude]";
      LOG.error(errorMessage);
      throw new BadRequestException(errorMessage, EnduranceTrioError.BAD_REQUEST);
    }

    // GeoJSON index 1 is Latitude
    double startLatitude = Math.toRadians(start.get(1));
    double endLatitude = Math.toRadians(end.get(1));
    double latitudeDelta = endLatitude - startLatitude;

    // GeoJSON index 0 is Longitude
    double longitudeDelta = Math.toRadians(end.getFirst() - start.getFirst());

    return Math.sqrt(
        Math.pow(Math.sin(latitudeDelta / 2), 2) +
            Math.pow(Math.sin(longitudeDelta / 2), 2) *
                Math.cos(startLatitude) * Math.cos(endLatitude)
    );
  }

  /**
   * Constructs a GeoJSON Feature containing a LineString geometry.
   * <p>
   * This feature represents the full path of the route.
   *
   * @param lineCoordinates The list of coordinate points defining the line string.
   * @param routeProperties A map of properties describing the route (e.g., id, distance).
   * @return A {@link Feature} object wrapping the LineString and its properties.
   */
  private Feature createGeometryLineString(
      List<List<Double>> lineCoordinates, Map<String, Object> routeProperties) {

    return Feature.of(new LineStringGeometry(lineCoordinates), routeProperties);
  }

  /**
   * Constructs a GeoJSON Feature containing a Point geometry.
   * <p>
   * This feature represents a specific waypoint or segment endpoint within the route. It includes
   * an "order" property to indicate its sequence in the route.
   *
   * @param segmentStart The coordinates of the point [longitude, latitude].
   * @param order        The sequential order of this point in the route.
   * @return A {@link Feature} object wrapping the Point and its order property.
   */
  private Feature createGeometryPoint(List<Double> segmentStart, int order) {

    return Feature.of(new PointGeometry(segmentStart), Map.of("order", order));
  }

  /**
   * Extracts unique device identifiers from the segments of the provided RouteDTO.
   *
   * @param routeDTO the RouteDTO containing segments with device information
   * @return a set of unique device identifiers
   */
  private Set<String> extractDevices(RouteDTO routeDTO) {

    return Optional.ofNullable(routeDTO)
        .map(RouteDTO::segments)
        .orElseGet(Collections::emptyList)
        .stream()
        .filter(Objects::nonNull)
        .flatMap(segment -> Stream.of(segment.startDevice(), segment.endDevice()))
        .collect(Collectors.toSet());
  }

  /**
   * Extracts and formats coordinates from a telemetry record into GeoJSON format.
   *
   * @param device     The device identifier key.
   * @param devicesMap A map of available telemetry data keyed by device identifier.
   * @return A list containing exactly two doubles: [longitude, latitude].
   */
  private List<Double> getGeoJsonCoordinates(
      String device, Map<String, DeviceTelemetry> devicesMap) {

    DeviceTelemetry deviceTelemetry = devicesMap.get(device);

    return List.of(deviceTelemetry.getLongitude(), deviceTelemetry.getLatitude());
  }

  /**
   * Retrieves a Route by its ID and maps it to a DTO.
   *
   * @param id The unique identifier of the route.
   * @return The mapped {@link RouteDTO}.
   * @throws NotFoundException if no route exists with the given ID.
   */
  private RouteDTO getRouteDTO(Long id) {

    Route route = routeRepository.findById(id).orElseThrow(() -> {
      String errorMessage = String.format("No route found with ID %d", id);
      LOG.warn(errorMessage);
      return new NotFoundException(errorMessage, EnduranceTrioError.NOT_FOUND);
    });

    return routeMapper.map(route);
  }

  /**
   * Retrieves the most recent telemetry data for a list of devices and validates completeness.
   * <p>
   * This method ensures that telemetry data is available for <b>every</b> requested device.
   *
   * @param devices The list of device identifiers to query.
   * @return A list of {@link DeviceTelemetry} objects corresponding to the devices.
   * @throws NotFoundException if telemetry data is missing for any of the requested devices.
   */
  private List<DeviceTelemetry> getDevicesTelemetry(ArrayList<String> devices) {

    List<DeviceTelemetry> telemetry = deviceTelemetryRepository.findMostRecentByDevices(devices);

    if (telemetry.size() != devices.size()) {
      Set<String> foundDevices = telemetry.stream()
          .map(DeviceTelemetry::getDevice)
          .collect(Collectors.toSet());

      List<String> missingDevices = devices.stream()
          .filter(device -> !foundDevices.contains(device))
          .toList();

      String errorMessage = String.format("Telemetry data missing for devices: %s", missingDevices);
      LOG.error(errorMessage);
      throw new NotFoundException(errorMessage, EnduranceTrioError.NOT_FOUND);
    }

    return telemetry;
  }

  /**
   * Validates that all devices referenced in the RouteDTO exist in the system.
   *
   * @param routeDTO the RouteDTO containing segments with device information
   * @throws NotFoundException if any device is not found in the system
   */
  private void validateRouteDevices(RouteDTO routeDTO) {
    Set<String> routeDevices = extractDevices(routeDTO);
    Set<String> existingDevices = deviceTelemetryRepository.findExistingDevicesFrom(routeDevices);

    if (existingDevices.size() != routeDevices.size()) {
      Set<String> missingDevices = routeDevices.stream()
          .filter(device -> !existingDevices.contains(device))
          .collect(Collectors.toSet());

      String errorMessage = String.format(
          "Cannot process route. The following devices are not registered: %s",
          String.join(", ", missingDevices)
      );

      LOG.warn(errorMessage);
      throw new BadRequestException(errorMessage, EnduranceTrioError.BAD_REQUEST);
    }
  }
}
