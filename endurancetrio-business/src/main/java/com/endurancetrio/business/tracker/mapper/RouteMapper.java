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

import com.endurancetrio.business.common.exception.BadRequestException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.data.tracker.model.entity.Route;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {

  private static final Logger LOG = LoggerFactory.getLogger(RouteMapper.class);

  private final RouteSegmentMapper routeSegmentMapper;

  @Autowired
  public RouteMapper(RouteSegmentMapper routeSegmentMapper) {
    this.routeSegmentMapper = routeSegmentMapper;
  }

  /**
   * Maps a Route entity to a RouteDTO.
   *
   * @param entity the Route entity to be mapped
   * @return the corresponding RouteDTO
   */
  public RouteDTO map(Route entity) {

    if (entity == null) {
      return null;
    }

    return new RouteDTO(entity.getId(), entity.getReference(),
        entity.getSegments().stream().map(routeSegmentMapper::map).toList()
    );
  }

  /**
   * Maps a RouteDTO to a Route entity.
   *
   * @param dto the RouteDTO to be mapped
   * @return the corresponding Route entity
   */
  public Route map(RouteDTO dto) {

    if (dto == null) {
      return null;
    }

    Route entity = new Route();
    entity.setId(dto.id());
    entity.setReference(dto.reference());
    entity.setSegments(dto.segments().stream().map(routeSegmentMapper::map).toList());

    return entity;
  }

  /**
   * Updates an existing Route entity with data from a RouteDTO.
   *
   * @param dto    the RouteDTO containing updated data
   * @param entity the Route entity to be updated
   */
  public void updateEntity(RouteDTO dto, Route entity) {

    if (dto == null || entity == null) {
      return;
    }

    entity.setReference(dto.reference());

    Map<Long, RouteSegment> existingSegments = entity.getSegments()
        .stream()
        .collect(Collectors.toMap(RouteSegment::getId, Function.identity()));
    Set<Long> processedIds = new HashSet<>();

    for (RouteSegmentDTO segmentDTO : dto.segments()) {
      if (segmentDTO.id() == null) {
        entity.getSegments().add(routeSegmentMapper.map(segmentDTO));
      } else {
        RouteSegment existingSegment = existingSegments.get(segmentDTO.id());
        if (existingSegment != null) {
          routeSegmentMapper.updateEntity(segmentDTO, existingSegment);
          processedIds.add(segmentDTO.id());
        } else {
          String errorMessage = String.format(
              "Segment with ID %d cannot be updated as it was not found on Route %d.",
              segmentDTO.id(), entity.getId()
          );

          LOG.error(errorMessage);
          throw new BadRequestException(errorMessage, EnduranceTrioError.BAD_REQUEST);
        }
      }
    }

    entity.getSegments()
        .removeIf(segment -> segment.getId() != null && !processedIds.contains(segment.getId()));
  }
}
