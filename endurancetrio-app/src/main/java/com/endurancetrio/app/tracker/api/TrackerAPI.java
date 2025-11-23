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
import com.endurancetrio.business.tracker.dto.TrackingDataDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Tracker", description = "EnduranceTrio Tracker API for managing tracking data")
public interface TrackerAPI {

  /**
   * Saves the provided tracking data, using the authenticated user as the owner account
   *
   * @param trackingDataDTO the tracking data to be saved
   * @return the saved {@link TrackingDataDTO} wrapped in an {@link EnduranceTrioResponse}
   */
  @Operation(
      summary = "Save tracking data",
      description = "Saves tracking data for a device, using the authenticated user as the owner account",
      security = {
          @SecurityRequirement(name = "Account Name"),
          @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "201", description = "Tracking data successfully saved",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Tracking data saved successfully",
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
              name = "Device locations",
              description = "URI to list the newly created device location",
              schema = @Schema(
                  type = "string",
                  example = "/tracker/v1/devices/{device}/locations"
              )
          )
      }
  )
  @ApiResponse(
      responseCode = "400", description = "Bad request - invalid tracking data",
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
      description = "Tracking data to be saved", required = true,
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = TrackingDataDTO.class),
          examples = {
              @ExampleObject(
                  name = "Tracking Data Example",
                  summary = "A typical tracking data payload",
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
  ResponseEntity<EnduranceTrioResponse<TrackingDataDTO>> save(
      @Parameter(
          description = "Tracking data transfer object containing device location information",
          required = true
      ) TrackingDataDTO trackingDataDTO
  );
}
