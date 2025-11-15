#!/bin/bash
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

clear -x

echo "------------------------------------------------------------------------------------------------------------------------"
echo -e "[\e[34mINFO\e[0m] Building EnduranceTrio Tracker Application..."
echo "------------------------------------------------------------------------------------------------------------------------"
echo
./mvnw clean package -DskipTests

echo
echo "------------------------------------------------------------------------------------------------------------------------"
echo -e "[\e[34mINFO\e[0m] Starting EnduranceTrio Tracker Application..."
echo "------------------------------------------------------------------------------------------------------------------------"
echo
java -jar -Dspring.profiles.active=local endurancetrio-app/target/*.jar
