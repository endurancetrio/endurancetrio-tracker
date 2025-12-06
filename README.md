# EnduranceTrio Tracker REST API

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
   1. [Core Functionality](#core-functionality)
   2. [API Endpoints](#api-endpoints)
   3. [Swagger UI & API Documentation](#swagger-ui--api-documentation)
3. [Development](#development)
   1. [Technology Stack](./docs/development.md#technology-stack)
   2. [API Key Management](./docs/development.md#api-key-management)
   3. [Database](./docs/development.md#database)
   4. [Installation](./docs/development.md#installation)
   5. [Code & Naming Conventions](./docs/development.md#code--naming-conventions)
   6. [Programmatic Version Management](./docs/development.md#programmatic-version-management)
4. [Deployment](#deployment)
   1. [Container Architecture](./docs/deployment.md#container-architecture)
   2. [Server Setup](./docs/deployment.md#server-setup)
   3. [Reverse Proxy Setup](./docs/deployment.md#reverse-proxy-setup)
   4. [SSL Certificate](./docs/deployment.md#ssl-certificate)
5. [License](#license)

## Introduction

**EnduranceTrio Tracker** is a modern REST API designed for managing IoT device locations. Built
with Java 21 and Spring Boot 4, the service provides a scalable and secure solution for submitting
and retrieving GPS data using API key authentication. It is ideal for applications such as fleet
management, asset tracking, and general IoT device monitoring.

### Development Team

This project was created by **Ricardo do Canto**, who is the lead developer and maintainer.

[![GitHub](https://img.shields.io/badge/GitHub-EnduranceCode-black?logo=github&logoColor=white)](https://github.com/EnduranceCode)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Ricardo_do_Canto-0A66C2?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/ricardodocanto/)

## Features

### Core Functionality

- **Secure Location Submission**: Submit device location data with API key authentication
- **Device Location Retrieval**: Get the last known location for all existing devices
- **Location History**: Access historical location data for comprehensive tracking and analysis
- **Auto-generated Documentation**: Interactive API documentation via Swagger UI

### API Endpoints

The following table summarizes the available endpoints.

| Method | Endpoint                                         | Description                                                         | Authentication     |
|--------|--------------------------------------------------|---------------------------------------------------------------------|--------------------|
| `POST` | `/tracker/v1/devices`                            | Submit a new device location                                        | API Key Required   |
| `GET`  | `/tracker/v1/devices`                            | Get last known location for all existing devices                    | API Key Required   |
| `GET`  | `/tracker/v1/devices/{device}/locations`         | Get historical locations for a device (supports pagination)         | API Key Required   |

For comprehensive documentation including request/response schemas, examples, and error handling,
see the following documents.

- [Tracker API Endpoints](docs/api-endpoints-tracker.md).

### Swagger UI & API Documentation

The **EnduranceTrio Tracker** API provides interactive documentation through **Swagger UI**, with
separate documentation groups for different environments. This graphic interface allows you to:

- Explore all available endpoints within the **Tracker** domain.
- View request/response models (e.g., `TrackingDataDTO`).
- Test API calls directly from your browser.

#### Environment-Specific Access

The Swagger UI endpoint varies by environment and deployment configuration:

| Environment           | Default URL                             | Note                        |
|-----------------------|-----------------------------------------|-----------------------------|
| **Local Development** | `http://localhost:8081/swagger-ui.html` | Direct application access   |
| **Public**            | `openapi` subdomain                     | Configured via Apache proxy |

#### Selecting the Correct Environment Group

When you access Swagger UI, **you must select the appropriate documentation group**
for your environment:

1. **Look for the dropdown menu** in the top-right corner of the Swagger UI page (labeled with
   the current group name)
2. **Choose the correct group** based on your environment:
    - **`Tracker Domain (LOCAL)`** - For local development (shows paths with `/api` prefix)
    - **`Tracker Domain (PROD)`** - For production use (shows paths without `/api` prefix)

> **Important:**
> When accessing Swagger UI through the **openapi subdomain** in production, the gateway strips
> the `/api` prefix from paths. The documentation reflects the client-facing URLs, not the internal
> application paths.

**Why this matters:**
- Different groups show different server URLs and path structures
- Selecting the wrong group may show incorrect endpoint URLs
- The authentication setup is shared between groups

#### Authentication Guide

The **EnduranceTrio Tracker** API requires **Dual-Header Authentication**. To test protected
endpoints in Swagger UI, follow these steps:

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

For detailed development documentation (API key management, database configuration, installation
instructions, coding conventions, etc.), please refer
to the [Development Guide](./docs/development.md).

### Quick Start

1. **Prerequisites**: Java 21, Maven, PostgreSQL
2. **Clone**: `git clone git@github.com:EnduranceCode/endurancetrio-tracker.git`
3. **Configure**: Set up `application-secrets.yaml` from template
4. **Build**: `mvn clean install`
5. **Run**: Use `./launch-app.sh` or the provided IntelliJ run configuration

> See the [full Development Guide](./docs/development.md) for comprehensive instructions.

## Deployment

For deployment instructions (container architecture, server and reverse proxy setup,
SSL certificate configuration, etc.), please refer to the [Deployment Guide](./docs/deployment.md).

## License

This project is licensed under the [Functional Source License](https://fsl.software/), Version 1.1,
ALv2 Future License. See the [`LICENSE.md`](./LICENSE.md) file for details.
