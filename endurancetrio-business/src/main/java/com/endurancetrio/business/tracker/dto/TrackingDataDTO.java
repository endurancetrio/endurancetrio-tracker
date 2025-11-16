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

import com.endurancetrio.data.tracker.model.entity.TrackingData;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * The {@link TrackingDataDTO} represents a geographic tracking point recorded by a device
 * at a specific time.
 * <p>
 * The {@link TrackingData} fields are defined as follows:
 * <ul>
 *  <li>
 *    {@link #getDevice()} device : Identifier of the device that recorded this tracking point
 *  </li>
 *  <li>
 *    {@link #getTime()} time : The timestamp when the tracking point was recorded
 *  </li>
 *  <li>
 *    {@link #getLatitude()} latitude : The latitude coordinate of the tracking point
 *  </li>
 *  <li>
 *    {@link #getLongitude()} longitude : The longitude coordinate of the tracking point
 *  </li>
 *  <li>
 *    {@link #getOwner()} owner : The owner of the account that recorded this tracking point
 *  </li>
 * </ul>
 */
public class TrackingDataDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotNull(message = "Device identifier cannot be null")
  @Size(min = 1, max = 50, message = "Device identifier must be between 1 and 50 characters")
  @Column(name = "device", nullable = false, length = 50)
  private String device;

  @NotNull(message = "Timestamp cannot be null")
  @Column(name = "time", nullable = false)
  private Instant time;

  @NotNull(message = "Latitude cannot be null")
  @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
  @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
  @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
  private Double latitude;

  @NotNull(message = "Longitude cannot be null")
  @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
  @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
  @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
  private Double longitude;

  @NotNull(message = "Owner cannot be null")
  @Size(min = 1, max = 50, message = "Owner must be between 1 and 100 characters")
  @Column(name = "owner", nullable = false, length = 50)
  private String owner;

  public TrackingDataDTO() {
    super();
  }

  public TrackingDataDTO(
      String device, Instant time, Double latitude, Double longitude, String owner) {
    this.device = device;
    this.time = time;
    this.latitude = latitude;
    this.longitude = longitude;
    this.owner = owner;
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

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrackingDataDTO that = (TrackingDataDTO) o;
    return Objects.equals(device, that.device) && Objects.equals(time, that.time) && Objects.equals(
        latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(
        owner, that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(device, time, latitude, longitude, owner);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TrackingDataDTO.class.getSimpleName() + "[", "]")
        .add("device='" + device + "'")
        .add("time=" + time)
        .add("latitude=" + latitude)
        .add("longitude=" + longitude)
        .add("owner='" + owner + "'")
        .toString();
  }
}
