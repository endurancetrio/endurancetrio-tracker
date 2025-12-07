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

-- Description: Creates the EnduranceTrio Tracker route database tables
--

-- Create sequence for primary key on EnduranceTrio Tracker tables
CREATE SEQUENCE IF NOT EXISTS seq_route_id START WITH 1 INCREMENT BY 5;
CREATE SEQUENCE IF NOT EXISTS seq_route_segment_id START WITH 1 INCREMENT BY 5;

-- Create the route table
CREATE TABLE route (
  id         BIGINT      NOT NULL,
  reference  VARCHAR(50) NOT NULL,
  version    INTEGER     NOT NULL DEFAULT 0,
  created_at TIMESTAMP   NOT NULL,
  updated_at TIMESTAMP
);

-- Create primary key, unique constraint and index on the route table
ALTER TABLE route ADD CONSTRAINT pk_route PRIMARY KEY (id);
ALTER TABLE route ADD CONSTRAINT uk_route_name UNIQUE (name);
CREATE INDEX idx_route_name ON route(name);

-- Create the route_segment table
CREATE TABLE route_segment (
  id            BIGINT      NOT NULL,
  route_id      BIGINT      NOT NULL,
  segment_order INTEGER     NOT NULL,
  start_device  VARCHAR(50) NOT NULL,
  end_device    VARCHAR(50) NOT NULL,
  version       INTEGER     NOT NULL DEFAULT 0,
  created_at    TIMESTAMP   NOT NULL,
  updated_at    TIMESTAMP
);

-- Create primary key, foreign key and indexes on the route_segment table
ALTER TABLE route_segment ADD CONSTRAINT pk_route_segment PRIMARY KEY (id);
ALTER TABLE route_segment ADD CONSTRAINT fk_route_segment_route_id
  FOREIGN KEY (route_id) REFERENCES route(id);
CREATE INDEX idx_route_segment_route_id ON route_segment(route_id);
CREATE INDEX idx_route_segment_route_id_segment_order ON route_segment(route_id, segment_order);
