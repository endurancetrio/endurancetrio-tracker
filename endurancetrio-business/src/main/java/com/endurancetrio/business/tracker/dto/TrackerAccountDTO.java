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

/**
 * The {@link TrackerAccountDTO} represents a tracker account with its owner, key, and enabled
 * status.
 */
public record TrackerAccountDTO(

    @NotBlank(message = "Owner is required")
    @Size(min = 1, max = 50, message = "Owner must be between 1 and 50 characters")
    String owner,

    @NotBlank(message = "Key is required")
    @Size(min = 32, max = 64, message = "Key must be between 32 and 64 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Key must contain only alphanumeric characters")
    String key,

    boolean enabled

) implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
}
