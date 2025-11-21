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

package com.endurancetrio.app.common.response;

import static com.endurancetrio.app.common.constants.ControllerConstants.DETAILS_SUCCESS;
import static com.endurancetrio.app.common.constants.ControllerConstants.MSG_200;
import static com.endurancetrio.app.common.constants.ControllerConstants.STATUS_200;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.endurancetrio.app.common.constants.ControllerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link EnduranceTrioResponse} entity.
 * <p>
 * This test may seem redundant since it only verify getters and constructor, but its purpose is to
 * establish a testing culture from the very beginning of the project. It serves as a reminder that
 * every part of the application should be testable and that tests should always be present.
 */
class EnduranceTrioResponseTest {

  private static final int TEST_STATUS = STATUS_200;
  private static final String TEST_MESSAGE = MSG_200;
  private static final String TEST_DETAILS = DETAILS_SUCCESS;
  private static final String MSG_DATA = "Success Data";

  private EnduranceTrioResponse<String> underTest;

  @BeforeEach
  void setUp() {
    underTest = new EnduranceTrioResponse<>(TEST_STATUS, TEST_MESSAGE, TEST_DETAILS, MSG_DATA);
  }

  @Test
  void entityShouldRetainValues() {

    assertEquals(TEST_STATUS, underTest.status());
    assertEquals(TEST_MESSAGE, underTest.message());
    assertEquals(TEST_DETAILS, underTest.details());
    assertEquals(MSG_DATA, underTest.data());
  }
}
