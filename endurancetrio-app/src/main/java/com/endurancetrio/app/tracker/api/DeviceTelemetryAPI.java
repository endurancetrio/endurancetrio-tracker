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

package com.endurancetrio.app.tracker.api;

import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import com.endurancetrio.business.tracker.dto.DeviceTelemetryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "Device",
    description = """
        Manages tracking device telemetry data including GPS coordinates, timestamps, and
        operational status for monitoring IoT devices
        """
)
public interface DeviceTelemetryAPI {

  /**
   * Saves the provided telemetry data, using the authenticated user as the owner account
   *
   * @param deviceTelemetryDTO the device telemetry data to be saved
   * @return the saved {@link DeviceTelemetryDTO} wrapped in an {@link EnduranceTrioResponse}
   */
  @Operation(
      summary = "Save telemetry data",
      description = "Saves the device telemetry data, using the authenticated user as the owner account",
      security = {
          @SecurityRequirement(name = "Account Name"),
          @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "201", description = "Telemetry data successfully saved",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Telemetry data saved successfully",
                  value = """
                          {
                            "code": 201,
                            "status": "Created",
                            "details": "Request handled successfully",
                            "data": {
                              "device": "SDABC",
                              "time": "2026-09-19T06:00:00Z",
                              "lat": 39.510058,
                              "lon": -9.136079,
                              "active": true
                            }
                          }
                          """
              )
          }
      ),
      headers = {
          @Header(
              name = "Device telemetry",
              description = "URI to list the newly created device telemetry",
              schema = @Schema(
                  type = "string",
                  example = "/tracker/v1/devices/{device}/telemetry"
              )
          )
      }
  )
  @ApiResponse(
      responseCode = "400", description = "Bad request - invalid telemetry data",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Validation Error",
                  summary = "Example of data validation failure",
                  value = """
                        {
                          "code": 400,
                          "status": "Bad Request",
                          "details": "The request was made with invalid or incomplete data"
                        }
                        """
              )
          }
      )
  )
  @ApiResponse(
      responseCode = "401", description = "Unauthorized - invalid or missing authentication",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Auth Error",
                  summary = "Example of authentication failure",
                  value = """
                        {
                          "code": 401,
                          "status": "Unauthorized",
                          "details": "Authentication failed"
                        }
                        """
              )
          }
      )
  )
  @ApiResponse(
      responseCode = "404", description = "Not found - authenticated user not found",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Not found Error",
                  summary = "Example of a resource not found failure",
                  value = """
                        {
                          "code": 404,
                          "status": "Not Found",
                          "details": "The requested resource was not found"
                        }
                        """
              )
          }
      )
  )
  @RequestBody(
      description = "Telemetry data to be saved", required = true,
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = DeviceTelemetryDTO.class),
          examples = {
              @ExampleObject(
                  name = "Telemetry Data Example",
                  summary = "A typical telemetry data payload",
                  value = """
                        {
                          "device": "SDABC",
                          "time": "2026-09-19T06:00:00Z",
                          "lat": 39.510058,
                          "lon": -9.136079,
                          "active": true
                        }
                        """
              )
          }
      )
  )
  ResponseEntity<@NonNull EnduranceTrioResponse<DeviceTelemetryDTO>> save(
      @Parameter(
          description = "Tracking data transfer object containing device location information",
          required = true
      ) DeviceTelemetryDTO deviceTelemetryDTO
  );

  /**
   * Gets the most recent telemetry data record for each device present in the database.
   *
   * @return list of telemetry data records containing the latest record for each device
   */
  @Operation(
      summary = "Gets most recent telemetry data per device",
      description = "Gets the most recent telemetry data record for each device present in the database",
      security = {
          @SecurityRequirement(name = "Account Name"),
          @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "200", description = "Telemetry data successfully obtained",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Telemetry data successfully obtained",
                  value = """
                          {
                            "code": 200,
                            "status": "OK",
                            "details": "Request handled successfully",
                            "data": [
                              {
                                "device": "SDABC",
                                "time": "2026-09-19T06:00:00Z",
                                "lat": 39.510058,
                                "lon": -9.136079,
                                "active": true
                              },
                              {
                                "device": "SDDEF",
                                "time": "2026-09-19T06:00:06Z",
                                "lat":  39.509001,
                                "lon": -9.139602,
                                "active": true
                              },
                              {
                                "device": "SDFGH",
                                "time": "2026-09-19T06:00:12Z",
                                "lat": 39.509773,
                                "lon": -9.140004,
                                "active": true
                              },
                              {
                                "device": "SDJKL",
                                "time": "2026-09-19T06:00:24Z",
                                "lat": 39.511075,
                                "lon":  -9.136516,
                                "active": true
                              }
                            ]
                          }
                          """
              ),
              @ExampleObject(
                  name = "Success Response with empty list",
                  summary = "Empty list",
                  value = """
                          {
                            "code": 200,
                            "status": "OK",
                            "details": "Request handled successfully",
                            "data": [
                            ]
                          }
                          """
              )
          }
      )
  )
  @ApiResponse(
      responseCode = "401", description = "Unauthorized - invalid or missing authentication",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Auth Error",
                  summary = "Example of authentication failure",
                  value = """
                        {
                          "code": 401,
                          "status": "Unauthorized",
                          "details": "Authentication failed"
                        }
                        """
              )
          }
      )
  )
  @ApiResponse(
      responseCode = "500", description = "Internal Server Error",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Internal Server Error",
                  summary = "Example of an internal server failure",
                  value = """
                        {
                          "code": 500,
                          "status": "Internal Server Error",
                          "details": "An internal server error occurred"
                        }
                        """
              )
          }
      )
  )
  ResponseEntity<@NonNull EnduranceTrioResponse<List<DeviceTelemetryDTO>>> getMostRecentRecordForEachDevice();
}
