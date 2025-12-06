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
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.Instant;
import java.util.StringJoiner;

/**
 * The {@link DeviceTelemetry} represents the telemetry data recorded by a device at a
 * specific time.
 * <p>
 * The {@link DeviceTelemetry} fields are defined as follows:
 * <ul>
 *   <li>
 *     {@link #getId()} id : The unique identifier of the {@link DeviceTelemetry} that is
 *     automatically generated and is the primary key.
 *  </li>
 *  <li>
 *    {@link #getAccount()} account : The account that recorded this telemetry data
 *  </li>
 *  <li>
 *    {@link #getDevice()} device : Identifier of the device that recorded this telemetry data
 *  </li>
 *  <li>
 *    {@link #getTime()} time : The timestamp when the telemetry data was recorded
 *  </li>
 *  <li>
 *    {@link #getLatitude()} latitude : The latitude coordinate within the telemetry data
 *  </li>
 *  <li>
 *    {@link #getLongitude()} longitude : The longitude coordinate within the telemetry data
 *  </li>
 *  <li>
 *    {@link #isActive()} active : Flag indicating whether the device is active
 *  </li>
 *  <li>
 *    {@link #getVersion()} version : The version number for optimistic locking,
 *    inherited from {@link AuditableEntity}.
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
@Table(name = "device_telemetry")
@SequenceGenerator(
    name = "seq_endurancetrio_generator", sequenceName = "seq_device_telemetry_id", allocationSize = 5
)
public class DeviceTelemetry extends BaseEntity<Long> {

  @Serial
  private static final long serialVersionUID = 1L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account", nullable = false)
  private TrackerAccount account;

  @Column(name = "device", nullable = false, length = 50)
  private String device;

  @Column(name = "record_time", nullable = false)
  private Instant time;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "active", nullable = false)
  private boolean active;

  public DeviceTelemetry() {
    super();
  }

  public DeviceTelemetry(
      TrackerAccount account, String device, Instant time, Double latitude, Double longitude,
      boolean active
  ) {
    this.account = account;
    this.device = device;
    this.time = time;
    this.latitude = latitude;
    this.longitude = longitude;
    this.active = active;
  }

  public TrackerAccount getAccount() {
    return account;
  }

  public void setAccount(TrackerAccount account) {
    this.account = account;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
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
    return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
        .add("id=" + this.getId())
        .add("account='" + (account != null ? account.getOwner() : null) + "'")
        .add("device='" + device + "'")
        .add("time=" + time)
        .add("latitude=" + latitude)
        .add("longitude=" + longitude)
        .add("active=" + active)
        .add("createdAt=" + this.getCreatedAt())
        .toString();
  }
}
