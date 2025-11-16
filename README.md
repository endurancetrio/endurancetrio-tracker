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
   3. [Installation](#installation)
4. [License](#license)

## Introduction

**EnduranceTrio Tracker** is a modern REST API designed for managing IoT device locations. Built
with Java 21 and Spring Boot, the service provides a scalable and secure solution for submitting
and retrieving GPS data using API key authentication. It is ideal for applications such as fleet
management, asset tracking, and general IoT device monitoring.

### Development Team

This project was created by **Ricardo do Canto**, who is the lead developer and maintainer.

[![GitHub](https://img.shields.io/badge/GitHub-EnduranceCode-orange?logo=github)](https://github.com/EnduranceCode)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Ricardo_do_Canto-blue?logo=linkedin)](https://www.linkedin.com/in/ricardodocanto/)

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

#### 1. Submit a device location (Authenticated)

```bash
POST /tracker/v1/devices
Content-Type: application/json
Authorization: Bearer api-key-here

{
  "device": "SDABC",
  "time": "2026-09-19T06:00:00Z",
  "lat": 39.510058,
  "lon": -9.136079
}
```

**Response**: `201 Created`

```json
{
  "status": "CREATED",
  "code": 201, 
  "message": "Request handled successfully",
  "data": {
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.510058,
    "lon": -9.136079
  }
}
```

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
  "message": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079
    },
    {
      "device": "SDDEF",
      "time": "2026-09-19T06:00:06Z",
      "lat":  39.509001,
      "lon": -9.139602
    },
    {
      "device": "SDFGH",
      "time": "2026-09-19T06:00:12Z",
      "lat": 39.509773,
      "lon": -9.140004
    },
    {
      "device": "SDJKL",
      "time": "2026-09-19T06:00:24Z",
      "lat": 39.511075,
      "lon":  -9.136516
    }
  ]
}
```

#### 3. Get historical locations for a device (Authenticated, Supports pagination)

```bash
GET /tracker/v1/devices/{deviceId}/locations?page=0&size=20
Content-Type: application/json
Authorization: Bearer api-key-here
```

**Response**: `200 OK`

```json
{
  "status": "OK",
  "code": 200,
  "message": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:06:00Z",
      "lat": 39.510071,
      "lon": -9.136071
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:12:00Z",
      "lat": 39.510082,
      "lon": -9.136062
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:24:00Z",
      "lat": 39.510093,
      "lon": -9.136053
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
- **Flyway** - Database migration and version control
- **Spring Security** - API key authentication and security configuration
- **SpringDoc OpenAPI** - Automated Swagger/OpenAPI documentation generation
- **Maven** - Dependency management and build automation

### Database

The application uses an **H2 in-memory database** for development and testing purposes,
configured with PostgreSQL compatibility mode. For production environments, the database will be
switched to [**PostgreSQL**](https://www.postgresql.org/).

All database schema changes are managed with Flyway. Migration scripts are located in the
`endurancetrio-data/src/main/resources/db/migration` folder and are automatically executed on
application startup. As the project evolves, migrations will support both H2 (development) and
PostgreSQL (production).

The file [`DATABASE.md`](./endurancetrio-data/src/main/resources/db/DATABASE.md) documents
the development and management of application's database.

#### H2 Database Console

During development, you can access the H2 database console at:

**URL:** `http://localhost:8081/h2-tracker/`

#### Database Characteristics:
- **Type:** In-memory H2 database
- **Mode:** PostgreSQL compatibility
- **Persistence:** Data is cleared on application restart
- **Purpose:** Development and testing environment

> **Note**
>
> All data in the development database is transient and will reset when the application restarts.

### Installation

#### 1. Prerequisites

- [Java 21](https://javaalmanac.io/jdk/21/) or higher
- [Apache Maven](https://maven.apache.org/) (Latest stable release)

#### 2. Clone the repository

```bash
git clone git@github.com:endurancetrio/endurancetrio-tracker.git
cd endurancetrio-tracker
```

#### 3. Configure application secrets

Create the `application-secrets.yaml` configuration file from the provided
[template](./endurancetrio-app/src/main/resources/template-secrets.yaml), with the following
command:

```bash
cp endurancetrio-app/src/main/resources/template-secrets.yaml endurancetrio-app/src/main/resources/application-secrets.yaml
```
Now, edit the `application-secrets.yaml` file:

- **Set database credentials**: replace `{USER}` and `{PASSWORD}` with your desired values.

> **Security Notice**
>
> Never commit the application-secrets.yaml file, or any file containing credentials, to version
> control.
> Ensure that your `.gitignore` rules prevent accidental commits of sensitive configuration.

#### 4. Build the project

From the repository root, run the following command to compile the application and install
its dependencies:

```bash
mvn clean install
```

#### 5. Run the application

The application uses Spring Boot profiles for environment-specific configuration:

- `application-local.yaml` – Active during local development.
- `application-dev.yaml` – For development environments.
- `application-prod.yaml` – For production environments (Not ready yet).

You can manually activate a profile when running the application with `spring-boot:run`:

```bash
-Dspring-boot.run.profiles=local
```

Or. for standard JAR execution:

```bash
-Dspring.profiles.active=dev
```

A helper script, `launch-app.sh`, is provided to streamline local development. It performs
a full Maven build and then starts the application using the packaged JAR with the **local**
profile enabled:

```bash
./launch-app.sh
```

This project also includes an IntelliJ run configuration stored in the `.run/` folder. After opening
the project in [IntelliJ](https://www.jetbrains.com/idea/), you will find a `TrackerApplication`
entry in the *run/debug* configuration dropdown. Select it and run the application with
`Shift + F10`, or use `Shift + F9` to run the application in debug mode.

The run configuration uses the `local` Spring profile (`application-local.yaml`), so you can start
developing immediately without additional setup.

## License

This project is licensed under the [Functional Source License](https://fsl.software/), Version 1.1,
ALv2 Future License. See the [`LICENSE.md`](./LICENSE.md) file for details.
