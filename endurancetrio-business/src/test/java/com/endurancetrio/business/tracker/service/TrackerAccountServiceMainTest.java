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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.business.tracker.mapper.TrackerAccountMapper;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TrackerAccountServiceMainTest {

  private static final String OWNER = "system";
  private static final String KEY = "TEST_ACCOUNT_KEY_1234567890";
  private static final boolean IS_ENABLED = true;

  private String inputOwner;
  private String inputKey;
  private TrackerAccount mockTrackerAccount;
  private TrackerAccountDTO mockTrackerAccountDTO;

  @Mock
  private TrackerAccountRepository trackerAccountRepository;

  @Mock
  private TrackerAccountMapper trackerAccountMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private TrackerAccountServiceMain underTest;

  @BeforeEach
  void setUp() {
    inputOwner = OWNER;
    inputKey = KEY;
    mockTrackerAccount = new TrackerAccount(OWNER, KEY, IS_ENABLED);
    mockTrackerAccountDTO = new TrackerAccountDTO(OWNER, KEY, IS_ENABLED);
  }

  @Test
  void validateKey() {
    when(trackerAccountRepository.findByOwner(inputOwner)).thenReturn(
        Optional.of(mockTrackerAccount));
    when(passwordEncoder.matches(inputKey, KEY)).thenReturn(true);

    boolean result = underTest.validateKey(inputOwner, inputKey);

    verify(trackerAccountRepository, times(1)).findByOwner(inputOwner);
    verify(passwordEncoder, times(1)).matches(inputKey, KEY);

    assertTrue(result);
  }

  @Test
  void validateKeyWithUnknownOwner() {

    String unknownOwner = "doe";

    when(trackerAccountRepository.findByOwner(unknownOwner)).thenReturn(Optional.empty());

    boolean result = underTest.validateKey(unknownOwner, inputKey);

    verify(trackerAccountRepository, times(1)).findByOwner(unknownOwner);
    verify(passwordEncoder, never()).matches(inputKey, KEY);

    assertFalse(result);
  }

  @Test
  void validateKeyWithAccountDisabled() {

    TrackerAccount disabledAccount = new TrackerAccount(OWNER, KEY, false);

    when(trackerAccountRepository.findByOwner(OWNER)).thenReturn(Optional.of(disabledAccount));

    boolean result = underTest.validateKey(inputOwner, inputKey);

    verify(trackerAccountRepository, times(1)).findByOwner(inputOwner);
    verify(passwordEncoder, never()).matches(inputKey, KEY);

    assertFalse(result);
  }

  @Test
  void validateKeyWithInvalidKey() {

    when(trackerAccountRepository.findByOwner(inputOwner)).thenReturn(
        Optional.of(mockTrackerAccount));
    when(passwordEncoder.matches(inputKey, KEY)).thenReturn(false);

    boolean result = underTest.validateKey(inputOwner, inputKey);

    verify(trackerAccountRepository, times(1)).findByOwner(inputOwner);
    verify(passwordEncoder, times(1)).matches(inputKey, KEY);

    assertFalse(result);
  }

  @Test
  void getByOwner() {

    when(trackerAccountRepository.findByOwner(inputOwner)).thenReturn(
        Optional.of(mockTrackerAccount));
    when(trackerAccountMapper.map(mockTrackerAccount)).thenReturn(mockTrackerAccountDTO);

    TrackerAccountDTO result = underTest.getByOwner(inputOwner);

    verify(trackerAccountRepository, times(1)).findByOwner(inputOwner);
    verify(trackerAccountMapper, times(1)).map(mockTrackerAccount);

    assertNotNull(result);
    assertEquals(OWNER, result.owner());
    assertEquals(IS_ENABLED, result.enabled());
  }

  @Test
  void getByOwnerWithNonExistingOwner() {

    String unknownOwner = "doe";

    when(trackerAccountRepository.findByOwner(unknownOwner)).thenReturn(Optional.empty());

    NotFoundException result = assertThrows(NotFoundException.class,
        () -> underTest.getByOwner(unknownOwner)
    );

    verify(trackerAccountRepository, times(1)).findByOwner(unknownOwner);
    verify(trackerAccountMapper, never()).map(mockTrackerAccount);

    assertEquals(EnduranceTrioError.NOT_FOUND.getCode(), result.getCode());
    assertEquals(EnduranceTrioError.NOT_FOUND.getMessage(), result.getMessage());
  }
}
