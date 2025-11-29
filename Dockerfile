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

ENV APP_USER=${APP_USER}
# We set the APP_HOME value matching the WORKDIR path
ENV APP_HOME=/opt/endurancetrio-tracker

RUN addgroup -S ${APP_USER} && adduser -S ${APP_USER} -G ${APP_USER} && apk --no-cache add curl shadow su-exec

COPY --from=builder /opt/endurancetrio-tracker/dependencies/ ./
COPY --from=builder /opt/endurancetrio-tracker/spring-boot-loader/ ./
COPY --from=builder /opt/endurancetrio-tracker/snapshot-dependencies/ ./
COPY --from=builder /opt/endurancetrio-tracker/application/ ./

RUN mkdir -p /opt/endurancetrio-tracker/logs && chown -R ${APP_USER}:${APP_USER} /opt/endurancetrio-tracker

COPY docker/build-assets/entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

# The container starts as root to run the entrypoint script
# The script will then switch to the user specified by PUID/PGID
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
