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

package com.endurancetrio.data.tracker.repository;

import com.endurancetrio.data.tracker.model.entity.DeviceTelemetry;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTelemetryRepository extends
    JpaRepository<@NonNull DeviceTelemetry, @NonNull Long> {

  /**
   * Finds existing devices from the provided set of device identifiers.
   *
   * @param devices set of device identifiers to check for existence
   * @return set of device identifiers that exist in the database
   */
  @Query("SELECT DISTINCT d.device FROM DeviceTelemetry d WHERE d.device IN :devices")
  Set<String> findExistingDevicesFrom(@Param("devices") Set<String> devices);

  /**
   * Finds the most recent active telemetry record for each specified device.
   * <p>
   * This method uses PostgreSQL's {@code DISTINCT ON} feature to efficiently retrieve exactly one
   * record per device - specifically, the record with the latest {@code created_at} timestamp for
   * each device where {@code active = true}.
   * <p>
   * Implementation Details: The query uses {@code ORDER BY device, created_at DESC} to ensure
   * deterministic results. PostgreSQL requires that {@code DISTINCT ON} columns (in this case,
   * {@code device}) appear as the leftmost columns in the {@code ORDER BY} clause. This groups all
   * records by device and sorts each group by {@code created_at} in descending order, guaranteeing
   * that the first record selected for each device is the most recent one.</p>
   * <p>
   * Example: Given devices [A, B] with the following records:
   * <pre>
   * Device | created_at       | active | value
   * -------|------------------|--------|-------
   * A      | 2024-01-05 10:00 | true   | 100
   * A      | 2024-01-04 09:00 | true   | 150
   * B      | 2024-01-05 10:00 | true   | 200  ← Same timestamp as A
   * B      | 2024-01-03 08:00 | true   | 250
   * </pre>
   * Returns: [A(2024-01-05 10:00), B(2024-01-05 10:00)]</p>
   *
   * @param devices the list of device identifiers to query (must not be null)
   * @return a non-null list containing the most recent active telemetry record for each device in
   * the input list, ordered by device name ascending. If a device has no active records or is not
   * in the database, it will not appear in the results. Returns an empty list if no active records
   * exist for any of the specified devices.
   * @throws IllegalArgumentException if {@code devices} is null
   */
  @Query(
      value = """
          SELECT DISTINCT ON (device) * FROM {h-schema}device_telemetry
                  WHERE active = true AND device IN :devices ORDER BY device, created_at DESC
          """, nativeQuery = true
  )
  List<DeviceTelemetry> findMostRecentByDevices(@NonNull @Param("devices") List<String> devices);

  /**
   * Finds the most recent telemetry data record for each device present in the database.
   * <p>
   * Uses a correlated subquery to identify records with the maximum timestamp per device. The main
   * advantage of this approach is JPA provider compatibility, though performance may degrade with
   * larger datasets. For databases supporting DISTINCT ON, consider migrating to a native query
   * approach when performance becomes a concern.
   * <p>
   * PostgreSQL native query alternative:
   *   {@code
   *     @Query(value = """
   *       SELECT DISTINCT ON (device) * FROM {h-schema}device_telemetry WHERE active = true
   *       ORDER BY device, time DESC", nativeQuery = true
   *     )
   *   }
   *
   * @return non-null list of telemetry data records containing the latest record for each device.
   *         Returns an empty list if no active records exist for any device.
   */
  @Query(
      value = """
          SELECT data FROM DeviceTelemetry data WHERE data.active = true AND data.time = (
            SELECT MAX(maxTimeData.time)
            FROM DeviceTelemetry maxTimeData WHERE maxTimeData.active = true AND maxTimeData.device = data.device
          ) ORDER BY data.device
          """
  )
  List<DeviceTelemetry> findMostRecentRecordForEachDevice();
}
