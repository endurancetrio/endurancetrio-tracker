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

package com.endurancetrio.business.tracker.validator;

import com.endurancetrio.business.tracker.annotation.RouteSegmentsValid;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteSegmentDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Comparator;
import java.util.List;

/**
 * Validator for the {@link RouteSegmentsValid} constraint on {@link RouteDTO}.
 * <p>
 * This validator enforces two primary business rules for route integrity:
 * <ol>
 * <li><b>Continuity:</b> The {@code endDevice} of segment N must match the {@code startDevice} of segment N+1.</li>
 * <li><b>Sequence:</b> Segment orders must be strictly sequential (1, 2, 3...) without gaps.</li>
 * </ol>
 * </p>
 */
public class RouteSegmentsValidator implements ConstraintValidator<RouteSegmentsValid, RouteDTO> {

  /**
   * Validator for the {@link RouteSegmentsValid} constraint.
   * <p>
   * Performs a comprehensive audit of the {@link RouteDTO} to ensure logical integrity. This
   * validator does not "fail-fast"; it evaluates the entire segment list to report all instances of
   * the following violations:
   * </p>
   * <ul>
   *   <li>
   *     <b>Order Initiation:</b> The first segment (lowest order) must be 1.
   *   </li>
   *   <li>
   *     <b>Device Continuity:</b> The {@code endDevice} of a segment must match
   *   the {@code startDevice} of the next segment in the sequence.</li>
   *   <li>
   *     <b>Numerical Sequence:</b> Segment orders must be strictly incremental (e.g., 1, 2, 3)
   *     without gaps.
   *   </li>
   * </ul>
   */
  @Override
  public boolean isValid(RouteDTO route, ConstraintValidatorContext context) {

    boolean isValid = true;

    if (route.segments() == null || route.segments().isEmpty()) {
      return true;
    }

    List<RouteSegmentDTO> sortedSegments = route.segments()
        .stream()
        .sorted(Comparator.comparing(RouteSegmentDTO::order))
        .toList();

    if (sortedSegments.getFirst().order() != 1) {
      buildViolation(context, "Route must start with a segment of order 1", isValid);

      isValid = false;
    }

    for (int i = 0; i < sortedSegments.size() - 1; i++) {

      RouteSegmentDTO currentSegment = sortedSegments.get(i);
      RouteSegmentDTO nextSegment = sortedSegments.get(i + 1);

      if (!currentSegment.endDevice().equals(nextSegment.startDevice())) {
        buildViolation(context,
            String.format("Device mismatch: Segment %d ends at [%s] but Segment %d starts at [%s]",
                currentSegment.order(), currentSegment.endDevice(), nextSegment.order(),
                nextSegment.startDevice()
            ), isValid
        );

        isValid = false;
      }

      if (nextSegment.order() != currentSegment.order() + 1) {
        buildViolation(context,
            String.format("Sequence error: Expected segment order %d but found %d",
                currentSegment.order() + 1, nextSegment.order()
            ), isValid
        );

        isValid = false;
      }
    }

    return isValid;
  }

  /**
   * Builds a custom violation message for the constraint validation context.
   *
   * @param context The validation context.
   * @param message The violation message to be added.
   */
  private void buildViolation(ConstraintValidatorContext context, String message, boolean isValid) {

    if (isValid) {
      context.disableDefaultConstraintViolation();
    }

    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode("segments")
        .addConstraintViolation();
  }
}
