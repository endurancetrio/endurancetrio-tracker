#
# Copyright (c) 2025-2025 Ricardo do Canto
#
# This file is part of the EnduranceTrio Tracker project.
#
# Licensed under the Functional Software License (FSL), Version 1.1, ALv2 Future License
# (the "License");
#
# You may not use this file except in compliance with the License. You may obtain a copy
# of the License at https://fsl.software/
#
# THE SOFTWARE IS PROVIDED "AS IS" AND WITHOUT WARRANTIES OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING WITHOUT LIMITATION WARRANTIES OF FITNESS FOR A PARTICULAR
# PURPOSE, MERCHANTABILITY, TITLE OR NON-INFRINGEMENT.
#
# IN NO EVENT WILL WE HAVE ANY LIABILITY TO YOU ARISING OUT OF OR RELATED TO THE
# SOFTWARE, INCLUDING INDIRECT, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
# EVEN IF WE HAVE BEEN INFORMED OF THEIR POSSIBILITY IN ADVANCE.
#

FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /opt/endurancetrio-tracker

ARG JAR_FILE=endurancetrio-app/target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/endurancetrio-tracker

# We define a default user 'endurancetrio', but it can be overrided at build time
ARG APP_USER=endurancetrio

RUN addgroup -S ${APP_USER} && adduser -S ${APP_USER} -G ${APP_USER} && apk --no-cache add curl

COPY --from=builder /opt/endurancetrio-tracker/dependencies/ ./
COPY --from=builder /opt/endurancetrio-tracker/spring-boot-loader/ ./
COPY --from=builder /opt/endurancetrio-tracker/snapshot-dependencies/ ./
COPY --from=builder /opt/endurancetrio-tracker/application/ ./

RUN mkdir -p /opt/endurancetrio-tracker/logs && chown -R ${APP_USER}:${APP_USER} /opt/endurancetrio-tracker

USER ${APP_USER}

ENTRYPOINT ["java", \
            "-Xms512m", \
            "-Xmx512m", \
            "-XX:+UseG1GC", \
            "org.springframework.boot.loader.launch.JarLauncher"]
