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

import com.endurancetrio.business.common.exception.NotFoundException;
import com.endurancetrio.business.common.exception.base.EnduranceTrioError;
import com.endurancetrio.business.tracker.dto.TrackerAccountDTO;
import com.endurancetrio.business.tracker.mapper.TrackerAccountMapper;
import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackerAccountServiceMain implements TrackerAccountService {

  private static final Logger LOG = LoggerFactory.getLogger(TrackerAccountServiceMain.class);

  private final TrackerAccountRepository repository;

  private final TrackerAccountMapper trackerAccountMapper;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public TrackerAccountServiceMain(
      TrackerAccountRepository repository, TrackerAccountMapper trackerAccountMapper,
      PasswordEncoder passwordEncoder
  ) {
    this.repository = repository;
    this.trackerAccountMapper = trackerAccountMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean validateKey(String owner, String key) {

    Optional<TrackerAccount> accountOptional = findByOwner(owner);

    if (accountOptional.isEmpty()) {
      LOG.warn("Authentication failed: No account found for owner '{}'", owner);
      return false;
    }

    TrackerAccount account = accountOptional.get();

    if (!account.isEnabled()) {
      LOG.warn("Authentication failed: Account '{}' is disabled", owner);
      return false;
    }

    boolean isValidKey = passwordEncoder.matches(key, account.getKey());

    if (!isValidKey) {
      LOG.warn("Authentication failed: Invalid key for owner '{}'", owner);
    }

    return isValidKey;
  }

  @Override
  @Transactional(readOnly = true)
  public TrackerAccountDTO getByOwner(String owner) {

    Optional<TrackerAccount> accountOptional = findByOwner(owner);

    if (accountOptional.isEmpty()) {
      LOG.warn("No account found for owner '{}'", owner);
      throw new NotFoundException(EnduranceTrioError.NOT_FOUND);
    }

    return trackerAccountMapper.map(accountOptional.get());
  }

  private Optional<TrackerAccount> findByOwner(String owner) {
    return repository.findByOwner(owner);
  }
}
