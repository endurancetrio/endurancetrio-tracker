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

-- Description: Creates the EnduranceTrio Tracker REST API database tables
--

CREATE SCHEMA IF NOT EXISTS endurancetrio_tracker;

-- Create sequence for tracking tracking data primary key
CREATE SEQUENCE IF NOT EXISTS seq_tracking_data_id START WITH 1 INCREMENT BY 5;

-- Create the tracking data table
CREATE TABLE tracking_data (
  id          BIGINT           NOT NULL DEFAULT NEXT VALUE FOR seq_tracking_data_id,
  device      VARCHAR(50)      NOT NULL,
  record_time TIMESTAMP        NOT NULL,
  latitude    DOUBLE PRECISION NOT NULL,
  longitude   DOUBLE PRECISION NOT NULL,
  owner       VARCHAR(50)      NOT NULL,
  created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Create primary key, constraints and indexes on the tracking_data table
ALTER TABLE tracking_data ADD CONSTRAINT pk_tracking_data PRIMARY KEY (id);
CREATE INDEX idx_tracking_data_device ON tracking_data(device);
CREATE INDEX idx_tracking_data_device_time ON tracking_data(device, record_time);
CREATE INDEX idx_tracking_data_owner ON tracking_data(owner);
CREATE INDEX idx_tracking_data_owner_device ON tracking_data(owner, device);
