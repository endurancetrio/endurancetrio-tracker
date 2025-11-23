# EnduranceTrio Tracker REST API

![Custom Badge](https://img.shields.io/badge/java-21-orange)
![Custom Badge](https://img.shields.io/badge/Spring_Boot-3.5.7-green)

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
   1. [Core Functionality](#core-functionality)
   2. [API Endpoints](#api-endpoints)
   3. [Swagger UI & API Documentation](#swagger-ui--api-documentation)
3. [Development](#development)
   1. [Technology Stack](#technology-stack)
   2. [Database](#database)
   3. [Installation](#installation)
   4. [Code & Naming Conventions](#code--naming-conventions)
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
| `GET`  | `/tracker/v1/devices`                            | Get last known location for all existing devices                    | API Key Required   |
| `GET`  | `/tracker/v1/devices/{device}/locations`         | Get historical locations for a device (supports pagination)         | API Key Required   |

### API Examples

#### 1. Submit a device location (Authenticated)

```bash
POST /tracker/v1/devices
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here

{
  "device": "SDABC",
  "time": "2026-09-19T06:00:00Z",
  "lat": 39.510058,
  "lon": -9.136079,
  "active": true
}
```

**Response**: `201 Created`

```json
{
  "code": 201,
  "status": "Created",
  "details": "Request handled successfully",
  "data": {
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.510058,
    "lon": -9.136079,
    "active": true
  }
}
```

##### `cURL` request (assuming the application is running on localhost:8081):

```bash
curl -X POST 'http://localhost:8081/api/tracker/v1/devices' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <api-key-here>' \
  -H 'ET-Owner: <account-name-here>' \
  -d '{
    "device": "SDABC",
    "time": "2026-09-19T06:00:00Z",
    "lat": 39.510058,
    "lon": -9.136079,
    "active": true
  }'
```

#### 2. Get last known location for all existing devices (Authenticated)

```bash
GET /tracker/v1/devices
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here
```

**Response**: `200 OK`

```json
{
  "code": 200,
  "status": "OK",
  "details": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079,
      "active": true
    },
    {
      "device": "SDDEF",
      "time": "2026-09-19T06:00:06Z",
      "lat":  39.509001,
      "lon": -9.139602,
      "active": true
    },
    {
      "device": "SDFGH",
      "time": "2026-09-19T06:00:12Z",
      "lat": 39.509773,
      "lon": -9.140004,
      "active": true
    },
    {
      "device": "SDJKL",
      "time": "2026-09-19T06:00:24Z",
      "lat": 39.511075,
      "lon":  -9.136516,
      "active": true
    }
  ]
}
```

#### 3. Get historical locations for a device (Authenticated, Supports pagination)

```bash
GET /tracker/v1/devices/{deviceId}/locations?page=0&size=20
Content-Type: application/json
Authorization: Bearer api-key-here
ET-Owner: account-name-here
```

**Response**: `200 OK`

```json
{
  "code": 200,
  "status": "OK",
  "details": "Request handled successfully",
  "data": [
    {
      "device": "SDABC",
      "time": "2026-09-19T06:00:00Z",
      "lat": 39.510058,
      "lon": -9.136079,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:06:00Z",
      "lat": 39.510071,
      "lon": -9.136071,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:12:00Z",
      "lat": 39.510082,
      "lon": -9.136062,
      "active": true
    },
    {
      "device": "SDABC",
      "time": "2026-09-19T06:24:00Z",
      "lat": 39.510093,
      "lon": -9.136053,
      "active": true
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

### Swagger UI & API Documentation

Access the interactive API documentation at [`/swagger-ui.html`](http://localhost:8081/swagger-ui.html)
when the application is running. This interface allows you to:

- Explore all available endpoints within the **Tracker** domain.
- View request/response models (e.g., `TrackingDataDTO`).
- Test API calls directly from your browser.

#### Authentication Guide

The EnduranceTrio Tracker API requires **Dual-Header Authentication**. To test protected endpoints
in Swagger UI, follow these steps:

1. Click the **Authorize** button at the top right of the page.
2. You will see a modal with two separate sections. You must configure **both** to successfully
   make requests:

| Field Label      | Corresponding Header | Value Format                             | Action                          |
|------------------|----------------------|------------------------------------------|---------------------------------|
| **Account Name** | `ET-Owner`           | Your account identifier (e.g., `system`) | Enter ID & click **Authorize**  |
| **API Key**      | `Authorization`      | `Bearer <your_api_key>`                  | Enter Key & click **Authorize** |

> **Important:**
> You must click the **Authorize** button for *each* field independently. Ensure both padlocks
> appear "locked" (closed) before closing the modal and testing endpoints.

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

#### Database Characteristics

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

### Code & Naming Conventions

#### Controllers, Services and Repositories

This section establishes naming guidelines for controllers, services and repositories, based on clarity,
maintainability, and semantic meaning.

##### General Principles

- **Interfaces define contracts**; implementations should have meaningful names.
- Use suffixes that reflect the role or nature of the implementation (e.g., `Main`, `Cached`, `Remote`).
- Avoid generic suffixes like `Impl` unless absolutely necessary.
- Keep naming consistent across layers.

##### Controllers

###### REST Controllers

- **Interface**:
  - Purpose: Hold OpenAPI annotations and define endpoint contracts.
  - Naming: `DomainAPI` (e.g., `UserAPI`).
- **Implementation**:
  - Annotate with `@EnduranceTrioRestController`.
  - Naming: `DomainRestController` (e.g., `UserRestController`).

```java
public interface UserAPI {
  /* OpenAPI annotations */
}

@EnduranceTrioRestController
public class UserRestController implements UserAPI {
  /* endpoints */
}
```

###### Web Controllers

- **Interface**: Optional (usually not needed unless for documentation or testing).
  - Naming: `DomainWeb` (e.g., `UserWeb`).
- **Implementation**:
  - Annotate with `@Controller`.
  - Naming: `EntityWebController` (e.g., `UserWebController`).

```java
@Controller
public class UserWebController {
  /* Thymeleaf views */
}
```

##### Service Layer

- **Interface**:
  - Naming: `DomainService` (e.g., `UserService`).
- **Implementation**:
  - Annotate with `@Service`.
  - Naming:
    - If single implementation: `DomainServiceMain` (e.g., `UserServiceMain`).
    - If multiple implementations: Use descriptive suffixes (e.g., `UserServiceCached`, `UserServiceRemote`).

```java
public interface UserService {
  /* business logic */
}

@Service
public class UserServiceMain implements UserService {
  /* implementation */
}
```

> **Why not `Impl`?**
> `Impl` is semantically weak. Descriptive suffixes improve readability and convey purpose.

##### Repository Layer

- Prefer Spring Data JPA interfaces:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
```

## License

This project is licensed under the [Functional Source License](https://fsl.software/), Version 1.1,
ALv2 Future License. See the [`LICENSE.md`](./LICENSE.md) file for details.
