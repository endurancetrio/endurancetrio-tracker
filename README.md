# EnduranceTrio Tracker REST API

![Custom Badge](https://img.shields.io/badge/java-21-orange)
![Custom Badge](https://img.shields.io/badge/Spring_Boot-3.5.7-green)

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
   1. [Core Functionality](#core-functionality)
   2. [API Endpoints](#api-endpoints)
   3. [Swagger UI Documentation](#swagger-ui-documentation)
3. [Development](#development)
   1. [Technology Stack](#technology-stack)
   2. [Database](#database)
4. [License](#license)

## Introduction

EnduranceTrio Tracker is a robust and modern REST API for managing IoT device locations. Built with
Java 21 and Spring Boot, this service provides a scalable solution for tracking device GPS data with
secure API key authentication. The API allows for efficient submission and retrieval of device
location information, making it ideal for fleet management, asset tracking, and IoT applications.

## Features

### Core Functionality

- **Secure Location Submission**: Submit device location data with API key authentication
- **Device Location Retrieval**: Get the last known location for all existing devices
- **Location History**: Access historical location data for comprehensive tracking and analysis
- **Auto-generated Documentation**: Interactive API documentation via Swagger UI

### API Endpoints

| Method | Endpoint                                         | Description                                                         | Authentication     |
|--------|--------------------------------------------------|---------------------------------------------------------------------|--------------------|
| `POST` | `/tracker/v1/devices`                            | Submit a new device location                                        | API Key Required   |
| `GET`  | `/tracker/v1/devices`                            | Get last known location for all existing devices                    | None               |
| `GET`  | `/tracker/v1/devices/{deviceId}/locations`       | Get historical locations for a device (supports pagination)         | API Key Required   |

### API Examples

#### 1. Submit a Device Location (Authenticated)

```bash
POST /tracker/v1/devices
Content-Type: application/json
X-API-Key: api-key-here

{
  "device": "SDABC",
  "time": "2026-09-19T06:00:00Z",
  "lat": 39.51006,
  "lon": -9.13608
}
```

**Response**: `201 Created`

```json
{
  "status": "CREATED",
  "code": 201, 
  "message": "Request handled with success",
  "data": {
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.51006,
    "lon": -9.13608
  }
}
```
}

#### 2. Get last known location for all existing devices

```bash
GET /tracker/v1/devices
Content-Type: application/json
```

**Response**: `200 OK`

```json
{
  "status": "OK",
  "code": 200,
  "message": "Request handled with success",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.51006,
      "lon": -9.13608
    },
    {
      "device": "SDDEF",
      "time": "2026-09-19T06:00:06Z",
      "lat":  39.50900,
      "lon": -9.13960
    },
    {
      "device": "SDFGH",
      "time": "2026-09-19T06:00:12Z",
      "lat": 39.50977,
      "lon": -9.14000
    },
    {
      "device": "SDJKL",
      "time": "2026-09-19T06:00:24Z",
      "lat": 39.51107,
      "lon":  -9.13651
    }
  ]
}
```

#### 3. Get historical locations for a device (Authenticated, Supports pagination)

```bash
GET /tracker/v1/devices/{deviceId}/locations?page=0&size=20
Content-Type: application/json
X-API-Key: api-key-here
```

**Response**: `200 OK`

```json
{
  "status": "OK",
  "code": 200,
  "message": "Request handled with success",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.51006,
      "lon": -9.13608
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:06:00Z",
      "lat": 39.51007,
      "lon": -9.13607
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:12:00Z",
      "lat": 39.51008,
      "lon": -9.13606
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:24:00Z",
      "lat": 39.51009,
      "lon": -9.13605
    }
  ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 4,
    "totalPages": 1
  }
}
```

### Swagger UI Documentation

Access the interactive API documentation at `/swagger-ui.html` when the application is running.
This interface allows you to:

- Explore all available endpoints
- Test API calls directly from your browser
- View request/response models and schemas
- Understand authentication requirements

## Development

### Technology Stack

- **Java 21** - Latest LTS version for optimal performance and features
- **Spring Boot 3.5.7** - Modern application framework with latest stable features
- **Spring Data JPA** - Robust data persistence and repository abstraction
- **H2 Database** - In-memory database for development and testing (To be replaced with PostgreSQL)
- **Spring Security** - API key authentication and security configuration
- **SpringDoc OpenAPI** - Automated Swagger/OpenAPI documentation generation
- **Maven** - Dependency management and build automation

### Database

The application uses an **H2 in-memory database** for development and testing purposes,
configured with PostgreSQL compatibility mode.

#### H2 Database Console

During development, you can access the H2 database console at:

**URL:** `http://localhost:8081/h2-tracker/`

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:tracker;MODE=PostgreSQL`
- **Username:** `EnduranceCode`
- **Password:** `EnduranceTrio`

#### Database Characteristics:
- **Type:** In-memory H2 database
- **Mode:** PostgreSQL compatibility
- **Persistence:** Data is cleared on application restart
- **Purpose:** Development and testing environment

> **Note:** The H2 database will be replaced with PostgreSQL in production environments.
> All data in the development database is transient and reset when the application restarts.

## License

This project is licensed under the Functional Source License, Version 1.1, ALv2 Future License.
See the [`LICENSE.md`](./LICENSE.md) file for details.
