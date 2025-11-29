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

import com.endurancetrio.data.tracker.model.entity.TrackerAccount;
import com.endurancetrio.data.tracker.repository.TrackerAccountRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for initializing the first tracker account upon application startup.
 * <p>
 * This service checks for the presence of environment variables {@code FIRST_OWNER} and
 * {@code FIRST_HASH} during application startup. If both variables are provided and valid, it
 * creates the initial tracker account in the database. If an account with the provided owner name
 * already exists in the database, its key hash will be overridden with the provided key hash.
 */
@Service
public class TrackerAccountInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(TrackerAccountInitializer.class);

  @Value("${app.initialization.first-account-owner}")
  private String firstOwner;

  @Value("${app.initialization.first-account-key-hash}")
  private String firstHash;

  private final TrackerAccountRepository trackerAccountRepository;

  @Autowired
  public TrackerAccountInitializer(TrackerAccountRepository trackerAccountRepository) {
    this.trackerAccountRepository = trackerAccountRepository;
  }

  /**
   * Initializes the first tracker account upon application startup if configuration is provided.
   * <p>
   * This method is automatically triggered when the application is fully started and ready. It
   * performs the following steps:
   * <p>
   * <ol>
   *   <li>Validates that both owner and hash configuration are present</li>
   *   <li>Checks if an account with the same owner already exists</li>
   *   <li>Updates the key hash if an account with the same owner already exists</li>
   *   <li>Creates and persists the new tracker account if none exists with the provided owner</li>
   * </ol>
   */
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void accountInitializer() {

    if (!isFirstOwnerConfigured()) {
      LOG.warn("Account initialization skipped: Missing configuration for first account owner.");
      return;
    }

    if (!isFirstHashConfigured()) {
      LOG.warn("Account initialization skipped: Missing configuration for first key hash.");
      return;
    }

    Optional<TrackerAccount> accountOptional = trackerAccountRepository.findByOwner(firstOwner);

    TrackerAccount firstAccount;
    if (accountOptional.isPresent()) {
      LOG.info("Account initialization will override existing key hash.");
      firstAccount = accountOptional.get();
      firstAccount.setKey(firstHash);
    } else {
      firstAccount = new TrackerAccount(firstOwner, firstHash, true);
    }

    try {
      trackerAccountRepository.save(firstAccount);
    } catch (Exception exception) {
      LOG.error("Account initialization failed:", exception);
    }

    LOG.info("Account initialization handled successfully");
  }

  private boolean isFirstOwnerConfigured() {
    return firstOwner != null && !firstOwner.trim().isEmpty();
  }

  private boolean isFirstHashConfigured() {
    return firstHash != null && !firstHash.trim().isEmpty();
  }
}
