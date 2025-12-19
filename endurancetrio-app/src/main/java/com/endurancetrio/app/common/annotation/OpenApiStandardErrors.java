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

package com.endurancetrio.app.common.annotation;

import com.endurancetrio.app.common.response.EnduranceTrioResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    responseCode = "401",
    description = "Unauthorized - invalid or missing authentication",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = EnduranceTrioResponse.class),
        examples = {
            @ExampleObject(
                name = "Auth Error",
                summary = "Example of authentication failure",
                value = """
                    {
                      "status": 401,
                      "message": "Unauthorized",
                      "details": "Authentication failed"
                    }
                    """
            )
        }
    )
)
@ApiResponse(
    responseCode = "400",
    description = "Bad request - data validation failure",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = EnduranceTrioResponse.class),
        examples = {
            @ExampleObject(
                name = "Validation Error",
                summary = "Example of data validation failure",
                value = """
                    {
                      "status": 400,
                      "message": "Bad Request",
                      "details": "The request was made with invalid or incomplete data"
                    }
                    """
            )
        }
    )
)
@ApiResponse(
    responseCode = "404",
    description = "Resource not found",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = EnduranceTrioResponse.class),
        examples = {
            @ExampleObject(
                name = "Not found Error",
                summary = "Example of a resource not found failure",
                value = """
                    {
                      "status": 404,
                      "message": "Not Found",
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
    responseCode = "500",
    description = "Internal Server Error",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = @Schema(implementation = EnduranceTrioResponse.class),
        examples = {
            @ExampleObject(
                name = "Internal Server Error",
                summary = "Example of an internal server failure",
                value = """
                    {
                      "status": 500,
                      "message": "Internal Server Error",
                      "details": "An internal server error occurred"
                    }
                    """
            )
        }
    )
)
public @interface OpenApiStandardErrors {

}
