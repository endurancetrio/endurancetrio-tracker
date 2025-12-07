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

-- Description: Inserts test data into EnduranceTrio Tracker route tables
--

-- Use the schema for the EnduranceTrio Tracker
SET search_path TO endurancetrio_tracker;

-- Insert test data into route table
INSERT INTO route (id, reference, created_at)
  VALUES
    (1, '20260921ETU001-001S', CURRENT_TIMESTAMP)
  ON CONFLICT (id) DO NOTHING;

-- Insert test data into route_segment table
INSERT INTO route_segment (id, route_id, segment_order, start_device, end_device, created_at)
  VALUES
    (1, 1, 1, 'SDABC', 'SDDEF', CURRENT_TIMESTAMP),
    (2, 1, 2, 'SDDEF', 'SDFGH', CURRENT_TIMESTAMP),
    (3, 1, 3, 'SDFGH', 'SDJKL', CURRENT_TIMESTAMP)
  ON CONFLICT (id) DO NOTHING;

-- Align the sequence to take in consideration manually inserted records.
-- The next sequence  value will be 100, which will help to distinguish the test values.
SELECT setval('seq_route_id', 99, true);
SELECT setval('seq_route_segment_id', 99, true);
