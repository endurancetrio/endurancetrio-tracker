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

import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import org.springframework.stereotype.Component;

/**
 * {@link TrackerAccountMapper} is a utility class for converting between {@link TrackerAccountDTO}
 * and {@link TrackerAccount} objects.
 */
@Component
public class TrackerAccountMapper {

  /**
   * Converts a {@link TrackerAccountDTO} to a {@link TrackerAccount} entity.
   *
   * @param dto the {@link TrackerAccountDTO} to be mapped
   * @return the corresponding {@link TrackerAccount} entity
   */
  TrackerAccount map(TrackerAccountDTO dto) {

    if (dto == null) {
      return null;
    }

    TrackerAccount entity = new TrackerAccount();
    entity.setOwner(dto.getOwner());
    entity.setKey(dto.getKey());
    entity.setEnabled(dto.isEnabled());

    return entity;
  }

  /**
   * Converts a {@link TrackerAccount} entity to a {@link TrackerAccountDTO}.
   *
   * @param entity the {@link TrackerAccount} to be mapped
   * @return the corresponding {@link TrackerAccountDTO}
   */
  TrackerAccountDTO map(TrackerAccount entity) {

    if (entity == null) {
      return null;
    }

    TrackerAccountDTO dto = new TrackerAccountDTO();
    dto.setOwner(entity.getOwner());
    dto.setKey(entity.getKey());
    dto.setEnabled(entity.isEnabled());

    return dto;
  }
}
