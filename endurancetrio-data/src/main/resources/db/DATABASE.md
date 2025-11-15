# EnduranceTrio Tracker database

This file documents the process to manage the database used with **EnduranceTrio Tracker** REST API.

## Database migration

The [database migrations](https://en.wikipedia.org/wiki/Schema_migration) are made using
[Flyway](https://www.red-gate.com/products/flyway/).

The migration scripts are separated by type and database:

* **[DDL](https://en.wikipedia.org/wiki/Data_definition_language)** - These scripts are
  database-specific and contain schema definition (e.g., `CREATE TABLE`, `ALTER TABLE`)
  and are stored in:
    * [`migration/ddl/h2`](migration/ddl/h2): DDL scripts for the **H2** database
      (local/dev environments).
    * [`migration/ddl/postgres`](migration/ddl/postgres): DDL scripts for the **PostgreSQL**
      database (production environment).
* **[DML](https://en.wikipedia.org/wiki/Data_manipulation_language)** - These scripts contain
  data manipulation (e.g., `INSERT`, `UPDATE`) and are stored in:
    * [`migration/dml`](migration/dml)

The [migration scripts](https://documentation.red-gate.com/flyway/quickstart-how-flyway-works/why-database-migrations)
must respect the following naming convention (which is compatible with the
[Flyway naming patterns](https://www.red-gate.com/blog/database-devops/flyway-naming-patterns-matter)):

    Vxxx.yyy.zzz.nnn__free-description.sql

> ***xxx*** : Major version number at the time of the script creation
>
> ***yyy*** : Minor version number at the time of the script creation
>
> ***zzz*** : Patch version number at the time of the script creation
>
> ***nnn*** : Order number for version number at the time of the script creation
>
> ***free-description*** : A short free description of the scripts actions with words separated
> with dashes

### Spring Boot Configuration

To make this structure work, the `spring.flyway.locations` property is set with the adequate values
in the `application-{profile}.yaml` files, enabling Flyway to use the correct folders
for each database.

#### H2 database (local and dev profiles)

```yaml
spring:
  flyway:
    locations:
      - classpath:db/migration/ddl/h2
      - classpath:db/migration/dml
```

#### PostgreSQL database (prod profile)

```yaml
spring:
  flyway:
    locations:
      - classpath:db/migration/ddl/postgres
      - classpath:db/migration/dml
```

## Migration scripts

The DDL scripts are duplicated for each supported database (H2 and PostgreSQL) to ensure full
compatibility with the targeted databases.

### DDL Scripts

1. Tables creation for the **EnduranceTrio Tracker** REST API:
   - [V000.000.001.001__create-tables-h2.sql](migration/ddl/h2/V000.000.001.001__create-tables-h2.sql)
   - [V000.000.001.001__create-tables-postgres.sql](migration/ddl/postgres/V000.000.001.001__create-tables-postgres.sql)
