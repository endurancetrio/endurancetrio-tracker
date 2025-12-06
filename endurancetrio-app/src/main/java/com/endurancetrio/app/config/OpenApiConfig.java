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

package com.endurancetrio.app.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "Account Name",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "ET-Owner",
    description = "Enter the Account Identifier (e.g., 'system')"
)
@SecurityScheme(
    name = "API Key",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization",
    description = "API Key in the format: Bearer {api-key}"
)
public class OpenApiConfig {

  @Value("${app.version:unknown}")
  private String appVersion;

  @Bean
  public OpenAPI enduranceTrioOpenAPI() {
    return new OpenAPI().info(new Info().title("EnduranceTrio Tracker API")
            .description("""
                EnduranceTrio Tracker is a modern REST API designed for managing IoT device
                telemetry. Built with Java 21 and Spring Boot, the service provides a scalable
                and secure solution for submitting and retrieving devices telemetry data
                 using API key authentication. It is ideal for applications
                 such as fleet management, asset tracking, and general IoT device monitoring.
                
                Application Version: %s
                """.formatted(appVersion))
            .version("v1.0.0"))
        .addSecurityItem(new SecurityRequirement().addList("Account Name").addList("API Key"));
  }

  /**
   * Tracker domain local development group (Full Internal Path)
   */
  @Bean
  public GroupedOpenApi localTrackerApi() {
    return GroupedOpenApi.builder()
        .group("tracker-local")
        .displayName("Tracker Domain (LOCAL)")
        .pathsToMatch("/api/tracker/**")
        .addOpenApiCustomizer(localServerCustomizer())
        .build();
  }

  /**
   * Tracker domain production Group (Stripped External Path)
   */
  @Bean
  public GroupedOpenApi productionTrackerApi() {
    return GroupedOpenApi.builder()
        .group("tracker-prod")
        .displayName("Tracker Domain (PROD)")
        .pathsToMatch("/api/tracker/**")
        .addOpenApiCustomizer(productionPathStripper())
        .addOpenApiCustomizer(productionServerCustomizer())
        .build();
  }

  /**
   * Customizer to define the local server URL
   */
  private OpenApiCustomizer localServerCustomizer() {
    return openApi -> openApi.servers(List.of(
        new Server().url("http://localhost:8081").description("Local Development")
    ));
  }

  /**
   * Customizer to define the production server URL
   */
  private OpenApiCustomizer productionServerCustomizer() {
    return openApi -> openApi.servers(List.of(
        new Server().url("https://api.endurancetrio.com").description("Production Gateway")
    ));
  }

  /**
   * Customizer to modify the paths shown in the documentation.
   * This removes the '/api' prefix from all paths for documentation purposes,
   * reflecting the client-facing URL when accessed via the gateway.
   */
  private OpenApiCustomizer productionPathStripper() {
    return openApi -> {
      io.swagger.v3.oas.models.Paths modifiedPaths = new io.swagger.v3.oas.models.Paths();

      if (openApi.getPaths() != null) {
        openApi.getPaths().forEach((path, pathItem) -> {
          if (path.startsWith("/api")) {
            String newPath = path.substring(4);
            modifiedPaths.addPathItem(newPath, pathItem);
          } else {
            modifiedPaths.addPathItem(path, pathItem);
          }
        });
      }
      openApi.setPaths(modifiedPaths);
    };
  }
}
