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

-- Use the schema for the EnduranceTrio Tracker
USE endurancetrio_tracker;

-- Insert test data into tracker_account table
INSERT INTO tracker_account (owner, account_key, enabled, version, created_at) VALUES
    ('system', 'TEST_ACCOUNT_KEY_1234567890', TRUE, 0, CURRENT_TIMESTAMP);

-- Insert test data into tracking_data table
INSERT INTO tracking_data (id, account, device, record_time, latitude, longitude, active, version, created_at) VALUES
    (1, 'system', 'SDABC', '2025-09-21T06:00:00Z', 39.510058, -9.136079, TRUE, 0, CURRENT_TIMESTAMP),
    (2, 'system', 'SDDEF', '2025-09-21T06:00:06Z', 39.509001, -9.139602, TRUE, 0, CURRENT_TIMESTAMP),
    (3, 'system', 'SDFGH', '2025-09-21T06:00:12Z', 39.509773, -9.140004, TRUE, 0, CURRENT_TIMESTAMP),
    (4, 'system', 'SDJKL', '2025-09-21T06:00:24Z', 39.511075, -9.136516, TRUE, 0, CURRENT_TIMESTAMP),
    (5, 'system', 'SDABC', '2025-09-21T06:06:00Z', 39.510071, -9.136071, TRUE, 0, CURRENT_TIMESTAMP),
    (6, 'system', 'SDABC', '2025-09-21T06:12:00Z', 39.510082, -9.136062, TRUE, 0, CURRENT_TIMESTAMP),
    (7, 'system', 'SDABC', '2025-09-21T06:24:00Z', 39.510093, -9.136053, TRUE, 0, CURRENT_TIMESTAMP);

-- Align the sequence to take in consideration manually inserted records.
-- The next sequence  value will be 100, which will help to distinguish the test values.
ALTER SEQUENCE seq_tracking_data_id RESTART WITH 100;
