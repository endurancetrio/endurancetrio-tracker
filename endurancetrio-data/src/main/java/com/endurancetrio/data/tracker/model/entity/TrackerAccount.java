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

package com.endurancetrio.data.tracker.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The {@link TrackerAccount} represents an account authorized to send tracking data to the
 * EnduranceTrio Tracker system.
 * <p>
 * The {@link TrackerAccount} fields are defined as follows:
 * <ul>
 *   <li>
 *     {@link #getOwner()} owner : The unique identifier of the account owner
 *   </li>
 *   <li>
 *     {@link #getKey()} key : The unique key associated with the account for authentication
 *   </li>
 *   <li>
 *     {@link #isEnabled()} enabled : Flag indicating whether the account is active
 *   </li>
 * </ul>
 */
@Entity
@Table(name = "tracker_account")
public class TrackerAccount implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "owner", nullable = false, unique = true, length = 50)
  private String owner;

  @Column(name = "account_key", nullable = false, unique = true, length = 100)
  private String key;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  public TrackerAccount() {
    super();
  }

  public TrackerAccount(String owner, String key, boolean enabled) {
    this.owner = owner;
    this.key = key;
    this.enabled = enabled;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrackerAccount that = (TrackerAccount) o;
    return Objects.equals(owner, that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TrackingData.class.getSimpleName() + "[", "]")
        .add("owner='" + owner + "'")
        .add("enabled=" + enabled)
        .toString();
  }
}
