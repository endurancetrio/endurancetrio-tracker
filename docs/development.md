# Development Guide

This document covers development setup and practices for the **EnduranceTrio Tracker** project. For
an overview of the project, see the [main README.md](../README.md).

## Table of Contents

1. [Technology Stack](#technology-stack)
2. [API Key Management](#api-key-management)
3. [Database](#database)
4. [Installation](#installation)
5. [Code & Naming Conventions](#code--naming-conventions)
6. [Programmatic Version Management](#programmatic-version-management)

### Technology Stack

- **Java 21** - Latest LTS version for optimal performance and features
- **Spring Boot 4.0.0** - Modern application framework with latest stable features
- **Spring Data JPA** - Robust data persistence and repository abstraction
- **PostgreSQL 18** - An advanced Open Source Relational Database for data persistence
- **H2 Database** - In-memory database for testing
- **Flyway** - Database migration and version control
- **Spring Security** - API key authentication and security configuration
- **SpringDoc OpenAPI** - Automated Swagger/OpenAPI documentation generation
- **Maven** - Dependency management and build automation

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

Then, replace the placeholder in the below command as appropriate and execute it to generate
the bcrypt hash from the previously generated API key.

```shell
python3 -c "import bcrypt; print(bcrypt.hashpw('{API_KEY}'.encode('utf-8'), bcrypt.gensalt(rounds=12)).decode('utf-8'))"
```

> **Placeholder Definition**
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

# 2. Set environment variables and start application
export FIRST_OWNER="system"
export FIRST_HASH=$(python3 -c "import bcrypt; print(bcrypt.hashpw('${API_KEY}'.encode('utf-8'), bcrypt.gensalt(rounds=12)).decode('utf-8'))")

# 3. Check if the environment variables are correct
echo "First Owner: ${FIRST_OWNER}"
echo "First Hash: ${FIRST_HASH}"

# IMPORTANT: Store the raw API key securely - you won't be able to retrieve it later!
# Only the bcrypt hash should be stored in the database.
```

#### Store Hashes in the Database

Access the database console, replace the placeholders in the below SQL command as appropriate and
execute it to insert the new account into the `tracker_account` table (see the
[database section](#database)).

```sql
INSERT INTO tracker_account (owner, account_key, enabled, version, created_at)
    VALUES ('{OWNER}', '{API_KEY_HASH}', TRUE, 0, CURRENT_TIMESTAMP);
```

> **Placeholder Definition**
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
in the [API Endpoints](../README.md#api-endpoints) section of the [main README.md](../README.md).

### Database

The application uses a [PostgreSQL](https://www.postgresql.org/) database and an *H2 in-memory
database*, configured with PostgreSQL compatibility mode for testing purposes.

All database schema changes are managed with [Flyway](https://github.com/flyway/flyway). Migration
scripts are located in the `endurancetrio-data/src/main/resources/db/migration` folder and are
automatically executed on application startup. Migrations support both H2 (test)
and PostgreSQL (development and production).

The file [`DATABASE.md`](../endurancetrio-data/src/main/resources/db/DATABASE.md) documents the
development and management of the application's database.

#### Database, Schema, and Application User Setup

Login into the [PostgreSQL](https://www.postgresql.org/) server, replace the placeholders in the
commands below as appropriate, and execute them
to [create](https://www.postgresql.org/docs/current/sql-createdatabase.html) the database
for the **EnduranceTrio Tracker** REST API:

```shell
sudo -u postgres psql
```

```sql
CREATE DATABASE {DATABASE_NAME} ENCODING = 'UTF8' LC_COLLATE = 'C.UTF-8' LC_CTYPE = 'C.UTF-8' TEMPLATE = template0;
```

> **Placeholder Definition**
>
> + **{DATABASE_NAME}** : The name chosen for the new database;

Confirm that the database was created, then connect to it:

```sql
\l
\c {DATABASE_NAME}
```

Create the schema for the **EnduranceTrio Tracker** REST API:

```sql
CREATE SCHEMA IF NOT EXISTS {SCHEMA_NAME};
```

> **Placeholder Definition**
>
> + **{SCHEMA_NAME}** : The name chosen for the new schema;

Confirm the schema creation:

```sql
\dn
```

[Create a user](https://www.postgresql.org/docs/current/sql-createuser.html) for the
**EnduranceTrio Tracker** database/schema management:

```sql
CREATE USER {USERNAME} WITH PASSWORD '{PASSWORD}';
```

> **Placeholder Definition**
>
> + **{USERNAME}** : The new account name in the PostgreSQL Server;
> + **{PASSWORD}** : The password of the new account in the PostgreSQL Server.

Confirm that the user creation:

```sql
\du
```

[Grant](https://www.postgresql.org/docs/current/sql-grant.html) the necessary permissions to the
**EnduranceTrio Tracker** schema user:

```sql
GRANT CONNECT ON DATABASE {DATABASE_NAME} TO {USERNAME};
GRANT USAGE ON SCHEMA {SCHEMA_NAME} TO {USERNAME};
GRANT CREATE ON SCHEMA {SCHEMA_NAME} TO {USERNAME};
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA {SCHEMA_NAME} TO {USERNAME};
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA {SCHEMA_NAME} TO {USERNAME};
ALTER DEFAULT PRIVILEGES IN SCHEMA {SCHEMA_NAME} GRANT ALL PRIVILEGES ON TABLES TO {USERNAME};
ALTER DEFAULT PRIVILEGES IN SCHEMA {SCHEMA_NAME} GRANT ALL PRIVILEGES ON SEQUENCES TO {USERNAME};
```

> **Placeholder Definition**
>
> + **{DATABASE_NAME}** : The name chosen for the new database;
> + **{SCHEMA_NAME}** : The name chosen for the new schema;
> + **{USERNAME}** : The account name in the PostgreSQL Server to whom the privileges will be assigned.

Confirm that the privileges were granted:

```sql
\l
```

Once all the commands were executes, exit the PostgreSQL server:

```sql
\q
```

#### Troubleshooting

**Connection refused**: Ensure PostgreSQL is running:

```bash
sudo systemctl status postgresql
```

**Permission denied**: Verify user has proper grants:

```sql
SELECT * FROM information_schema.role_table_grants  WHERE grantee = '{USERNAME}';
```

### Installation

#### 1. Prerequisites

- [Java 21](https://javaalmanac.io/jdk/21/) or higher
- [Apache Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)

#### 2. Clone the repository

```shell
git clone git@github.com:EnduranceCode/endurancetrio-tracker.git
cd endurancetrio-tracker
```

#### 3. Configure application secrets

Create the `application-secrets.yaml` configuration file from the provided
[template](../endurancetrio-app/src/main/resources/template-secrets.yaml), with the following
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
- `application-prod.yaml` – For production environments.

You can manually activate a profile when running the application with `spring-boot:run`:

```shell
-Dspring-boot.run.profiles=local
```

Or, for standard JAR execution:

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

> **Placeholder Definition**
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
