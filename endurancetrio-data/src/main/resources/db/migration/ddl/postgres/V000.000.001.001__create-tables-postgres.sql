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
CREATE SEQUENCE IF NOT EXISTS seq_tracking_data_id  START WITH 1 INCREMENT BY 5 CACHE 5;

-- Create the tracker_account table
CREATE TABLE tracker_account (
    owner       VARCHAR(50)  NOT NULL,
    account_key VARCHAR(100) NOT NULL,
    enabled     BOOLEAN      NOT NULL
);

-- Create primary key and unique constraints on the tracker_account table
ALTER TABLE tracker_account ADD CONSTRAINT pk_tracker_account PRIMARY KEY (owner);
ALTER TABLE tracker_account ADD CONSTRAINT uk_tracker_account_key UNIQUE (account_key);

-- Create the tracking data table
CREATE TABLE tracking_data (
  id          BIGINT       NOT NULL DEFAULT nextval('seq_tracking_data_id'),
  account     VARCHAR(50)  NOT NULL,
  device      VARCHAR(50)  NOT NULL,
  record_time TIMESTAMPTZ  NOT NULL,
  latitude    NUMERIC(9,6) NOT NULL,
  longitude   NUMERIC(9,6) NOT NULL,
  created_at  TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP
);

-- Create primary key, foreign key, constraints and indexes on the tracking_data table
ALTER TABLE tracking_data ADD CONSTRAINT pk_tracking_data
  PRIMARY KEY (id);
ALTER TABLE tracking_data ADD CONSTRAINT fk_tracking_data_tracker_account_owner
  FOREIGN KEY (account) REFERENCES tracker_account(owner);
CREATE INDEX idx_tracking_data_device ON tracking_data(device);
CREATE INDEX idx_tracking_data_device_time ON tracking_data(device, record_time);
CREATE INDEX idx_tracking_data_account ON tracking_data(account);
CREATE INDEX idx_tracking_data_account_device ON tracking_data(account, device);
