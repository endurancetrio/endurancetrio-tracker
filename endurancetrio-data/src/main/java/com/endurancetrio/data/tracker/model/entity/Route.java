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

import com.endurancetrio.data.common.model.entity.AuditableEntity;
import com.endurancetrio.data.common.model.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * The {@link Route} represents a route segment in the EnduranceTrio Tracker system.
 * <p>
 * The {@link Route} fields are defined as follows:
 * <ul>
 *   <li>
 *     {@link #getId()} id : The unique identifier of the {@link Route} that is automatically
 *     generated and is the primary key.
 *  </li>
 *   <li>
 *     {@link #getReference()} name : The name of the route
 *   </li>
 *   <li>
 *     {@link #getSegments()} segments : The list of segments that make up the route
 *   </li>
 *  <li>
 *    {@link #getCreatedAt()} createdAt : The system timestamp of creation,
 *    inherited from {@link AuditableEntity}.
 *  </li>
 *  <li>
 *    {@link #getUpdatedAt()} updatedAt : The system timestamp of the last update,
 *    inherited from {@link AuditableEntity}.
 *  </li>
 * </ul>
 */
@Entity
@Table(name = "route")
@SequenceGenerator(
    name = "seq_endurancetrio_generator", sequenceName = "seq_route_id", allocationSize = 5
)
public class Route extends BaseEntity<Long> {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(name = "reference", nullable = false)
  String reference;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "route_id", nullable = false)
  @OrderBy("segment_order ASC")
  private List<RouteSegment> segments;

  public Route() {
    super();
    this.segments = new ArrayList<>();
  }

  /**
   * Utility method to add a segment to the route.
   *
   * @param segment the segment to add
   */
  public void addSegment(RouteSegment segment) {
    this.segments.add(segment);
  }

  /**
   * Utility method to remove a segment from the route.
   *
   * @param segment the segment to remove
   */
  public void removeSegment(RouteSegment segment) {
    this.segments.remove(segment);
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public List<RouteSegment> getSegments() {
    return segments;
  }

  public void setSegments(
      List<RouteSegment> segments
  ) {
    this.segments = segments;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Route.class.getSimpleName() + "[", "]")
        .add("id=" + this.getId())
        .add("name='" + reference + "'")
        .add("segments=" + segments)
        .toString();
  }
}
