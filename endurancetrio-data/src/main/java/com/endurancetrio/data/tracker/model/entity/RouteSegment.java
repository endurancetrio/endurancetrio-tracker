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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.StringJoiner;

/**
 * The {@link RouteSegment} represents a route segment in the EnduranceTrio Tracker system.
 * <p>
 * The {@link RouteSegment} fields are defined as follows:
 * <ul>
 *   <li>
 *     {@link #getId()} id : The unique identifier of the {@link RouteSegment} that is automatically
 *     generated and is the primary key.
 *  </li>
 *   <li>
 *     {@link #getOrder()} order : The order of the segment within the route
 *   </li>
 *   <li>
 *     {@link #getStartDevice()} startDevice : The starting device identifier of the segment
 *   </li>
 *  <li>
 *    {@link #getEndDevice()} endDevice : The ending device identifier of the segment
 *  </li>
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
@Table(name = "route_segment")
@SequenceGenerator(
    name = "seq_endurancetrio_generator", sequenceName = "seq_route_segment_id", allocationSize = 5
)
public class RouteSegment extends BaseEntity<Long> {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(name = "segment_order", nullable = false)
  Integer order;

  @Column(name = "start_device", nullable = false)
  String startDevice;

  @Column(name = "end_device", nullable = false)
  String endDevice;

  public RouteSegment() {
    super();
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getStartDevice() {
    return startDevice;
  }

  public void setStartDevice(String startDevice) {
    this.startDevice = startDevice;
  }

  public String getEndDevice() {
    return endDevice;
  }

  public void setEndDevice(String endDevice) {
    this.endDevice = endDevice;
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
    return new StringJoiner(", ", RouteSegment.class.getSimpleName() + "[", "]")
        .add("id=" + this.getId())
        .add("order=" + order)
        .add("startDevice='" + startDevice + "'")
        .add("endDevice='" + endDevice + "'")
        .toString();
  }
}
