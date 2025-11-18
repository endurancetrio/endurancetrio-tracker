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

package com.endurancetrio.business.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The {@link TrackerAccountDTO} represents a tracker account with its owner, key, and enabled
 * status.
 */
public class TrackerAccountDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank(message = "Owner is required")
  @Size(min = 1, max = 50, message = "Owner must be between 1 and 50 characters")
  private String owner;

  @NotBlank(message = "Key is required")
  @Size(min = 32, max = 64, message = "Key must be between 32 and 64 characters")
  @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Key must contain only alphanumeric characters")
  private String key;

  private boolean enabled;

  public TrackerAccountDTO() {
    super();
  }

  public TrackerAccountDTO(String owner, String key, boolean enabled) {
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
    TrackerAccountDTO that = (TrackerAccountDTO) o;
    return Objects.equals(owner, that.owner) && Objects.equals(key, that.key) && Objects.equals(
        enabled, that.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, enabled);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TrackingDataDTO.class.getSimpleName() + "[", "]")
        .add("owner='" + owner + "'")
        .add("key='" + key + "'")
        .add("enabled=" + enabled)
        .toString();
  }
}
