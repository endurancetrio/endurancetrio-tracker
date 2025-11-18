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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;
import org.hibernate.annotations.CreationTimestamp;

/**
 * The {@link TrackingData} represents a geographic tracking point recorded by a device at a
 * specific time.
 * <p>
 * The {@link TrackingData} fields are defined as follows:
 * <ul>
 *   <li>
 *     {@link #getId()} id : The unique identifier of the {@link TrackingData} that is automatically
 *     generated and is the primary key.
 *  </li>
 *  <li>
 *    {@link #getAccount()} account : The account that recorded this tracking point
 *  </li>
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
 *    {@link #getCreatedAt()} createdAt : The timestamp when this record was created in the system
 *  </li>
 * </ul>
 */
@Entity
@Table(name = "tracking_data")
public class TrackingData {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tracking_data")
  @SequenceGenerator(
      name = "seq_tracking_data", sequenceName = "seq_tracking_data_id", allocationSize = 5
  )
  private Long id;

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

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private Instant createdAt;

  public TrackingData() {
    super();
  }

  public TrackingData(
      TrackerAccount account, String device, Instant time, Double latitude, Double longitude) {
    this.account = account;
    this.device = device;
    this.time = time;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrackingData that = (TrackingData) o;
    return Objects.equals(id, that.id) && Objects.equals(device, that.device) && Objects.equals(
        time, that.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, device, time);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TrackingData.class.getSimpleName() + "[", "]").add("id=" + id)
        .add("account='" + account.getOwner() + "'")
        .add("device='" + device + "'")
        .add("time=" + time)
        .add("latitude=" + latitude)
        .add("longitude=" + longitude)
        .add("createdAt=" + createdAt)
        .toString();
  }
}
