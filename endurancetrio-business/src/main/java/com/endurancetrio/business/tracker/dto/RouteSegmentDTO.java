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

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * The {@link RouteSegmentDTO} represents a route segment in the EnduranceTrio Tracker system.
 *
 * @param id          The unique identifier of the {@link RouteSegmentDTO}.
 * @param order       The order of the segment within the route.
 * @param startDevice The starting device identifier of the segment.
 * @param endDevice   The ending device identifier of the segment.
 */
public record RouteSegmentDTO(

    Long id,

    @NotNull(message = "Order is required and cannot be null")
    @Min(value = 1, message = "Order must be at least 1")
    Integer order,

    @NotBlank(message = "Start device identifier is required")
    String startDevice,

    @NotBlank(message = "End device identifier is required")
    String endDevice

) implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @AssertTrue(message = "Start and end devices must be different")
  public boolean areDevicesDifferent() {
    return !startDevice.equals(endDevice);
  }
}
