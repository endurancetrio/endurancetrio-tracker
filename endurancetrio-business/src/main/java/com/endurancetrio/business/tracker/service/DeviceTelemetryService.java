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

package com.endurancetrio.business.tracker.service;

import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import java.util.List;

public interface DeviceTelemetryService {

  /**
   * Saves the provided telemetry data for the specified owner.
   *
   * @param owner              the owner of the telemetry data
   * @param deviceTelemetryDTO the telemetry data to be saved
   * @return the saved {@link DeviceTelemetryDTO}
   */
  DeviceTelemetryDTO save(String owner, DeviceTelemetryDTO deviceTelemetryDTO);

  /**
   * Finds the most recent telemetry data record for each device present in the database.
   *
   * @return list of telemetry data records containing the latest record for each device
   */
  List<DeviceTelemetryDTO> findMostRecentRecordForEachDevice();
}
