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
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceTelemetryRepository extends JpaRepository<@NonNull DeviceTelemetry, @NonNull Long> {

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
   *       SELECT DISTINCT ON (device) * FROM telemetry_data WHERE active = true
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
