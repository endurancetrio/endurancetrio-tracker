# EnduranceTrio Tracker REST API

![Custom Badge](https://img.shields.io/badge/java-21-orange)
![Custom Badge](https://img.shields.io/badge/Spring_Boot-4.0.0-green)

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
   1. [Core Functionality](#core-functionality)
   2. [API Endpoints](#api-endpoints)
   3. [Swagger UI & API Documentation](#swagger-ui--api-documentation)
3. [Development](#development)
   1. [Technology Stack](#technology-stack)
   2. [Database](#database)
   3. [API Key Management](#api-key-management)
   4. [Installation](#installation)
   5. [Code & Naming Conventions](#code--naming-conventions)
   6. [Programmatic Version Management](#programmatic-version-management)
4. [Deployment](#deployment)
   1. [Container Architecture](#container-architecture)
   2. [Server Setup](#server-setup)
   3. [Reverse Proxy Setup](#reverse-proxy-setup)
   4. [SSL Certificate](#ssl-certificate)
5. [License](#license)

## Introduction

**EnduranceTrio Tracker** is a modern REST API designed for managing IoT device locations. Built
with Java 21 and Spring Boot 4, the service provides a scalable and secure solution for submitting
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

```shell
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

```shell
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

```shell
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

##### `cURL` request (assuming the application is running on localhost:8081):

```shell
curl -X GET 'http://localhost:8081/api/tracker/v1/devices' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <api-key-here>' \
  -H 'ET-Owner: <account-name-here>'
```

#### 3. Get historical locations for a device (Authenticated, Supports pagination)

```shell
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

The EnduranceTrio Tracker API provides interactive documentation through **Swagger UI**, with
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
- **Spring Boot 4.0.0** - Modern application framework with latest stable features
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

### API Key Management

#### Overview

The **EnduranceTrio Tracker** REST API uses secure API key authentication. All API keys are stored
as bcrypt hashes in the database for enhanced security. This section explains how to generate secure
API keys, create their bcrypt hashes, and store them in the database.

#### Generate Secure API keys

Generate cryptographically secure random API keys, with `openssl`, executing the following command:

```shell
openssl rand -base64 32
```

The above command generates a 32-character secure API key. A 48-character secure API key (even more
secure), can be generated executing the following command:

```shell
openssl rand -base64 48
```

#### Generate bcrypt Hashes

Using **Python3** is the recommended method to generate bcrypt hashes. On Ubuntu/Debian, ensure
the bcrypt library is installed with the following command:

```shell
sudo apt update && sudo apt install python3-bcrypt
```

Then, replace the ***{LABEL}*** in the below command as appropriate and execute it to generate
the bcrypt hash from the previously generated API key.

```shell
python3 -c "import bcrypt; print(bcrypt.hashpw('{API_KEY}'.encode('utf-8'), bcrypt.gensalt(rounds=12)).decode('utf-8'))"
```

> **Label Definition**
>
> + **{API_KEY}** : The API key

#### Initialize First Account via Environment Variables

The application supports automatic initialization of the first tracker account using environment
variables. This is the recommended approach for initial setup.

The service responsible for initializing the first tracker account will be executed upon application
first startup. This service checks for the presence of environment variables `FIRST_OWNER` and
`FIRST_HASH` during application startup. If both variables are provided and valid, it creates
the initial tracker account in the database. If an account with the provided owner name already
exists in the database, its key hash will be overridden with the provided key hash.

##### Complete Workflow

```shell
# 1. Generate a secure API key
API_KEY=$(openssl rand -base64 32)
echo "Generated API Key: ${API_KEY}"

# 2. Generate bcrypt hash
HASH=$(python3 -c "import bcrypt; print(bcrypt.hashpw('${API_KEY}'.encode('utf-8'), bcrypt.gensalt(rounds=12)).decode('utf-8'))")
echo "Bcrypt Hash: ${HASH}"

# 3. Set environment variables and start application
export FIRST_OWNER="system"
export FIRST_HASH="${HASH}"

# IMPORTANT: Store the raw API key securely - you won't be able to retrieve it later!
# Only the bcrypt hash should be stored in the database.
```

#### Store Hashes in the Database

Access the database console, replace the ***{LABELS}*** in the below SQL command as appropriate and
execute it to insert the new account into the `tracker_account` table.

```sql
INSERT INTO tracker_account (owner, account_key, enabled, version, created_at)
    VALUES ('{OWNER}', '{API_KEY_HASH}', TRUE, 0, CURRENT_TIMESTAMP);
```

> **Label Definition**
>
> + **{OWNER}** : The name of the owner/user of the API key
> + **{API_KEY_HASH}** : The bcrypt hash of the API key (not the raw API key)

#### Security Best Practices

1. Key Generation
   - Use cryptographically secure random generators
   - Minimum 32 characters length
   - Base64 encoding for URL-safe characters
2. Hash Storage
   - Always use bcrypt with cost factor 12
   - Never store raw API keys in the database
   - Include account name and creation timestamp
3. Operational Security
   - Securely transmit the raw API key to the end user once
   - Implement key rotation policies
   - Monitor and audit API key usage
   - Store the raw key securely during initial distribution
   - When the application initial startup is completed, unset the environment variables
     `FIRST_OWNER` and `FIRST_HASH`.

#### Verification

You can verify API keys work by testing with the provided endpoints using the
`Authorization: Bearer api-key-here` and `ET-Owner: account-name-here` headers as shown
in the API examples section.

### Installation

#### 1. Prerequisites

- [Java 21](https://javaalmanac.io/jdk/21/) or higher
- [Apache Maven](https://maven.apache.org/) (Latest stable release)

#### 2. Clone the repository

```shell
git clone git@github.com:endurancetrio/endurancetrio-tracker.git
cd endurancetrio-tracker
```

#### 3. Configure application secrets

Create the `application-secrets.yaml` configuration file from the provided
[template](./endurancetrio-app/src/main/resources/template-secrets.yaml), with the following
command:

```shell
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

```shell
mvn clean install
```

#### 5. Run the application

The application uses Spring Boot profiles for environment-specific configuration:

- `application-local.yaml` – Active during local development.
- `application-dev.yaml` – For development environments.
- `application-prod.yaml` – For production environments (Not ready yet).

You can manually activate a profile when running the application with `spring-boot:run`:

```shell
-Dspring-boot.run.profiles=local
```

Or. for standard JAR execution:

```shell
-Dspring.profiles.active=dev
```

A helper script, `launch-app.sh`, is provided to streamline local development. It performs
a full Maven build and then starts the application using the packaged JAR with the **local**
profile enabled:

```shell
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

### Programmatic Version Management

This project supports programmatic version updates across all Maven modules. It can be achieved
replacing the label as appropriate in the below command and then executing it.

```shell
mvn versions:set -DnewVersion={VERSION_NUMBER}
```

> **Label Definition**
>
> + **{VERSION_NUMBER}** : The new Sematic Version number to be applied across all Maven modules

The changes applied with the above command can be reverted executing the following command:

```shell
mvn versions:revert
```

Or commited with the following command:

```shell
mvn versions:commit
```

## Deployment

The **EnduranceTrio Tracker** REST API is optimized for containerized deployments using
[Docker](https://www.docker.com/). The project includes a robust CI/CD pipeline that automatically
builds and publishes OCI-compliant images to the GitHub Container Registry ([GHCR](https://github.com/orgs/endurancetrio/packages)).

### Container Architecture

The deployment uses a lightweight Alpine-based image (`eclipse-temurin:21-jre-alpine`) optimized
for security and performance.

Key features of the container setup include:
- **Multi-stage Build**: Minimizes image size by separating the build environment from the runtime.
- **Non-root Execution**: Runs as a dedicated non-root user for security.
- **PUID/PGID Mapping**: A custom entrypoint script allows mapping the internal container user
  to a host user, ensuring seamless volume permission management for logs and data.
- **Health Checks**: Built-in integration with Spring Boot Actuator for orchestration health
  monitoring.

Official images are available at:

`ghcr.io/endurancetrio/endurancetrio-tracker`

| Tag Format    | Description                                      | Use Case        |
|---------------|--------------------------------------------------|-----------------|
| `vX.Y.Z`      | Semantic version release (e.g., `v1.0.0`)        | **Production**  |
| `sha-XXXXXXX` | Short Git SHA commit hash                        | Testing/Staging |

### Server Setup

The recommended way to deploy the application is using Docker Compose. To create the necessary
folders for the app installation, execute the following command in the deployment server:

```shell
sudo mkdir -p /opt/endurancetrio-tracker/logs
```

We will create a user to manage the **EnduranceTrio Tracker** application and set it as the owner
of the folder `/opt/endurancetrio-tracker/logs/`. This will be achieved with the execution of the
following commands:

```shell
sudo useradd -r -s /usr/sbin/nologin endurancetrio
sudo chown -R endurancetrio:endurancetrio /opt/endurancetrio-tracker/logs/
```

To confirm that the folder `/opt/endurancetrio-tracker/logs/` has the correct ownership,
check the output of the following command:

```shell
ls -lag
```

The folder `/opt/endurancetrio-tracker` will store the files necessary to deploy the application
with Docker Compose. To download, from this repository to the server, the `docker-compose.yaml` file
and the template for the `.env` file, execute the following commands:

```shell
cd /opt/endurancetrio-tracker/
sudo wget https://raw.githubusercontent.com/endurancetrio/endurancetrio-tracker/refs/heads/tracker/docker/deployment/docker-compose.yaml
sudo wget https://raw.githubusercontent.com/endurancetrio/endurancetrio-tracker/refs/heads/tracker/docker/deployment/.env-template
```

To confirm that the files were downloaded, check the output of the following command:

```shell
ls -lag
```

Check the content of the `docker-compose.yaml` file with the below command, and if necessary, use
the [nano text editor](https://www.nano-editor.org/) to introduce the necessary adaptations.

```shell
cat docker-compose.yaml
```

Create a `.env` file in the deployment folder, based on the provided `.env-template`, using
the following command:

```shell
sudo mv .env-template .env
```

The `.env` file manages environment-specific configurations and secrets.

**Key Environment Variables:**

| Variable                 | Description                                        | Required |
|--------------------------|----------------------------------------------------|----------|
| `VERSION`                | The image tag to deploy (e.g., `v1.0.0`)           | Yes      |
| `PUID`                   | User ID under which the container process runs     | Yes      |
| `PGID`                   | Group ID under which the container process runs    | Yes      |
| `TRACKER_EXT_PORT`       | The host port mapped to the API (e.g., `8080`)     | Yes      |
| `SPRING_PROFILES_ACTIVE` | Spring profile (e.g., `dev` or `prod`)             | Yes      |
| `DB_USERNAME`            | Database username                                  | Yes      |
| `DB_SECRET`              | Database password                                  | Yes      |
| `FIRST_OWNER`            | Name for the initial account initialization        | Optional |
| `FIRST_HASH`             | Bcrypt hash for the initial account initialization | Optional |

The user ID of the created `endurancetrio` user is obtained with the following command:

```shell
id -u endurancetrio
```

The group ID of the created `endurancetrio` user is obtained with the following command:

```shell
id -g endurancetrio
```

Open the `.env` file with the [nano text editor](https://www.nano-editor.org/)
and set the environment variables values. After setting the values for all variables,
save the file with the command `CTRL + O` and then close the editor with the command `CTRL + X`.

Given the sensitive nature of the variable `FIRST_HASH`, its value should be provided as a temporary
environment variable rather than persisted in the `.env` file to reduce attack surface.

> **Security Note**
>
> Refer to the [API Key Management](#api-key-management) section for details on generating
> the `FIRST_HASH`.

To complete deployment of **EnduranceTrio Tracker** REST API, execute the following command:

```shell
docker compose -p endurancetrio-tracker up -d
```

The output of the above command should show that **EnduranceTrio Tracker** REST API was deployed
with success. For a second confirmation, replace the ***{LABEL}*** as appropriate in the below
commands, and check its output.

```shell
docker ps
docker logs endurancetrio-tracker
curl -f http://localhost:{TRACKER_EXT_PORT}/actuator/health
```

> **Label Definition**
>
> + **{TRACKER_EXT_PORT}** : The external port on the Docker host for accessing the application

### Reverse Proxy Setup

To ensure that the necessary [Apache Server](https://httpd.apache.org/) modules for reverse
proxying are enabled, execute the following commands:

```shell
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod headers
sudo a2enmod rewrite
sudo systemctl reload apache2
```

To have a domain (or a subdomain) pointing to your **EnduranceTrio Tracker** REST API instance,
you need to start by [creating the DNS records](https://docs.digitalocean.com/products/networking/dns/how-to/manage-records/)
of the desired domain (or subdomain) redirecting to your server's IP address.

After creating the necessary [DNS Records](https://docs.digitalocean.com/products/networking/dns/),
create an Apache Virtual Host configuration file. This repository includes a template that sets
the following redirects:

- `domain` -> `localhost:<PORT>/`
- `api subdomain` -> `localhost:<PORT>/api/`
- `openapi subdomain` -> `localhost:<PORT>/openapi/`

To use the [provided template](./apache/vhost-template.conf), execute the following command:

```shell
sudo wget -P /etc/apache2/sites-available/ https://raw.githubusercontent.com/endurancetrio/endurancetrio-tracker/refs/heads/tracker/apache/vhost-template.conf
```

Then, replace the ***{LABEL}*** in the below command as appropriate and execute it to rename the file.

```shell
sudo mv /etc/apache2/sites-available/vhost-template.conf /etc/apache2/sites-available/{SECOND_LEVEL_DOMAIN_SLD}.conf
```

> **Label Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [*Second-level domain*](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)

Customize the Virtual Host configuration file downloaded with the previous command, using
[nano text editor](https://www.nano-editor.org/), and replace the included placeholders
as described in the following table:

| Placeholder               | Description                                                  |
|---------------------------|--------------------------------------------------------------|
| <SECOND_LEVEL_DOMAIN_SLD> | Domain name (e.g., 'example' in example.com)                 |
| <TOP_LEVEL_DOMAIN_TLD>    | Top-level domain (e.g., 'com', 'org', 'net')                 |
| <SERVER_ADMIN_EMAIL>      | Administrator email for server notifications                 |
| <TRACKER_EXT_PORT>        | External port where your tracker app runs (e.g., 8080, 8081) |

Check if it's necessary any further modifications, implement it if necessary and when finished,
save the file with the command `CTRL + O` and then exit the text editor with the command `CTRL + X`.

Validate the Apache Server configuration with the following command:

```shell
sudo apachectl configtest
```

To activate the new Virtual Host, replace the ***{LABEL}*** in the below commands as appropriate
and then execute it.

```shell
sudo a2ensite {SECOND_LEVEL_DOMAIN_SLD}.conf
sudo systemctl reload apache2
```

> **Label Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [*Second-level domain*](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)

If the domain of the Virtual Host created with the previous procedure has already its DNS Records
pointing to the server's IP address, replace the ***{LABELS}*** in the below URL as appropriate
and enter it into a browser’s address bar to test the new local Virtual Host.

```text
http://{SECOND_LEVEL_DOMAIN_SLD}.{TOP_LEVEL_DOMAIN_TLD}
```

> **Labels Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [*Second-level domain*](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)
> + **{TOP_LEVEL_DOMAIN_TLD}**    : The [TLD](https://en.wikipedia.org/wiki/Top-level_domain) (e.g., 'com', 'org', 'net')

### SSL Certificate

If [Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) isn't yet installed
on you server, install it and set the SSL certificate for the **EnduranceTrio Tracker** REST API
instance domain (or a subdomain) following the [instructions available here](https://github.com/EnduranceCode/server-setup-guide/blob/master/03-01-apache-server-management.md#312-apache--secure-apache-with-lets-encrypt).
If you already have SSL Certificates installed on your server with
[*Certbot*](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal), you can expand it
to include the new domain, or you can create a separate certificate for the new domain.

To expand an existing certificate, replace the ***{LABELS}*** in the below command as appropriate
and execute it.

```shell
sudo certbot --apache --cert-name {EXISTING_DOMAIN} --expand -d {EXISTING_DOMAIN} -d {NEW_DOMAIN}
```

> **Label Definition**
>
> + **{EXISTING_DOMAIN}** : The existing domain (or subdomain) that already has a SSL certificate
> + **{NEW_DOMAIN}**      : The new domain (or subdomain) to be included in the existing SSL certificate

Otherwise, to create a separate certificate for the new domain (or subdomain), replace
the ***{LABEL}*** in the below command as appropriate and execute it.

```shell
sudo certbot --apache -d {DOMAIN}
```

> **Label Definition**
>
> + **{DOMAIN}** : The domain (or subdomain) of the new SSL certificate

[Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) will create a file,
at `/etc/apache2/sites-available/` named `{SECOND_LEVEL_DOMAIN_SLD}-le-ssl.conf` and it's necessary
to check if the `RequestHeader` directives are set correctly. Replace the ***{LABEL}*** as appropriate
in the below command and execute it to edit with the [nano text editor](https://www.nano-editor.org/).

```shell
sudo nano {SECOND_LEVEL_DOMAIN_SLD}-le-ssl.conf
```

Ensure that the `RequestHeader` directives matches the content of the following snippet:

```text
RequestHeader set X-Forwarded-Proto "https"
RequestHeader set X-Forwarded-Port "443"
```

Check if it's necessary any further modifications, implement it if required and when finished, save
the file with the command `CTRL + O` and then exit the text editor  with the command `CTRL + X`.

Validate the Apache Server configuration and, if everything is correct, restart the
[Apache Server](https://httpd.apache.org/) to apply the updated configuration, executing the below
commands:

```shell
sudo apachectl configtest
sudo systemctl restart apache2
```

[SSL Labs Server Test](https://www.ssllabs.com/ssltest/) can be used to verify the certificate’s
grade and obtain detailed information about it, from the perspective of an external service.

To test if the [Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) renewal
script includes the new domain (or subdomain), execute the following command:

```shell
sudo certbot renew --dry-run
```

## License

This project is licensed under the [Functional Source License](https://fsl.software/), Version 1.1,
ALv2 Future License. See the [`LICENSE.md`](./LICENSE.md) file for details.
