--
-- Copyright (c) 2025-2025 Ricardo do Canto
--
-- This file is part of the EnduranceTrio Tracker project.
--
-- Licensed under the Functional Software License (FSL), Version 1.1, ALv2 Future License
-- (the "License");
--
-- You may not use this file except in compliance with the License. You may obtain a copy
-- of the License at https://fsl.software/
--
-- THE SOFTWARE IS PROVIDED "AS IS" AND WITHOUT WARRANTIES OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING WITHOUT LIMITATION WARRANTIES OF FITNESS FOR A PARTICULAR
-- PURPOSE, MERCHANTABILITY, TITLE OR NON-INFRINGEMENT.
--
-- IN NO EVENT WILL WE HAVE ANY LIABILITY TO YOU ARISING OUT OF OR RELATED TO THE
-- SOFTWARE, INCLUDING INDIRECT, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
-- EVEN IF WE HAVE BEEN INFORMED OF THEIR POSSIBILITY IN ADVANCE.
--

-- Description: Inserts test data into EnduranceTrio Tracker REST API database tables
--

-- Insert test data into tracking_data table
INSERT INTO tracking_data (device, record_time, latitude, longitude, owner) VALUES
    ('SDABC', '2026-09-19T06:00:00Z', 39.510058, -9.136079, 'system'),
    ('SDDEF', '2026-09-19T06:00:06Z', 39.509001, -9.139602, 'system'),
    ('SDFGH', '2026-09-19T06:00:12Z', 39.509773, -9.140004, 'system'),
    ('SDJKL', '2026-09-19T06:00:24Z', 39.511075, -9.136516, 'system'),
    ('SDABC', '2026-09-19T06:06:00Z', 39.510071, -9.136071, 'system'),
    ('SDABC', '2026-09-19T06:12:00Z', 39.510082, -9.136062, 'system'),
    ('SDABC', '2026-09-19T06:24:00Z', 39.510093, -9.136053, 'system');
