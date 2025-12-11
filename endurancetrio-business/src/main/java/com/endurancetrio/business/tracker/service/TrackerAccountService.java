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

import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;

public interface TrackerAccountService {

  /**
   * Retrieves the tracker account details for the specified owner.
   *
   * @param owner the owner of the tracker account
   * @return the {@link TrackerAccountDTO} containing the account details
   */
  TrackerAccountDTO getByOwner(String owner);

  /**
   * Validates the provided key for the given owner.
   *
   * @param owner the owner of the key
   * @param key   the key to validate
   * @return true if the provided key is valid and the account is enabled, false otherwise
   */
  boolean validateKey(String owner, String key);
}
