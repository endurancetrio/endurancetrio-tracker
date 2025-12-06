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

-- Description: Create the EnduranceTrio Tracker telemetry management database table
--

-- Drops tracking_data table and its sequence
DROP TABLE IF EXISTS tracking_data;
DROP SEQUENCE IF EXISTS seq_tracking_data_id;

-- Create sequence for device_telemetry table primary key
CREATE SEQUENCE IF NOT EXISTS seq_device_telemetry_id START WITH 1 INCREMENT BY 5 CACHE 5;

-- Create the device_telemetry table
CREATE TABLE device_telemetry (
  id          BIGINT           NOT NULL,
  account     VARCHAR(50)      NOT NULL,
  device      VARCHAR(50)      NOT NULL,
  record_time TIMESTAMP        NOT NULL,
  latitude    DOUBLE PRECISION NOT NULL,
  longitude   DOUBLE PRECISION NOT NULL,
  active      BOOLEAN          NOT NULL,
  version     INTEGER          NOT NULL DEFAULT 0,
  created_at  TIMESTAMP        NOT NULL,
  updated_at  TIMESTAMP
);

-- Create primary key, foreign key and indexes on the device_telemetry table
ALTER TABLE device_telemetry ADD CONSTRAINT pk_device_telemetry
  PRIMARY KEY (id);
ALTER TABLE device_telemetry ADD CONSTRAINT fk_device_telemetry_tracker_account_owner
  FOREIGN KEY (account) REFERENCES tracker_account(owner);
CREATE INDEX idx_device_telemetry_device ON device_telemetry(device);
CREATE INDEX idx_device_telemetry_device_time ON device_telemetry(device, record_time);
CREATE INDEX idx_device_telemetry_account ON device_telemetry(account);
CREATE INDEX idx_device_telemetry_account_device ON device_telemetry(account, device);
