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

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TrackerAccountInitializerTest {

  private static final String FIRST_OWNER = "system";

  private static final String FIRST_HASH = "TEST_ACCOUNT_KEY_1234567890";

  @Mock
  private TrackerAccountRepository trackerAccountRepository;

  @InjectMocks
  private TrackerAccountInitializer underTest;

  @Test
  void accountInitializer() {

    ReflectionTestUtils.setField(underTest, "firstOwner", FIRST_OWNER);
    ReflectionTestUtils.setField(underTest, "firstHash", FIRST_HASH);
    when(trackerAccountRepository.findByOwner(FIRST_OWNER)).thenReturn(Optional.empty());

    underTest.accountInitializer();

    verify(trackerAccountRepository, times(1)).findByOwner(FIRST_OWNER);
    verify(trackerAccountRepository, times(1)).save(argThat(
        account -> account.getOwner().equals(FIRST_OWNER) && account.getKey().equals(FIRST_HASH)
            && account.isEnabled()));
  }

  @Test
  void accountInitializerWithEmptyFirstOwner() {

    ReflectionTestUtils.setField(underTest, "firstOwner", "");
    ReflectionTestUtils.setField(underTest, "firstHash", FIRST_HASH);

    underTest.accountInitializer();

    verify(trackerAccountRepository, never()).findByOwner(FIRST_OWNER);
    verify(trackerAccountRepository, never()).save(argThat(
        account -> account.getOwner().equals(FIRST_OWNER) && account.getKey().equals(FIRST_HASH)
            && account.isEnabled()));
  }

  @Test
  void accountInitializerWithEmptyFirstHash() {

    ReflectionTestUtils.setField(underTest, "firstOwner", FIRST_OWNER);
    ReflectionTestUtils.setField(underTest, "firstHash", "");

    underTest.accountInitializer();

    verify(trackerAccountRepository, never()).findByOwner(FIRST_OWNER);
    verify(trackerAccountRepository, never()).save(argThat(
        account -> account.getOwner().equals(FIRST_OWNER) && account.getKey().equals(FIRST_HASH)
            && account.isEnabled()));
  }

  @Test
  void accountInitializerWithExistingAccount() {

    ReflectionTestUtils.setField(underTest, "firstOwner", FIRST_OWNER);
    ReflectionTestUtils.setField(underTest, "firstHash", FIRST_HASH);
    when(trackerAccountRepository.findByOwner(FIRST_OWNER)).thenReturn(
        Optional.of(new TrackerAccount(FIRST_OWNER, "to-be-overridden", true)));

    underTest.accountInitializer();

    verify(trackerAccountRepository, times(1)).findByOwner(FIRST_OWNER);
    verify(trackerAccountRepository, times(1)).save(argThat(
        account -> account.getOwner().equals(FIRST_OWNER) && account.getKey().equals(FIRST_HASH)
            && account.isEnabled()));
  }
}
