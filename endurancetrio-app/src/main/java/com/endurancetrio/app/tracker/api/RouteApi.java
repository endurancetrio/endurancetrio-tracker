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

import com.endurancetrio.app.common.annotation.OpenApiStandardErrors;
import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import com.endurancetrio.business.tracker.dto.RouteDTO;
import com.endurancetrio.business.tracker.dto.RouteMetricsDTO;
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
    name = "Route",
    description = """
        Provides operations for creating and managing route configurations. Routes are structured
        as sequences of segments, each connecting a start device to an end device
        """
)
public interface RouteApi {

  /**
   * Retrieves all route configurations.
   *
   * @return a {@link ResponseEntity} containing an {@link EnduranceTrioResponse} with a list of
   * {@link RouteDTO} representing all route configurations
   */
  @Operation(
      summary = "Retrieves all route configurations",
      description = "Gets all route configurations present in the database",
      security = {
          @SecurityRequirement(name = "Account Name"),
          @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "200",
      description = "List of all routes configuration present in the database successfully obtained",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "List of routes configuration successfully obtained",
                  value = """
                      {
                        "status": 200,
                        "message": "OK",
                        "details": "Request handled successfully",
                        "data": [
                          {
                            "id": 1,
                            "reference": "20260921ETU001-001S",
                            "segments": [
                              {
                                "id": 1,
                                "order": 1,
                                "startDevice": "SDABC",
                                "endDevice": "SDDEF"
                              },
                              {
                                "id": 2,
                                "order": 2,
                                "startDevice": "SDDEF",
                                "endDevice": "SDFGH"
                              },
                              {
                                "id": 3,
                                "order": 3,
                                "startDevice": "SDFGH",
                                "endDevice": "SDJKL"
                              }
                            ]
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
                        "status": 200,
                        "message": "OK",
                        "details": "Request handled successfully",
                        "data": [
                        ]
                      }
                      """
              )
          }
      )
  )
  @OpenApiStandardErrors
  ResponseEntity<@NonNull EnduranceTrioResponse<List<RouteDTO>>> findAll();

  /**
   * Saves the provided route configuration.
   *
   * @param routeDTO the route configuration to be saved
   * @return a {@link ResponseEntity} containing an {@link EnduranceTrioResponse} with the saved
   * {@link RouteDTO}
   */
  @Operation(
      summary = "Save route configuration",
      description = "Saves the provided route configuration.",
      security = {
          @SecurityRequirement(name = "Account Name"), @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "201",
      description = "Route configuration successfully saved",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Route configuration saved successfully",
                  value = """
                      {
                        "status": 201,
                        "message": "Created",
                        "details": "Request handled successfully",
                        "data": {
                          "id": 1,
                          "reference": "20260921ETU001-001S",
                          "segments": [
                            {
                              "id": 1,
                              "order": 1,
                              "startDevice": "SDABC",
                              "endDevice": "SDDEF"
                            },
                            {
                              "id": 2,
                              "order": 2,
                              "startDevice": "SDDEF",
                              "endDevice": "SDFGH"
                            },
                            {
                              "id": 3,
                              "order": 3,
                              "startDevice": "SDFGH",
                              "endDevice": "SDJKL"
                            }
                          ]
                        }
                      }
                      """
              )
          }
      ),
      headers = {
          @Header(
              name = "Route Configuration",
              description = "URI to list the newly created route configuration",
              schema = @Schema(
                  type = "string", example = "/tracker/v1/routes/{id}"
              )
          )
      }
  )
  @OpenApiStandardErrors
  @RequestBody(
      description = """
          Route configuration to be saved. The first route segments must start at order 1,
          the segments order must be sequential and the segment N end device must match
          the segment N+1 start device.
          """,
      required = true,
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = RouteDTO.class),
          examples = {
              @ExampleObject(
                  name = "Route Configuration Example",
                  summary = "A typical route configuration payload",
                  value = """
                      {
                        "reference": "20260921ETU001-001S",
                        "segments": [
                          {
                            "order": 1,
                            "startDevice": "SDABC",
                            "endDevice": "SDDEF"
                          },
                          {
                            "order": 2,
                            "startDevice": "SDDEF",
                            "endDevice": "SDFGH"
                          },
                          {
                            "order": 3,
                            "startDevice": "SDFGH",
                            "endDevice": "SDJKL"
                          }
                        ]
                      }
                      """
              )
          }
      )
  )
  ResponseEntity<@NonNull EnduranceTrioResponse<RouteDTO>> save(
      @Parameter(description = "Route configuration to be saved", required = true) RouteDTO routeDTO
  );

  /**
   * Finds a route configuration by its unique identifier.
   *
   * @param id the unique identifier of the route configuration
   * @return a {@link ResponseEntity} containing an {@link EnduranceTrioResponse} with the
   * corresponding {@link RouteDTO}
   */
  @Operation(
      summary = "Find route configuration by it s id",
      description = "Finds a route configuration by its unique identifier",
      security = {
          @SecurityRequirement(name = "Account Name"), @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "200",
      description = "Route configuration successfully retrieved",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Route configuration successfully retrieved",
                  value = """
                      {
                        "status": 200,
                        "message": "OK",
                        "details": "Request handled successfully",
                        "data": {
                          "id": 1,
                          "reference": "20260921ETU001-001S",
                          "segments": [
                            {
                              "id": 1,
                              "order": 1,
                              "startDevice": "SDABC",
                              "endDevice": "SDDEF"
                            },
                            {
                              "id": 2,
                              "order": 2,
                              "startDevice": "SDDEF",
                              "endDevice": "SDFGH"
                            },
                            {
                              "id": 3,
                              "order": 3,
                              "startDevice": "SDFGH",
                              "endDevice": "SDJKL"
                            }
                          ]
                        }
                      }
                      """
              )
          }
      )
  )
  @OpenApiStandardErrors
  ResponseEntity<EnduranceTrioResponse<RouteDTO>> findById(
      @Parameter(description = "The unique identifier of the route", example = "1") @NonNull Long id
  );

  /**
   * Retrieves the GeoJSON CollectionFeature definition for a specific route.
   *
   * @param id The unique identifier of the route.
   * @return a {@link ResponseEntity} containing an {@link EnduranceTrioResponse} with the
   * corresponding {@link RouteMetricsDTO}
   */
  @Operation(
      summary = "Retrieves the GeoJSON definition for a specific route",
      description = "Retrieves the GeoJSON CollectionFeature definition for a specific route",
      security = {
          @SecurityRequirement(name = "Account Name"), @SecurityRequirement(name = "API Key")
      }
  )
  @ApiResponse(
      responseCode = "200",
      description = "Route metrics successfully retrieved",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Success Response",
                  summary = "Route metrics successfully retrieved",
                  value = """
                      {
                        "status": 200,
                        "message": "OK",
                        "details": "Request handled successfully",
                        "data": {
                          "type": "FeatureCollection",
                          "features": [
                            {
                              "type": "Feature",
                              "geometry": {
                                "type": "Point",
                                "coordinates": [
                                  -9.136053,
                                  39.510093
                                ]
                              },
                              "properties": {
                                "order": 1
                              }
                            },
                            {
                              "type": "Feature",
                              "geometry": {
                                "type": "Point",
                                "coordinates": [
                                  -9.139602,
                                  39.509001
                                ]
                              },
                              "properties": {
                                "order": 2
                              }
                            },
                            {
                              "type": "Feature",
                              "geometry": {
                                "type": "Point",
                                "coordinates": [
                                  -9.140004,
                                  39.509773
                                ]
                              },
                              "properties": {
                                "order": 3
                              }
                            },
                            {
                              "type": "Feature",
                              "geometry": {
                                "type": "Point",
                                "coordinates": [
                                  -9.136516,
                                  39.511075
                                ]
                              },
                              "properties": {
                                "order": 4
                              }
                            },
                            {
                              "type": "Feature",
                              "geometry": {
                                "type": "LineString",
                                "coordinates": [
                                  [
                                    -9.136053,
                                    39.510093
                                  ],
                                  [
                                    -9.139602,
                                    39.509001
                                  ],
                                  [
                                    -9.140004,
                                    39.509773
                                  ],
                                  [
                                    -9.136516,
                                    39.511075
                                  ]
                                ]
                              },
                              "properties": {
                                "id": 1,
                                "reference": "20260921ETU001-001S",
                                "totalDistance": 753,
                                "segments": [
                                  {
                                    "order": 1,
                                    "segmentDistance": 328
                                  },
                                  {
                                    "order": 2,
                                    "segmentDistance": 93
                                  },
                                  {
                                    "order": 3,
                                    "segmentDistance": 332
                                  }
                                ]
                              }
                            }
                          ]
                        }
                      }
                      """
              )
          }
      )
  )
  @OpenApiStandardErrors
  ResponseEntity<EnduranceTrioResponse<RouteMetricsDTO>> getRouteMetrics(
      @Parameter(description = "The unique identifier of the route", example = "1") @NonNull Long id
  );
}
