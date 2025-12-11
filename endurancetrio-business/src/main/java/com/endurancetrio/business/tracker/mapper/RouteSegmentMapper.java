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

import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import com.endurancetrio.data.tracker.model.entity.RouteSegment;
import org.springframework.stereotype.Component;

@Component
public class RouteSegmentMapper {

  /**
   * Maps a RouteSegment entity to a RouteSegmentDTO.
   *
   * @param entity the RouteSegment entity to be mapped
   * @return the corresponding RouteSegmentDTO
   */
  RouteSegmentDTO map(RouteSegment entity) {

    if (entity == null) {
      return null;
    }

    return new RouteSegmentDTO(entity.getId(), entity.getOrder(),
        entity.getStartDevice(), entity.getEndDevice()
    );
  }

  /**
   * Maps a RouteSegmentDTO to a RouteSegment entity.
   *
   * @param dto the RouteSegmentDTO to be mapped
   * @return the corresponding RouteSegment entity
   */
  RouteSegment map(RouteSegmentDTO dto) {

    if (dto == null) {
      return null;
    }

    RouteSegment entity = new RouteSegment();
    entity.setId(dto.id());
    entity.setOrder(dto.order());
    entity.setStartDevice(dto.startDevice());
    entity.setEndDevice(dto.endDevice());

    return entity;
  }

  /**
   * Updates an existing RouteSegment entity with data from a RouteSegmentDTO.
   *
   * @param dto    the RouteSegmentDTO containing updated data
   * @param entity the RouteSegment entity to be updated
   */
  public void updateEntity(RouteSegmentDTO dto, RouteSegment entity) {

    if (dto == null || entity == null) {
      return;
    }

    entity.setOrder(dto.order());
    entity.setStartDevice(dto.startDevice());
    entity.setEndDevice(dto.endDevice());
  }
}
