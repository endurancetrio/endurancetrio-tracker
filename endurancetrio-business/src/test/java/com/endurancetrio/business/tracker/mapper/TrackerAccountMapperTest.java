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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackerAccountMapperTest {

  private static final String OWNER = "system";
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";
  private static final boolean IS_ENABLED = true;

  private TrackerAccount entityTest;
  private TrackerAccountDTO dtoTest;

  private TrackerAccountMapper underTest;

  @BeforeEach
  void setUp() {

    underTest = new TrackerAccountMapper();

    entityTest = new TrackerAccount();
    entityTest.setOwner(OWNER);
    entityTest.setKey(KEY);
    entityTest.setEnabled(true);

    dtoTest = new TrackerAccountDTO(OWNER, KEY, IS_ENABLED);
  }

  @Test
  void mapEntity() {

    TrackerAccountDTO result = underTest.map(entityTest);

    assertNotNull(result);
    assertEquals(OWNER, result.owner());
    assertEquals(KEY, result.key());
    assertTrue(result.enabled());
  }

  @Test
  void mapDTO() {

    TrackerAccount result = underTest.map(dtoTest);

    assertNotNull(result);
    assertEquals(OWNER, result.getOwner());
    assertEquals(KEY, result.getKey());
    assertTrue(result.isEnabled());
  }

  @Test
  void mapNullEntity() {

    TrackerAccountDTO result = underTest.map((TrackerAccount) null);

    assertNull(result);
  }

  @Test
  void mapNullDTO() {

    TrackerAccount result = underTest.map((TrackerAccountDTO) null);

    assertNull(result);
  }
}