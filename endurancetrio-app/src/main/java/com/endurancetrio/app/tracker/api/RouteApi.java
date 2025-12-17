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
import com.endurancetrio.business.tracker.dto.RouteDTO;
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
                            "code": 200,
                            "status": "OK",
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
  ResponseEntity<@NonNull EnduranceTrioResponse<List<RouteDTO>>> findAll();

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
                        "code": 200,
                        "status": "OK",
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
      responseCode = "404", description = "Resource not found - the route configuration could not be found",
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
                        "details": "No route found with ID {id}",
                        "data": {
                          "error": "NOT_FOUND",
                          "message": "The requested resource was not found"
                        }
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
  ResponseEntity<EnduranceTrioResponse<RouteDTO>> findById(@NonNull Long id);

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
                        "code": 201,
                        "status": "Created",
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
  @ApiResponse(
      responseCode = "400",
      description = "Bad request - invalid route configuration",
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
      responseCode = "401",
      description = "Unauthorized - invalid or missing authentication",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = EnduranceTrioResponse.class),
          examples = {
              @ExampleObject(
                  name = "Auth Error", summary = "Example of authentication failure", value = """
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
      responseCode = "404",
      description = "Not found - the route configuration could not be found",
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
}
